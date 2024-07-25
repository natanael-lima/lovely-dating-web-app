import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../services/user.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatchService } from '../../services/match.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Observable, forkJoin } from 'rxjs';
import { UserResponse } from '../../interfaces/userResponse';

enum ProfileState {
  Loading,
  Loaded,
  Error,
  NoProfiles
}

@Component({
  selector: 'app-explore',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './explore.component.html',
  styleUrl: './explore.component.css'
})
export class ExploreComponent implements OnInit {
  @ViewChild('currentCard') currentCardElement!: ElementRef;
  @ViewChild('nextCard') nextCardElement!: ElementRef;

  showMatchNotification: boolean = false;
  profileState: ProfileState = ProfileState.Loading;
  ProfileState = ProfileState; // Añadir esta línea para poder usar ProfileState en la plantilla
  currentCard = 1;

  selectedImageURL: SafeUrl | string | ArrayBuffer | null = null;
  
  filterProfiles: UserResponse[] = [];
  currentProfile!: UserResponse; 

  // Define una variable para el índice actual
  currentProfileIndex: number = 0;
  

  constructor(private userService: UserService,private sanitizer: DomSanitizer, private matchService: MatchService, private router: Router) { 
    this.currentProfile = {
      id: 0,
      username: '',
      lastname: '',
      name: '',
      role: '',
      state: '',
      isVisible:false,
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
  ngOnInit() {
    // Aquí puedes inicializar tu componente, por ejemplo, obteniendo el perfil del usuario actual
    this.userService.getCurrentProfile().subscribe(
      user => {
        this.currentProfile = user;
      },
      error => {
        console.error('Error al obtener el usuario actual:', error);
      }
    );
    this.loadFilterProfiles();
  }


   loadFilterProfiles() {
    this.profileState = ProfileState.Loading;
    this.userService.getFilterByPreference().subscribe(
      (data: UserResponse[]) => {
        this.filterProfiles = data;
        this.currentProfileIndex = 0;
        this.profileState = data.length > 0 ? ProfileState.Loaded : ProfileState.NoProfiles;
      },
      (error) => {
        console.error('Error loading filtered profiles:', error);
        this.profileState = ProfileState.Error;
      }
    );
  }

  getImageUrl(file: File | null): SafeUrl {
    if (file) {
      const url = URL.createObjectURL(file);
      return this.sanitizer.bypassSecurityTrustUrl(url);
    } else {
      return this.sanitizer.bypassSecurityTrustUrl('https://i.postimg.cc/7hsdHJL7/nofound2.png'); // URL de imagen por defecto
    }
  }
  getImageUrlOld(imageData: File | null): SafeUrl {
    // Asegúrate de que los datos de la imagen estén en el formato correcto (base64)
    if (imageData && typeof imageData === 'string') {
      const imageUrl = 'data:image/jpeg;base64,' + imageData;
      return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
    } else {
      // Si los datos de la imagen no están en el formato correcto, devuelve una URL de imagen predeterminada o null
      return 'https://i.postimg.cc/7hsdHJL7/nofound2.png';
    }
  }

  // Método para cambiar al siguiente perfil
  nextProfile() {
    this.currentProfileIndex = (this.currentProfileIndex + 1) % this.filterProfiles.length;
    this.nextProfileIndex(); // Llama al método para actualizar el índice del próximo perfil
  }

  // Método para obtener el índice del siguiente perfil
  nextProfileIndex(): number {
    return (this.currentProfileIndex + 1) % this.filterProfiles.length;
  }


  like() {
    this.animateCard('reload'); // Método para animación (si es necesario)
    this.onLike(this.filterProfiles[this.currentProfileIndex].id); // Realizar acción de like
    this.nextProfile(); // Cambiar al siguiente perfil y alternar cards
  }

  dislike() {
    this.animateCard('reload'); // Método para animación (si es necesario)
    this.onDislike(this.filterProfiles[this.currentProfileIndex].id); // Realizar acción de dislike
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
      switch (action) {
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


  onLike(targetId: number): void {
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

  onDislike(targetId: number): void {
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

    this.matchService.checkForMatch(this.currentProfile.id, targetId).subscribe(
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
    this.router.navigate(['/search']);
    this.loadFilterProfiles();
  }

  calculateAge(birthDate: Date): number {
    const today = new Date();
    const birthDateObj = new Date(birthDate);
    let age = today.getFullYear() - birthDateObj.getFullYear();
    const monthDiff = today.getMonth() - birthDateObj.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDateObj.getDate())) {
      age--;
    }
    return age;
  }

}
