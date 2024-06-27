import { Injectable, NgZone, OnInit } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable, interval, startWith, switchMap } from 'rxjs';
import { Message } from '../models/message';
import { UserRequest } from '../interfaces/userRequest';
import { UserService } from './user.service';
import { HttpClient } from '@angular/common/http';
import { MessageRequest } from '../interfaces/messageRequest';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private stompClient: any;
  private messageSubject: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>([]);
  public message$: Observable<Message[]> = this.messageSubject.asObservable();  // Aquí se define el observable
  private connected: boolean = false;

  constructor(private http: HttpClient, private zone: NgZone) { 
    this.initConnenctionSocket();
  }

 
// Inicializa la conexion
initConnenctionSocket() {
    const url = '//localhost:3000/chat-socket';
    const socket = new SockJS(url);

    this.stompClient = Stomp.over(() => socket);

    this.stompClient.connect({}, 
      () => {
        console.log('Connected to WebSocket');
      },
      (error: any) => {
        console.error('Error connecting to WebSocket: ', error);
      }
    );
}

// Se une al chat y obtiene los mensajes de ese chat
joinChat(chatId: number) {
  console.log('Joining chat:', chatId);
  if (this.stompClient && this.stompClient.connected) {
    this.stompClient.subscribe(`/topic/${chatId}`, (message: any) => {
      const messageContent = JSON.parse(message.body);
      const currentMessages = this.messageSubject.getValue();
      currentMessages.push(messageContent);
      this.zone.run(() => {
        this.messageSubject.next(currentMessages);
      });
      console.log('Message received:', messageContent);
    });
  } else {
    console.error('STOMP client not connected');
  }
  }

// Envia un nuevo mensaje y obtiene los mensajes
sendMessage(chatId: number, userId:number, message: Message) {
    console.log('Sending message service:', message);
    this.stompClient.send(`/app/chat/${chatId}/${userId}`, {}, JSON.stringify(message));
}

// Permite suscribirse a un observable y recibir actualizaciones (notifica automáticamente a todos los suscriptores)
getMessageSubject() {
    console.log('ServidorSubjectObservable');
    return this.messageSubject.asObservable();
 }
// Obtener todos los mensajes de un chat específico desde el servidor y actualizar el BehaviorSubject messageSubject con la respuesta.
getMessagesForChat(chatId: number) {
  const url = `http://localhost:3000/api/chats/${chatId}/messages`;
  this.http.get<Message[]>(url).subscribe((response) => {
    console.log('Respuesta del servidor:', response);
    this.messageSubject.next(response);
  });
}
// Busca mensajes de un chat especifica con su id 
findMessagesByChatId(chatId: number): Observable<Message[]> {
  return this.http.get<Message[]>('//localhost:3000/api/chats/'+{chatId}+'/messages');
}


  /*this.stompClient.reconnect_delay = 5000; // Reintentar la conexión cada 5 segundos
    this.stompClient.connect({}, (frame: any) => {
      this.connected = true;
      console.log('WebSocket connected', frame);
    }, (error: any) => {
      console.error('WebSocket connection error', error);
      this.connected = false;
      console.log('Attempting to reconnect...');
      setTimeout(() => {
        this.initConnenctionSocket();
      }, 5000);
    });----------------------
        //if (!this.connected) {
    //  console.error('WebSocket is not connected send');
    //  return;
    //}
    
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame: any) => {
      console.log('WebSocket connected: ' + frame);
    }, (error: any) => {
      console.error('Error connecting to WebSocket: ', error);
    });
    */
}