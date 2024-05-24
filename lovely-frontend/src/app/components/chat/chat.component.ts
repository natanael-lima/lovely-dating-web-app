import { Component, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { BehaviorSubject, switchMap, zip } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import {FormControl, FormsModule} from '@angular/forms';
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
export class ChatComponent implements OnInit {
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

  constructor(private chatService: ChatService, private matchService: MatchService,private route: ActivatedRoute, private userService:UserService){

  }

  ngOnInit(): void {
     // Esperar a que los parámetros de la ruta estén disponibles
     this.route.paramMap.pipe(
      switchMap(params => {
        const userId1 = +params.get('userId1')!;
        const userId2 = +params.get('userId2')!;
        
        // Obtener el ID del chat usando los IDs de los usuarios
        return this.matchService.getMatchByIds(userId1, userId2);
      })
    ).subscribe(
      (data: any) => {
        this.chatId = data.id; // Asumiendo que la respuesta del servicio contiene el ID del chat
      },
      (error) => {
        console.error('Error al obtener el ID del chat:', error);
      }
    );

    this.userService.getCurrentUser().subscribe(
      (user: UserRequest) => {
        this.currentUser = user;
        
        this.joinChat();
        this.loadInitialMessages(); // Cargar mensajes iniciales al cargar la página
        this.subscribeToMessages();
      },
      (error) => {
        console.error('Error al obtener el usuario actual:', error);
      }
    );
  }

  sendMessage() {
    const message = {
      content: this.messageInput,
      senderId: this.currentUser.id
    } as Message
    // Enviar el mensaje al servicio de chat
    console.log('mjs:'+message.content)
    console.log('objeto:'+this.chatId+'-'+ this.currentUser.id+'-'+message.content)
    this.chatService.sendMessage(this.chatId, this.currentUser.id,message);
    this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    this.messageInput = ''; // Limpiar el campo de entrada después de enviar el mensaje
  }

  private joinChat() {
    this.chatService.joinChat(this.chatId);
    this.chatService.getMessagesForChat(this.chatId);
  }

  private loadInitialMessages() {
    this.chatService.getMessagesForChat(this.chatId);
    console.log();
  }

  private subscribeToMessages() {
    this.chatService.getMessageSubject().subscribe((messages: Message[]) => {
      this.messageList = messages.map(this.formatMessage);
    });
  }

  private formatMessage = (message: Message) => ({
    ...message,
    isSender: message.senderId === this.currentUser.id
  });

  /*lisenerMessage() {
    this.chatService.getMessageSubject().subscribe((messages: any) => {
      this.messageList = messages.map((item: any)=> ({
        ...item,
        message_side: item.senderId === this.currentUser.id ? 'sender' : 'receiver'
      }))
    });
  }*/

<<<<<<< HEAD
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
  
  
=======

>>>>>>> a7f63b9c399c0f1b1d5f050b0b558954eb287074

}
