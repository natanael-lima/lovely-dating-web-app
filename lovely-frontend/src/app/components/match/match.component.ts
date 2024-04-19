import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserRequest } from '../../interfaces/userRequest';
import { ChatService } from '../../services/chat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Subscription, forkJoin } from 'rxjs';
import { MatchService } from '../../services/match.service';
import { Match } from '../../models/match';
import { CommonModule } from '@angular/common';
import { ProfileRequest } from '../../interfaces/profileRequest';

@Component({
  selector: 'app-match',
  standalone: true,
  imports: [CommonModule],
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

  prueba1!: ProfileRequest;
  prueba2!: ProfileRequest;

  private userSubscription: Subscription | undefined;
  constructor(private userService:UserService,private matchService:MatchService, private route: ActivatedRoute, private router: Router){

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
      forkJoin({
        currentUserProfile: this.userService.getCurrentUserProfile(this.currentUser.id),
        targetUserProfile: this.userService.getCurrentUserProfile(userId)
      }).subscribe(
        ({ currentUserProfile, targetUserProfile }) => {
          this.prueba1 = currentUserProfile;
          this.prueba2 = targetUserProfile;
          
          console.log("User Logueado:", this.prueba1.userId);
          console.log("User Match:", this.prueba2.userId);
          
          // Navegar al chat una vez que se hayan obtenido ambos perfiles
          if (this.prueba1 && this.prueba2) {
            this.router.navigate(['/chat/', this.prueba1.userId, this.prueba2.userId]);
          } else {
            console.error('Error al obtener los perfiles de usuario');
          }
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

