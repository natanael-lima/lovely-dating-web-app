import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, forkJoin, map, switchMap } from 'rxjs';
import { Match } from '../models/match';
import { ProfileRequest } from '../interfaces/profileRequest';
import { UserRequest } from '../interfaces/userRequest';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  private apiUrl = 'http://localhost:3000';
  urlBase:string="http://localhost:3000/api/users";

  constructor(private http: HttpClient) { }

  likeUser(targetId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/matches/like/${targetId}`,{});
  }

  dislikeUser(targetId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/api/matches/dislike/${targetId}`,{});
  }

  getMyMatches(id: number){
    return this.http.get<any>(this.apiUrl+"/api/matches/"+id);
  }

  getChatById(id: number){
    return this.http.get<any>(this.apiUrl+"/api/matches/chat/"+id);
  }
  
  getMatchByIds(userId1:number,userId2:number){
    return this.http.get<any>(this.apiUrl+"/api/matches/byusers/"+userId1+"/"+userId2);
  }

  checkForMatch(userId1: number, userId2: number): Observable<{ match: boolean }> {
    return this.http.get<{ match: boolean }>(`${this.apiUrl}/api/matches/check-match?profileId1=${userId1}&profileId2=${userId2}`);
  }

}
