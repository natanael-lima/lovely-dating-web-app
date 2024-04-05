import { HttpClient } from '@angular/common/http';
import { Injectable,PLATFORM_ID, Inject, afterRender  } from '@angular/core';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { User } from '../models/user';
import { LoginRequest } from '../interfaces/loginRequest';

@Injectable({
  providedIn: 'root'
})

export class LoginService {
  private apiUrl = 'http://localhost:3000';
  isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) { 
     this.isLoggedIn = new BehaviorSubject<boolean>(sessionStorage.getItem("token") !== null);
  }

  loginUser(credentials: LoginRequest): Observable<any>{
    return this.http.post<any>(this.apiUrl+'/auth/login', credentials).pipe(
      tap((res :any) => {
        // Verificar si la respuesta tiene un token antes de guardarlo
        if (res.token) {
          // Almacenar el token JWT en el almacenamiento local después de un inicio de sesión exitoso
          sessionStorage.setItem("token", res.token);
          this.isLoggedIn.next(true);
        }
      })
    );
   }

   logoutUser(): void {
    sessionStorage.removeItem('token');
    // En el método de logout
    this.isLoggedIn.next(false);
   }

   get userToken(){
    return sessionStorage.getItem('token');
  }

}
