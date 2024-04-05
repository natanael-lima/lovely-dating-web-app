import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormBuilder, FormsModule } from '@angular/forms';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  currentSection: string = 'perfil'; // Sección actual, inicialmente 'perfil'
  currentUser: User = new User('','','','');
  isLoggedIn: boolean = false;

 // Función para cambiar la sección actual
  changeSection(section: string): void {
    this.currentSection = section;
  }
  
  constructor(private userService:UserService, private formBuilder:FormBuilder, private loginService:LoginService, private http:HttpClient,private router:Router ){
    
  }
  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(
      (user: User) => {
        this.currentUser = user;
        
      },
      (error) => {
        console.error('Error al obtener el usuario actual:', error);
      }
    );
    
  }
  
  onLogout(): void {
    // Llamar al método logout() del servicio LoginService
    this.loginService.logoutUser();
    this.router.navigateByUrl('/user/login');
  }

 



}
