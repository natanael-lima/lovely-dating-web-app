import { Injectable, OnInit } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';
import { Message } from '../models/message';
import { UserRequest } from '../interfaces/userRequest';
import { UserService } from './user.service';
import { HttpClient } from '@angular/common/http';
import { MessageRequest } from '../interfaces/messageRequest';

@Injectable({
  providedIn: 'root'
})
export class ChatService implements OnInit{

  private stompClient: any;
  private messageSubject: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>([]);
  private connected: boolean = false;


  constructor(private http: HttpClient) { 
    this.initConnenctionSocket();
  }

  ngOnInit(): void {
    this.initConnenctionSocket();
  }

  initConnenctionSocket() {
    const url = '//localhost:3000/chat-socket';
    const socket = new SockJS(url);
    //this.stompClient = Stomp.over(socket)
    //console.log('WebSocket connected');

    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame: any) => {
      console.log('Connected: ' + frame);
    }, (error: any) => {
      console.error('Error connecting to WebSocket: ', error);
    });
  }

  joinChat(chatId: number) {
    this.stompClient.connect({}, ()=>{
      this.stompClient.subscribe(`/topic/${chatId}`, (messages: any) => {
        const messageContent = JSON.parse(messages.body);
        const currentMessage = this.messageSubject.getValue();
        currentMessage.push(messageContent);

        this.messageSubject.next(currentMessage);
        console.log(messageContent);
      })
    })
  }

  sendMessage(chatId: number, userId:number, message: Message) {
    //if (!this.connected) {
    //  console.error('WebSocket is not connected send');
    //  return;
    //}
    this.stompClient.send(`/app/chat/${chatId}/${userId}`, {}, JSON.stringify(message));
    this.getMessagesForChat(chatId);
  }

  
  getMessageSubject() {
    return this.messageSubject.asObservable();
 }
 
 getMessagesForChat(chatId: number) {
  const url = `http://localhost:3000/api/chats/${chatId}/messages`;
  this.http.get<Message[]>(url).subscribe((response) => {
    console.log('Respuesta del servidor:', response);
    this.messageSubject.next(response);
  });
}

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
    });*/
}