import { HttpClient, HttpErrorResponse, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable} from '@angular/core';
import { Observable, catchError, forkJoin, retry, tap, throwError } from 'rxjs';
import { User } from '../models/user';
import { Router } from '@angular/router';
import { UserRequest } from '../interfaces/userRequest';
import { ProfileRequest } from '../interfaces/profileRequest';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient, private router:Router) { }

 // Método para registrar un usuario + profile
 registerUser(formData:FormData): Observable<User> { 
  
  return this.http.post<User>(this.apiUrl+'/auth/registration-user', formData).pipe(
  );
 }
 // Método para checkear si existe username
 checkUsernameExists(username: string): Observable<any> { 
  return this.http.get<any>(`${this.apiUrl}/api/user/checkUsername?username=${username}`).pipe(
  );
 }

// Método para obtener un usuario
 getUser(id:number):Observable<any>{
  return this.http.get<any>(this.apiUrl+"/api/user/"+id).pipe(
    catchError(this.handleError)
  )
}

// Método para obtener un profile
getUserProfile(id:number):Observable<any>{
  return this.http.get<any>(this.apiUrl+"/api/user/profile/"+id).pipe(
    catchError(this.handleError)
  )
}

// Método para obtener los datos del usuario actualmente logueado
 getCurrentUser(): Observable<UserRequest> {
  return this.http.get<UserRequest>(this.apiUrl+'/api/user/current').pipe(
    catchError(this.handleError)
  );
}

// Método para obtener los datos del perfil actualmente logueado
getCurrentUserProfile(id:number): Observable<ProfileRequest> {
  return this.http.get<ProfileRequest>(this.apiUrl+'/api/user/currentProfile/'+id).pipe(
    catchError(this.handleError)
  );
}

// Método para actualizar un usuario
updateUser(userRequest:UserRequest):Observable<any>
  {
    return this.http.put(this.apiUrl+'/api/user/updateUser', userRequest).pipe(
      catchError(this.handleError)
    )
  }

// Método para obtener los datos del perfil actualmente logueado
updatePhotoAndProfile(formData:FormData): Observable<any> {
  return this.http.put<any>(this.apiUrl+'/api/user/update-profile-image',formData).pipe(
    catchError(error => {
      if (error instanceof HttpErrorResponse) {
        console.error('Error en la solicitud:', error);
        console.error('Código de estado:', error.status);
        console.error('Mensaje de error:', error.message);
      }
      return this.handleError(error);
    })
  );
}

getFilterByPreference(): Observable<ProfileRequest[]> {
  return this.http.get<ProfileRequest[]>(this.apiUrl+'/api/user/filter-users');
}
getRandomProfile(): Observable<ProfileRequest> {
  return this.http.get<any>(this.apiUrl+'/api/user/random-user');
}
getRandomProfiles(): Observable<ProfileRequest[]> {
  return this.http.get<any[]>(this.apiUrl+'/api/user/random-users');
}
getUsers(userId: number): Observable<UserRequest[]> {
  return this.http.get<UserRequest[]>(this.apiUrl+'/api/user/all/'+userId);
   
}

getCountries(): Observable<any[]> {
  return this.http.get<any[]>('https://restcountries.com/v3.1/all');
   
}

private handleError(error: any): Observable<never> {
  console.error('Ocurrio un error:', error);
// Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
  return throwError('Something bad happened; please try again later.');
}
}
