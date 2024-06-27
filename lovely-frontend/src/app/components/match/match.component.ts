import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { UserRequest } from '../../interfaces/userRequest';
import { ChatService } from '../../services/chat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Subscription, forkJoin } from 'rxjs';
import { MatchService } from '../../services/match.service';
import { Match } from '../../models/match';
import { CommonModule } from '@angular/common';
import { ProfileRequest } from '../../interfaces/profileRequest';
import { ChatComponent } from '../chat/chat.component';
import { Message } from '../../models/message';


@Component({
  selector: 'app-match',
  standalone: true,
  imports: [CommonModule,ChatComponent],
  templateUrl: './match.component.html',
  styleUrl: './match.component.css'
})
export class MatchComponent implements OnInit, OnDestroy {
  currentUser: UserRequest ={
    id:0,
    name:'',
    lastname:'',
    username:''
  };
  matches: Match[] = [];// Variable para almacenar los matches
  users: UserRequest[] = []; 

  currentUserProfile!: ProfileRequest;
  targetUserProfile!: ProfileRequest;

  selectedUserId: number | null = null;
  chatKey: number = 0; // Añade esta línea

  private userSubscription: Subscription | undefined;
  private messageSubscription: Subscription | undefined;
  
  constructor(private userService:UserService,private matchService:MatchService, private route: ActivatedRoute, private router: Router, private chatService: ChatService){

  }
  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(
      (user: UserRequest) => {
        this.currentUser = user;
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
    this.matchService.getMyMatches(this.currentUser.id).subscribe(
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
    this.userService.getUsers(this.currentUser.id).subscribe(
      (data: UserRequest[]) => {
        this.users = data; 
        console.log("datos:"+ data)
      },
      (error) => {
        console.error('Error', error);
      }
    );
  }
  goToChat(userId: number): void {
      // Obtener el perfil del usuario seleccionado y del usuario logueado
      console.log('Going to chat with user:', userId);
      forkJoin({
        currentUserProfile: this.userService.getCurrentUserProfile(this.currentUser.id),
        targetUserProfile: this.userService.getCurrentUserProfile(userId)
      }).subscribe(
        ({ currentUserProfile, targetUserProfile }) => {
          this.currentUserProfile = currentUserProfile;
          this.targetUserProfile = targetUserProfile;
          
          console.log("User Logueado:", this.currentUserProfile.userId);
          console.log("User Match:", this.targetUserProfile.userId);
          // Actualizar el usuario seleccionado
          this.selectedUserId = userId;
          this.chatKey = Date.now();// Usar Date.now() para asegurar un valor único cada vez
          console.log('Chat key updated:', this.chatKey);
          // Navegar a la URL del chat con los IDs de usuario
          this.router.navigate(['/match', this.currentUserProfile.userId, this.targetUserProfile.userId]);

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

