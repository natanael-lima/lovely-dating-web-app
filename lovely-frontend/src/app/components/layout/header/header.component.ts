import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../../services/login.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NotificationService } from '../../../services/notification.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  currentSection: string = 'explore'; // Sección actual, inicialmente 'perfil'
  isLoggedIn: boolean = false;
  notificationsAvailable!: boolean;
  constructor(private loginService: LoginService,private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.loginService.isLoggedIn.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      this.checkNotifications();
    });
  }
  // Función para cambiar la sección actual
  changeSection(section: string) {
    this.currentSection = section;
  }

  checkNotifications(){
    this.notificationService.hasUnreadNotifications().subscribe(
      (data: boolean) => {
        this.notificationsAvailable = data;
        console.log("Server Response: ",this.notificationsAvailable);
      },
      (error) => {
        console.error('Error loading filtered profiles:', error);
        }
    );
  }


}
