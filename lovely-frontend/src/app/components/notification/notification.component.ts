import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface Notification {
  id: number;
  avatar: string;
  name: string;
  content: string;
  time: string;
  isUnread: boolean;
}
@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent {
  notifications: Notification[] = [
    {
      id: 1,
      avatar: 'https://via.placeholder.com/50',
      name: 'Ana García',
      content: 'Tienes un nuevo match con',
      time: 'Hace 5 minutos',
      isUnread: true
    },
    {
      id: 2,
      avatar: 'https://via.placeholder.com/50',
      name: 'Beatriz López',
      content: 'te ha enviado un mensaje',
      time: 'Hace 15 minutos',
      isUnread: true
    },
    {
      id: 3,
      avatar: 'https://via.placeholder.com/50',
      name: 'Carmen Fernández',
      content: 'Tienes un nuevo match con',
      time: 'Hace 30 minutos',
      isUnread: false
    },
    {
      id: 4,
      avatar: 'https://via.placeholder.com/50',
      name: 'Diana Martín',
      content: 'Tienes un nuevo match con',
      time: 'Hace 45 minutos',
      isUnread: true
    },
    {
      id: 5,
      avatar: 'https://via.placeholder.com/50',
      name: 'Elena Pérez',
      content: 'te ha enviado un mensaje',
      time: 'Hace 1 hora',
      isUnread: true
    },
    {
      id: 6,
      avatar: 'https://via.placeholder.com/50',
      name: 'Flor Rodríguez',
      content: 'te ha enviado un mensaje',
      time: 'Hace 2 horas',
      isUnread: true
    },
    {
      id: 7,
      avatar: 'https://via.placeholder.com/50',
      name: 'Gabriela Ramírez',
      content: 'te ha enviado un mensaje',
      time: 'Hace 3 horas',
      isUnread: false
    },
    {
      id: 8,
      avatar: 'https://via.placeholder.com/50',
      name: 'Hilda Sánchez',
      content: 'te ha enviado un mensaje',
      time: 'Hace 4 horas',
      isUnread: false
    },
    {
      id: 9,
      avatar: 'https://via.placeholder.com/50',
      name: 'Inés Ruiz',
      content: 'te ha enviado un mensaje',
      time: 'Hace 5 horas',
      isUnread: false
    },
    {
      id: 10,
      avatar: 'https://via.placeholder.com/50',
      name: 'Julia Torres',
      content: 'te ha enviado un mensaje',
      time: 'Hace 6 horas',
      isUnread: false
    }
];


  constructor(private router: Router) {}

  markAsRead(notification: Notification): void {
    notification.isUnread = false;
    // Aquí puedes añadir lógica para actualizar el estado en el backend
    console.log('Marcando como leída:', notification.id);
    
    // Redirigir (ajusta según tus rutas)
    // this.router.navigate(['/notification', notification.id]);
  }
}
