import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatchService } from '../../services/match.service';
import { ProfileRequest } from '../../interfaces/profileRequest';
import { UserRequest } from '../../interfaces/userRequest';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable, forkJoin } from 'rxjs';

interface ProfileWithImageUrl extends ProfileRequest {
  imageUrl: SafeUrl;
   name: string;
  lastname: string;
}
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  @ViewChild('currentCard') currentCardElement!: ElementRef;
  @ViewChild('nextCard') nextCardElement!: ElementRef;


  showMatchNotification: boolean = false;
  selectedImageURL: SafeUrl | string | ArrayBuffer | null = null;
  //filterProfiles:ProfileRequest [] = [];
  filterProfilesWithImages: ProfileWithImageUrl[] = [];
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
  currentCard = 1;
  // Define una variable para el índice actual
  currentProfileIndex: number = 0;

  constructor(private userService: UserService,private sanitizer: DomSanitizer, private matchService: MatchService, private router: Router) { 
    
  }
// Método para cambiar al siguiente perfil
nextProfile() {
  this.currentProfileIndex = (this.currentProfileIndex + 1) % this.filterProfilesWithImages.length;
  this.nextProfileIndex(); // Llama al método para actualizar el índice del próximo perfil
}

// Método para obtener el índice del siguiente perfil
nextProfileIndex(): number {
  return (this.currentProfileIndex + 1) % this.filterProfilesWithImages.length;
}


like() {
  this.animateCard('like'); // Método para animación (si es necesario)
  this.onLike(this.filterProfilesWithImages[this.currentProfileIndex].id); // Realizar acción de like
  this.nextProfile(); // Cambiar al siguiente perfil y alternar cards
}

dislike() {
  this.animateCard('dislike'); // Método para animación (si es necesario)
  this.onDislike(this.filterProfilesWithImages[this.currentProfileIndex].id); // Realizar acción de dislike
  this.nextProfile(); // Cambiar al siguiente perfil y alternar cards
}

reload() {
  this.animateCard('reload'); // Método para animación (si es necesario)
  this.nextProfile(); // Cambiar al siguiente perfil y alternar cards
}
  

  animateCard(action: string) {
    const currentCard = document.getElementById(`card${this.currentCard}`);
    const nextCardId = this.currentCard === 1 ? 2 : 1;
    const nextCard = document.getElementById(`card${nextCardId}`);
  
    if (currentCard && nextCard) {
      switch(action) {
        case 'like':
          currentCard.classList.add('like-animation');
          break;
        case 'dislike':
          currentCard.classList.add('dislike-animation');
          break;
        case 'reload':
          currentCard.classList.add('reload-animation');
          break;
      }
  
      nextCard.classList.add('next-card', 'enter');
  
      setTimeout(() => {
        currentCard.classList.remove('like-animation', 'dislike-animation', 'reload-animation', 'enter');
        currentCard.classList.add('hidden-card');
        nextCard.classList.remove('hidden-card', 'next-card', 'enter');
        this.currentCard = nextCardId;
      }, 500);
    }
  }
  

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
    this.loadFilterProfiles();
  }

  loadRandomProfile() {
    this.userService.getRandomProfile().subscribe(
      (userProfile: ProfileRequest) => {
        this.randomProfile = userProfile;
        console.log("es: ",);
        if (this.randomProfile.photo) {
          // Se guarda la URL de la imagen y se la muestra en el perfil  
          this.selectedImageURL = this.getImageUrlOld(this.randomProfile.photo);
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

  loadFilterProfiles() {
    this.userService.getFilterByPreference().subscribe(
      (data: any[]) => {
        // Realiza solicitudes para obtener los datos de los usuarios
        const userRequests = data.map(profile => this.userService.getUser(profile.userId));
        
        // Usa forkJoin para esperar a que todas las solicitudes de usuarios se completen
        forkJoin(userRequests).subscribe(
          (users: UserRequest[]) => {
            // Combina los perfiles y los usuarios
            this.filterProfilesWithImages = data.map(profile => {
              const user = users.find(u => u.id === profile.userId);
              return {
                ...profile,
                photo: profile.photo ? new File([profile.photo], profile.photoFileName, { type: 'image/jpeg' }) : null,
                imageUrl: this.getImageUrlOld(profile.photo),
                name: user?.name || '',
                lastname: user?.lastname || ''
              };
            });
  
            // Asegúrate de que el índice no sea mayor que la longitud de los perfiles
            this.currentProfileIndex = Math.min(this.currentProfileIndex, this.filterProfilesWithImages.length - 1);
          },
          (error) => {
            console.error('Error loading users:', error);
          }
        );
      },
      (error) => {
        console.error('Error loading filtered profiles:', error);
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
    this.loadFilterProfiles();
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

  getImageUrlOld(imageData: File): SafeUrl {
    // Asegúrate de que los datos de la imagen estén en el formato correcto (base64)
    if (imageData && typeof imageData === 'string') {
      const imageUrl = 'data:image/jpeg;base64,' + imageData;
      return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
    } else {
      // Si los datos de la imagen no están en el formato correcto, devuelve una URL de imagen predeterminada o null
      return 'z';
    }
  }


  getImageUrl(imageData: File | null): SafeUrl {
    if (imageData instanceof File) {
      return this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(imageData));
    } else if (imageData && typeof imageData === 'string') {
      // Este caso es para manejar strings que puedan venir del backend
      return this.sanitizer.bypassSecurityTrustUrl(imageData);
    } else {
      return this.sanitizer.bypassSecurityTrustUrl('https://t3.ftcdn.net/jpg/05/70/71/06/360_F_570710660_Jana1ujcJyQTiT2rIzvfmyXzXamVcby8.jpg');
    }
  }
}
