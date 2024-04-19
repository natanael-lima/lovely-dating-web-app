import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatchService } from '../../services/match.service';
import { ProfileRequest } from '../../interfaces/profileRequest';
import { UserRequest } from '../../interfaces/userRequest';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  
  randomProfile: ProfileRequest={
    id:0,
    userId:0,
    photo:null,
    photoFileName:'',
    location: '',
    gender: '',
    age: 0,
    likeGender: '',
    maxAge: 0,
    minAge: 0
  };
  randomUser: UserRequest ={
    id:0,
    name:'',
    lastname:'',
    username:''
  };
  userProfileCurrent!:ProfileRequest;
  userCurrent!:UserRequest;
  constructor(private userService: UserService,private sanitizer: DomSanitizer, private matchService: MatchService) { }

  ngOnInit() {
    // Aquí puedes inicializar tu componente, por ejemplo, obteniendo el perfil del usuario actual
    // Suponiendo que tienes un método para obtener el perfil del usuario actual en tu servicio de Match
    this.userService.getCurrentUser().subscribe(
      user => {
        this.userCurrent = user;
        this.userService.getCurrentUserProfile(this.userCurrent.id).subscribe(
          userProfile => {
            this.userProfileCurrent = userProfile;
          },
          error => {
            console.error('Error al obtener el perfil del usuario:', error);
          }
        );
      },
      error => {
        console.error('Error al obtener el usuario actual:', error);
      }
    );
    this.loadRandomProfile();
  }

  loadRandomProfile() {
    this.userService.getRandomProfile().subscribe(
      (userProfile: ProfileRequest) => {
        this.randomProfile = userProfile;
        // Después de recibir el perfil aleatorio, obtén el usuario correspondiente
        this.userService.getUser(this.randomProfile.userId).subscribe(
          (user: UserRequest) => {
            this.randomUser = user;
          },
          (error) => {
            console.error('Error loading random user:', error);
          }
        );
      },
      (error) => {
        console.error('Error loading random profile:', error);
      }
    );

  }

  onLike( targetId: number): void {
    this.matchService.likeUser(targetId).subscribe(
      response => {
        console.log(response.message); // Imprime el mensaje de éxito
        // Actualiza cualquier estado necesario en tu componente
      },
      error => {
        console.error('Error al dar like:', error);
        // Maneja el error como lo consideres necesario
      }
    );
  }

  onDislike( targetId: number): void {
    this.matchService.dislikeUser(targetId).subscribe(
      response => {
        console.log(response.message); // Imprime el mensaje de éxito
        // Actualiza cualquier estado necesario en tu componente
      },
      error => {
        console.error('Error al dar like:', error);
        // Maneja el error como lo consideres necesario
      }
    );
  }

  onReject() {
    // Aquí puedes agregar la lógica para manejar el rechazo
    this.loadRandomProfile();
  }

  getImageUrl2(imageData: File): SafeUrl | string{
    // Verifica si hay datos de imagen
    if (imageData) {
      // Convierte los datos de imagen en una URL segura
      const imageUrl = 'data:image/jpeg;base64,' + imageData;
      return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
    } else {
      // Si no hay datos de imagen, puedes proporcionar una URL de imagen por defecto
      return 'assets/default-profile-image.jpg';
    }
  }
}
