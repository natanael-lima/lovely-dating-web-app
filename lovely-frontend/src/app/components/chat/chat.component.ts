import { Component, OnInit, ElementRef, ViewChild, AfterViewInit, Input, OnChanges, SimpleChanges, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { BehaviorSubject, Subscription, switchMap, zip } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import {FormsModule} from '@angular/forms';
import { Message } from '../../models/message';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserRequest } from '../../interfaces/userRequest';
import { MatchService } from '../../services/match.service';

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

  currentUser: UserRequest ={
    id:0,
    name:'',
    lastname:'',
    username:''
  };
  private subscription: Subscription | null = null;

  @Input() currentUserId!: number;
  @Input() targetUserId!: number;
  @Input() key!: number;

  constructor(private chatService: ChatService, private matchService: MatchService,private route: ActivatedRoute, private userService:UserService, private cdr: ChangeDetectorRef){

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
          this.userService.getCurrentUser().subscribe(
            (user: UserRequest) => {
              this.currentUser = user;
              // Ahora que tenemos el chatId y el usuario actual, podemos inicializar todo
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
  
sendMessage() {
    if (this.messageInput.trim() !== '' && this.chatId) {
      const message = {
        content: this.messageInput,
        senderId: this.currentUser.id
      } as Message;
    console.log('Sending message from component:', message);
    this.chatService.sendMessage(this.chatId, this.currentUser.id,message);
    this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    this.messageInput = ''; // Limpiar el campo de entrada después de enviar el mensaje
    
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
    isSender: message.senderId === this.currentUser.id
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
