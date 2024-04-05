import { Component } from '@angular/core';
import { FormBuilder, FormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { Login } from '../../models/login';
import { catchError, tap, throwError } from 'rxjs';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { LoginRequest } from '../../interfaces/loginRequest';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  creds: LoginRequest ={
    username:'',
    password:''
  };

  constructor( private loginService: LoginService, private http:HttpClient, private router:Router) { 
    
  }
  
  errorMessage = false;
  onLogin(){
    debugger;
      this.loginService.loginUser(this.creds).pipe(
      tap(response => {
        // Registro exitoso, mostrar el mensaje
        alert("Login Success");
        this.router.navigateByUrl('/perfil');
      }),
      catchError(error => {
        console.error('Error al iniciar sesión:', error);
        this.errorMessage = true;
        // Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
        return throwError(error);
      })
    ).subscribe();
  }

}