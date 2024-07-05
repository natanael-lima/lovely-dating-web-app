import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../../services/login.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

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
  
  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
    this.loginService.isLoggedIn.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
    });
  }
  // Función para cambiar la sección actual
  changeSection(section: string) {
    this.currentSection = section;
  }

}
