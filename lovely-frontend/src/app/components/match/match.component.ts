import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ChatService } from '../../services/chat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Observable, Subscription, forkJoin, map } from 'rxjs';
import { MatchService } from '../../services/match.service';
import { Match } from '../../models/match';
import { CommonModule } from '@angular/common';
import { ChatComponent } from '../chat/chat.component';
import { BreakpointObserver } from '@angular/cdk/layout';
import { UserResponse } from '../../interfaces/userResponse';

@Component({
  selector: 'app-match',
  standalone: true,
  imports: [CommonModule,ChatComponent],
  templateUrl: './match.component.html',
  styleUrl: './match.component.css'
})
export class MatchComponent implements OnInit, OnDestroy {

  matches: Match[] = [];// Variable para almacenar los matches
  users: UserResponse[] = []; 

  currentProfile!: UserResponse; 
  targetProfile!: UserResponse;

  selectedUserId: number | null = null;
  chatKey: number = 0; // Añade esta línea
  
  isMobile: boolean = false;
  showUserList: boolean = true;

  private userSubscription: Subscription | undefined;
  private messageSubscription: Subscription | undefined;
  
  constructor(private breakpointObserver: BreakpointObserver, private userService:UserService,private matchService:MatchService, private route: ActivatedRoute, private router: Router, private chatService: ChatService){
    this.currentProfile = {
      id: 0,
      username: '',
      lastname: '',
      name: '',
      preference: {
        id: 0,
        maxAge: 0,
        minAge: 0,
        likeGender: '',
        location: '',
        distance: 0,
        interests: []
      },
      profileDetail: {
        id: 0,
        phone: '',
        gender: '',
        birthDate: new Date(),
        description: '',
        work: '',
        photo: null,
        photoFileName: '',
        timestamp: ''
      }
    };
    this.targetProfile = {
      id: 0,
      username: '',
      lastname: '',
      name: '',
      preference: {
        id: 0,
        maxAge: 0,
        minAge: 0,
        likeGender: '',
        location: '',
        distance: 0,
        interests: []
      },
      profileDetail: {
        id: 0,
        phone: '',
        gender: '',
        birthDate: new Date(),
        description: '',
        work: '',
        photo: null,
        photoFileName: '',
        timestamp: ''
      }
    };
  }

  checkScreenSize() {
    this.breakpointObserver.observe(['(max-width: 600px)','(max-width: 767px)'])
      .subscribe(result => {
        this.isMobile = result.matches;
        this.showUserList = true;
      });
  }

  goBack() {
    if (this.isMobile) {
      this.showUserList = true;
      this.selectedUserId = null;
    }
  }

  ngOnInit(): void {
    this.checkScreenSize();
    this.userService.getCurrentProfile().subscribe(
      (user: UserResponse) => {
        this.currentProfile = user;
        this.getAllMatch();
        this.getUsersByMatch();
      },
      (error) => {
        console.error('Error al obtener el usuario actual:', error);
      }
    );
  }
  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }
  }
  getAllMatch(){
    this.matchService.getMyMatches(this.currentProfile.id).subscribe(
      (data: Match[]) => {
        this.matches = data; // Almacena los matches en la variable
        console.log("datos:"+ data)
      },
      (error) => {
        console.error('Error', error);
      }
    );
  }
  getUsersByMatch(){
    this.userService.getUsers(this.currentProfile.id).subscribe(
      (data: UserResponse[]) => {
        this.users = data; 
        console.log("datos:"+ data)
      },
      (error) => {
        console.error('Error', error);
      }
    );
  }
  goToChat(userId: number): void {
    this.selectedUserId = userId;
    if (this.isMobile) {
      this.showUserList = false;
    }
      // Obtener el perfil del usuario seleccionado y del usuario logueado
      console.log('Going to chat with user:', userId);
      forkJoin({
        currentProfile: this.userService.getUser(this.currentProfile.id),
        targetProfile: this.userService.getUser(userId)
      }).subscribe(
        ({ currentProfile, targetProfile }) => {
          this.currentProfile = currentProfile;
          this.targetProfile = targetProfile;
          
          console.log("User Logueado:", this.currentProfile.id);
          console.log("User Match:", this.targetProfile.id);
          // Actualizar el usuario seleccionado
          this.selectedUserId = userId;
          this.chatKey = Date.now();// Usar Date.now() para asegurar un valor único cada vez
          console.log('Chat key updated:', this.chatKey);
          // Navegar a la URL del chat con los IDs de usuario
          this.router.navigate(['/match', this.currentProfile.id, this.targetProfile.id]);

          // Navegar al chat una vez que se hayan obtenido ambos perfiles
          /*if (this.prueba1 && this.prueba2) {
            this.router.navigate(['/chat/', this.prueba1.userId, this.prueba2.userId]);
          } else {
            console.error('Error al obtener los perfiles de usuario');
          }*/
        },
        (error) => {
          console.error('Error al obtener los perfiles de usuario:', error);
        }
      );
    // Generar y usar enmascaramiento para los IDs aquí
    //const maskedUserId = this.generateMaskedId(this.prueba1.userId);
    //const maskedTargetUserId = this.generateMaskedId(this.prueba2.userId);
  }

  generateMaskedId(id: number): string {
    // Implementa tu lógica de enmascaramiento aquí
    // Por ejemplo, podrías usar hashing, encoding, etc.
    return btoa(String(id)); // Codifica el ID en base64 como ejemplo
  }
}

