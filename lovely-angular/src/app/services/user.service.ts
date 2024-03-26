import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:3000/api';
  urlBase:string="http://localhost:3000/api/users";

  constructor(private http: HttpClient) { }

  getRandomProfile(): Observable<User> {
    return this.http.get<User>(this.urlBase+ '/random-user');
  }

  xdsd(): Observable<User> {
    return this.http.get<User>(this.urlBase+ '/random-user');
  }

  // Método para registrar un usuario
  registerUser(user: User, imageFile: File): Observable<User> {
    const formData: FormData = new FormData();
    formData.append('imageFile', imageFile);
    formData.append('user', JSON.stringify(user));

    return this.http.post<User>(this.apiUrl+'/auth/registration-user', formData);
  }
}
