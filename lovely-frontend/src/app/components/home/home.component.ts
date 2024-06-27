import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatchService } from '../../services/match.service';
import { ProfileRequest } from '../../interfaces/profileRequest';
import { UserRequest } from '../../interfaces/userRequest';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  selectedImageURL: SafeUrl | string | ArrayBuffer | null = null;
  showMatchNotification: boolean = false;
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
  constructor(private userService: UserService,private sanitizer: DomSanitizer, private matchService: MatchService, private router: Router) { }

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
        console.log("es: ",);
        if (this.randomProfile.photo) {
          // Se guarda la URL de la imagen y se la muestra en el perfil  
          this.selectedImageURL = this.getImageUrl(this.randomProfile.photo);
        } else {
          // Si no hay foto de perfil, usa la URL de la imagen por defecto
          this.selectedImageURL = 'https://t3.ftcdn.net/jpg/05/70/71/06/360_F_570710660_Jana1ujcJyQTiT2rIzvfmyXzXamVcby8.jpg';
        }
       
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
        console.log(this.checkForMatch(targetId));
        this.checkForMatch(targetId); // Verificar si hay un match después de dar like
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

  checkForMatch(targetId: number) {

    this.matchService.checkForMatch(this.userProfileCurrent.id, targetId).subscribe(
      response => {
       
        if (response) {
          this.showMatchNotification = true;
          console.log("match exitoso");
        }
       
      },
      error => {
        console.error('Error al verificar match:', error);
      }
    );
  }

closeMatchNotification() {
    this.showMatchNotification = false;
    this.router.navigate(['/home']);
    this.loadRandomProfile();
  }

// Función para obtener la URL segura de la imagen
getImageUrl(imageData: File): SafeUrl {
  // Asegúrate de que los datos de la imagen estén en el formato correcto (base64)
  if (imageData && typeof imageData === 'string') {
    const imageUrl = 'data:image/jpeg;base64,' + imageData;
    return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  } else {
    // Si los datos de la imagen no están en el formato correcto, devuelve una URL de imagen predeterminada o null
    return 'z';
  }
}
}
