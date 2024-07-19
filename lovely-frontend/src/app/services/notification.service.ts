import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { NotificationDTO } from '../interfaces/notificationDTO';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) { }

  registerNotificationMessages(id: number){
    return this.http.post<any>(`${this.apiUrl}/api/notification/register-notification-message/${id}`,{})
  }
  registerNotificationMatches(userId1:number,userId2:number){
    return this.http.get<any>(this.apiUrl+"/api/matches/byusers/"+userId1+"/"+userId2);
  }

  getAllNotificationByUserId(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/api/notification/filter-my-notification`);
  }

  maskNotification(notification:NotificationDTO): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/api/notification/mask-notification/${notification.id}`,{notification});
  }

  hasUnreadNotifications(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/api/notification/has-unread-notifications`);
  }

}
