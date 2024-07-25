import { HttpClient, HttpErrorResponse, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable} from '@angular/core';
import { Observable, catchError, forkJoin, retry, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { UserRequest } from '../interfaces/userRequest';
import { UserDTO } from '../interfaces/userDTO';
import { PreferenceDTO } from '../interfaces/preferenceDTO';
import { PasswordRequest } from '../interfaces/passwordRequest';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient, private router:Router) { }

 // Método para registrar un usuario completo con todos los datos
 registerUser(formData:FormData): Observable<UserDTO> { 
  
  return this.http.post<UserDTO>(this.apiUrl+'/auth/registration-profile', formData).pipe(
  );
 }

 // Método para checkear si existe username para mostrar mensaje y elegir otro username
 checkUsernameExists(username: string): Observable<any> { 
  return this.http.get<any>(`${this.apiUrl}/api/user/check-username?username=${username}`).pipe(
  );
 }

// Método para obtener un usuario por ID
 getUser(id:number):Observable<any>{
  return this.http.get<any>(this.apiUrl+"/api/user/get-user/"+id).pipe(
    catchError(this.handleError)
  )
}

// Método para obtener un datos de user basico
getUserBasic(id:number):Observable<any>{
  return this.http.get<any>(this.apiUrl+"/api/user/get-user/basic/"+id).pipe(
    catchError(this.handleError)
  )
}

// Método para obtener los datos del usuario completo actualmente logueado
 getCurrentProfile(): Observable<any> {
  return this.http.get<any>(this.apiUrl+'/api/user/current-user-profile').pipe(
    catchError(this.handleError)
  );
}

// Método para actualizar profile detail o photo profile
updateUserDetail(data:FormData,id:number): Observable<any> {
  return this.http.put(`${this.apiUrl}/api/profile/update-profile-detail/${id}`,data).pipe(
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
// Método para actualizar un usuario 
updateUserBasic(data:UserRequest):Observable<any>
  {
    return this.http.put(`${this.apiUrl}/api/user/update-user/${data.id}`, data).pipe(
      catchError(this.handleError)
    )
  }
// Método para actualizar un usuario 
updateUserVisible(data:UserRequest):Observable<any>
  {
    return this.http.put(`${this.apiUrl}/api/user/update-visiblity/${data.id}`, data).pipe(
      catchError(this.handleError)
    )
  }

// Método para actualizar un usuario 
updateUserPreference(data:PreferenceDTO):Observable<any>
  {
    return this.http.put(`${this.apiUrl}/api/preference/update-preference/${data.id}`, data).pipe(
      catchError(this.handleError)
    )
  }

getFilterByPreference(): Observable<any[]> {
  return this.http.get<any[]>(this.apiUrl+'/api/user/filter-users');
}

getUsers(id: number): Observable<any[]> {
  return this.http.get<any[]>(this.apiUrl+'/api/user/match-all/'+id);
}

getCountries(): Observable<any[]> {
  return this.http.get<any[]>('https://restcountries.com/v3.1/all');
   
}

// Método para actualizar un password
updatePassword(id:number,  change :PasswordRequest): Observable<PasswordRequest> {
  return this.http.put<PasswordRequest>(`${this.apiUrl}/api/user/${id}/change-password`,change).pipe(
    catchError(this.handleError)
  );
}


private handleError(error: any): Observable<never> {
  console.error('Ocurrio un error:', error);
// Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
  return throwError('Something bad happened; please try again later.');
}
}
