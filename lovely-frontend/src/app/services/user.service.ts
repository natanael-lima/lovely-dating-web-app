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

 // Método para registrar un usuario
 registerUser(formData:FormData): Observable<User> { 
  
  return this.http.post<User>(this.apiUrl+'/auth/registration-user', formData).pipe(
  );
 }
 // Método para registrar un profile
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

// Método para obtener un usuario
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

updateUser(userRequest:UserRequest):Observable<any>
  {
    return this.http.put(this.apiUrl+'/api/user/updateUser', userRequest).pipe(
      catchError(this.handleError)
    )
  }
// Método para obtener los datos del perfil actualmente logueado
updateUserProfile(profileRequest:ProfileRequest): Observable<any> {
  return this.http.put<ProfileRequest>(this.apiUrl+'/api/user/updateProfile',profileRequest).pipe(
    catchError(this.handleError)
  );
}

// Método para actualizar el perfil con la foto
updateUserProfilePhoto(formData: FormData): Observable<HttpEvent<any>>  {
  const _headers = new HttpHeaders({
    'Content-Type': 'multipart/form-data'
  });

  // Mostrar el contenido del FormData en la consola
  console.log('api formphotofile:',formData.get('photoFile'));
  // Mostrar el contenido del FormData en la consola
  console.log('api formreq:',formData.get('req'));
  const req = new HttpRequest('PUT', this.apiUrl+'/api/user/updateProfilePhoto', formData, {
    responseType: 'json',headers:_headers});

  return this.http.request(req);
}

private handleError(error: any): Observable<never> {
  console.error('Ocurrio un error:', error);
// Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
  return throwError('Something bad happened; please try again later.');
}

getRandomProfiles(): Observable<ProfileRequest> {
    return this.http.get<any>(this.apiUrl+'/api/user/random-users');
}
getRandomProfile(): Observable<ProfileRequest> {
  return this.http.get<any>(this.apiUrl+'/api/user/random-user');
}

getUsers(userId: number): Observable<UserRequest[]> {
  return this.http.get<UserRequest[]>(this.apiUrl+'/api/user/all/'+userId);
   
}

}
