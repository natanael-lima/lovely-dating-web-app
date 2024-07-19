import { Component, OnInit, ElementRef, ViewChild, AfterViewInit, Input, OnChanges, SimpleChanges, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { BehaviorSubject, Subscription, switchMap, zip } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import {FormsModule} from '@angular/forms';
import { Message } from '../../models/message';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatchService } from '../../services/match.service';
import { UserResponse } from '../../interfaces/userResponse';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.css'
})
export class ChatComponent implements OnInit, OnChanges, OnDestroy {
  @ViewChild('chatContainer')
  chatContainer!: ElementRef;

  messageInput: string = '';
  messageList: any[] = [];
  chatId!: number;

  currentProfile!: UserResponse; 
  targetProfile!: UserResponse;

  private subscription: Subscription | null = null;

  @Input() currentUserId!: number;
  @Input() targetUserId!: number;
  @Input() key!: number;

  constructor(private chatService: ChatService, private matchService: MatchService, private notificationService: NotificationService, private userService:UserService, private sanitizer: DomSanitizer,){
  this.currentProfile = {
        id: 0,
        username: '',
        lastname: '',
        name: '',
        preference: {
          id: 0,
          maxAge: 0,
          minAge: 0,
          likeGender: '',
          location: '',
          distance: 0,
          interests: []
        },
        profileDetail: {
          id: 0,
          phone: '',
          gender: '',
          birthDate: new Date(),
          description: '',
          work: '',
          photo: null,
          photoFileName: '',
          timestamp: ''
        }
      };
      this.targetProfile = {
        id: 0,
        username: '',
        lastname: '',
        name: '',
        preference: {
          id: 0,
          maxAge: 0,
          minAge: 0,
          likeGender: '',
          location: '',
          distance: 0,
          interests: []
        },
        profileDetail: {
          id: 0,
          phone: '',
          gender: '',
          birthDate: new Date(),
          description: '',
          work: '',
          photo: null,
          photoFileName: '',
          timestamp: ''
        }
      };
  }

  ngOnInit(): void {
    console.log('ChatComponent initialized with:', this.currentUserId, this.targetUserId, this.key);
    
    //this.initializeChat(); //genera duplex
  }
  ngOnDestroy(): void {
      if (this.subscription) {
        this.subscription.unsubscribe();
    }
  }
  //Para detectar el cambio de chat y actualizar
  ngOnChanges(changes: SimpleChanges): void {
    console.log('ChatComponent changes:', changes);
    if (changes['currentUserId'] || changes['targetUserId'] || changes['key']) {
      console.log("Se cambio de chat:",this.chatId);
      this.initializeChat(); //genera duplex
    }
  }

private initializeChat(): void {
    console.log('Inicializamos chat');
    if (this.currentUserId && this.targetUserId) {
      // Primero, obtenemos el ID del chat
      this.matchService.getMatchByIds(this.currentUserId, this.targetUserId).subscribe(
        (data: any) => {
          this.chatId = data.id;
          console.log('Chat ID obtenido:', this.chatId);
          this.userService.getCurrentProfile().subscribe(
            (user: UserResponse) => {
              this.currentProfile = user;
              // Ahora que tenemos el chatId y el usuario actual, podemos inicializar todo
              this.getUserTarget();
              this.joinChat();
              this.loadInitialMessages();
              this.subscribeToMessages();
            },
            (error) => {
              console.error('Error al obtener el usuario actual:', error);
            }
          );
        },
        (error) => {
          console.error('Error al obtener el ID del chat:', error);
        }
      );
    }
  }
  getImageUrl(imageData: File | null): SafeUrl {
    // Asegúrate de que los datos de la imagen estén en el formato correcto (base64)
    if (imageData && typeof imageData === 'string') {
      const imageUrl = 'data:image/jpeg;base64,' + imageData;
      return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
    } else {
      // Si los datos de la imagen no están en el formato correcto, devuelve una URL de imagen predeterminada o null
      return 'https://i.postimg.cc/7hsdHJL7/nofound2.png';
    }
  }

  getUserTarget(){
    this.userService.getUser(this.targetUserId).subscribe(
      (userTarger: UserResponse) => {
        this.targetProfile = userTarger;
      },
      (error) => {
        console.error('Error al obtener el usuario target:', error);
      }
    );
  }

sendMessage() {
    if (this.messageInput.trim() !== '' && this.chatId) {
      const message = {
        content: this.messageInput,
        senderId: this.currentProfile.id
      } as Message;
    console.log('Sending message from component:', message);
    this.chatService.sendMessage(this.chatId, this.currentProfile.id,message);
    this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    this.messageInput = ''; // Limpiar el campo de entrada después de enviar el mensaje
    this.notificationService.registerNotificationMessages(this.targetProfile.id).subscribe(
      (response) => {
        console.log("Mensaje server",response);
      },
      (error) => {
        console.error('Error loading filtered profiles:', error);
  
      }
    );
    }
  }
  handleKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.repeat) {
      this.sendMessage();
      event.preventDefault(); // Prevent default action of Enter key to avoid form submission or other side effects
    }
  }
  private joinChat() {
    if (this.chatId) {
      console.log('Joining chat in component:', this.chatId);
      this.chatService.joinChat(this.chatId);
       this.chatService.getMessagesForChat(this.chatId);
    }
  }

  private loadInitialMessages() {
    console.log('Cargando mensajes:');
    this.chatService.getMessagesForChat(this.chatId);
    
  }

  private subscribeToMessages() {
    this.chatService.getMessageSubject().subscribe((messages: Message[]) => {
      this.messageList = messages.map(this.formatMessage);
      this.scrollToBottom();
    });

    /*this.chatService.message$.subscribe(  // Aquí nos suscribimos al observable
      (messages: Message[]) => {
        console.log('Mensajes del servidor:', messages);
        this.messageList = messages.map(this.formatMessage);
        this.scrollToBottom();
      }
    );*/
   
  }

  private scrollToBottom() {
    setTimeout(() => {
      if (this.chatContainer) {
        this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
      }
    }, 100);
  }

  private formatMessage = (message: Message) => ({
    ...message,
    isSender: message.senderId === this.currentProfile.id
  });


  formatTimestamp(timestamp: string): string {
    const date = new Date(timestamp);
    const day = date.getDate();
    const monthNames = ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic'];
    const month = monthNames[date.getMonth()];
    const year = date.getFullYear();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const formattedHours = hours < 10 ? '0' + hours : hours;
    const formattedMinutes = minutes < 10 ? '0' + minutes : minutes;

    return `${day} ${month} ${year}, ${formattedHours}:${formattedMinutes}`;
  }
    /*lisenerMessage() {
    this.chatService.getMessageSubject().subscribe((messages: any) => {
      this.messageList = messages.map((item: any)=> ({
        ...item,
        message_side: item.senderId === this.currentUser.id ? 'sender' : 'receiver'
      }))
    });
  }*/
  

}
