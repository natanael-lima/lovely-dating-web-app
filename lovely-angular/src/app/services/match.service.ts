import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  urlBase:string="http://localhost:3000/api/users";

  constructor(private http: HttpClient) { }

  likeUser(likerId: number, targetId: number): Observable<any> {
    return this.http.post<any>(`${this.urlBase}/like`, { likerId, targetId });
  }

  dislikeUser(likerId: number, targetId: number): Observable<any> {
    return this.http.post<any>(`${this.urlBase}/dislike`, { likerId, targetId });
  }
}
