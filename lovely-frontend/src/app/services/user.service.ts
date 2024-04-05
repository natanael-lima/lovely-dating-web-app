import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable} from '@angular/core';
import { Observable, catchError, retry, tap, throwError } from 'rxjs';
import { User } from '../models/user';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient, private router:Router) { }

 // Método para registrar un usuario
 registerUser(user: User): Observable<User> { 
  return this.http.post<User>(this.apiUrl+'/auth/registration-user', user).pipe(
    
  );
 }
// Método para obtener un usuario
 getUser(id:number):Observable<User>{
  return this.http.get<User>(this.apiUrl+"/api/user/"+id).pipe(
    catchError(this.handleError)
  )
}

// Método para obtener los datos del usuario actualmente logueado
 getCurrentUser(): Observable<User> {
  return this.http.get<User>(this.apiUrl+'/api/user/current').pipe(
    catchError(this.handleError)
  );
}

private handleError(error: any): Observable<never> {
  console.error('Ocurrio un error:', error);
// Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
  return throwError('Something bad happened; please try again later.');
}

getRandomProfile(): Observable<User> {
    return this.http.get<User>(this.apiUrl+ 'user/random-user');
}

xdsd(): Observable<User> {
    return this.http.get<User>(this.apiUrl+ 'user/random-user');
  }

}
