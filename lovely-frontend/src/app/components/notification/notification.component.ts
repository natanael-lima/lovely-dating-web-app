import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NotificationDTO } from '../../interfaces/notificationDTO';
import { NotificationService } from '../../services/notification.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

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
export class NotificationComponent  implements OnInit {
  
  notifications: NotificationDTO[] = [];
  constructor(private router: Router, private notificationService: NotificationService,private sanitizer: DomSanitizer ) {

  }
  ngOnInit() {

    this.loadNotifications();
  }

  loadNotifications() {
    this.notificationService.getAllNotificationByUserId().subscribe(
      (data: NotificationDTO[]) => {
        this.notifications = data;
        console.log(this.notifications);
      },
      (error) => {
        console.error('Error loading filtered profiles:', error);
        }
    );
  }



  markAsRead(notification: NotificationDTO): void {
    notification.isUnread = false;
    // Aquí puedes añadir lógica para actualizar el estado en el backend
    console.log('Marcando como leída:', notification.id);
    
    this.notificationService.maskNotification(notification).subscribe(
      (response) => {
        console.log("Server response:",response);
      },
      (error) => {
        console.error('Error loading filtered profiles:', error);
        }
    );

    // Redirigir (ajusta según tus rutas)
    // this.router.navigate(['/notification', notification.id]);
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

  timeAgo(time: string): string {
    console.log('Original time:', time); // Verifica el valor recibido
    const now = new Date();
    const date = new Date(time); // Convierte la cadena de texto a Date
    const diff = Math.floor((now.getTime() - date.getTime()) / 1000);

    console.log('Time difference in seconds:', diff); // Verifica el cálculo

    if (diff < 60) return 'Hace unos segundos';
    const minutes = Math.floor(diff / 60);
    if (minutes < 60) return `Hace ${minutes} minutos`;
    const hours = Math.floor(minutes / 60);
    if (hours < 24) return `Hace ${hours} horas`;
    const days = Math.floor(hours / 24);
    if (days < 30) return `Hace ${days} días`;
    const months = Math.floor(days / 30);
    if (months < 12) return `Hace ${months} meses`;
    const years = Math.floor(months / 12);
    return `Hace ${years} años`;
  } 
}
