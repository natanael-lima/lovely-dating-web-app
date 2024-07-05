import { Component, Inject, NgModule, OnInit } from '@angular/core';
import { Router, RouterOutlet, NavigationStart } from '@angular/router';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { CommonModule } from '@angular/common';
import { ChatComponent } from './components/chat/chat.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { HomeComponent } from './components/home/home.component';
import { HttpClientModule } from '@angular/common/http'; // Importa HttpClientModule
import { UserFormComponent } from './components/user-form/user-form.component';
import { FormsModule, NgModel } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { LoginComponent } from './components/login/login.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { NotificationComponent } from './components/notification/notification.component';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ CommonModule,RouterOutlet, HeaderComponent, FooterComponent, ChatComponent, PerfilComponent, HomeComponent,HttpClientModule,FormsModule, UserFormComponent,LoginComponent,WelcomeComponent,NotificationComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent{
  title = 'lovely-angular';


}
