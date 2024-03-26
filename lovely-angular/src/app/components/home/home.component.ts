import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatchService } from '../../services/match.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  
  currentUser!: User;


 



  constructor(private userService: UserService,private sanitizer: DomSanitizer, private matchService: MatchService) { }

  ngOnInit() {
    this.loadRandomProfile();
  }

  loadRandomProfile() {
    this.userService.getRandomProfile().subscribe(
      (user: User) => {
        this.currentUser = user;
       
      },
      (error) => {
        console.error('Error loading random profile:', error);
      }
    );
  }
   likeUser(likerId: number, targetId: number): void {
    this.matchService.likeUser(likerId, targetId).subscribe(
      response => console.log('Liked user:', targetId),
      error => console.error('Error liking user:', error)
    );
  }

  dislikeUser(likerId: number, targetId: number): void {
    this.matchService.dislikeUser(likerId, targetId).subscribe(
      response => console.log('Disliked user:', targetId),
      error => console.error('Error disliking user:', error)
    );
  }
  onLike() {
    // Aquí puedes agregar la lógica para manejar el like
    this.loadRandomProfile();
  }

  onReject() {
    // Aquí puedes agregar la lógica para manejar el rechazo
    this.loadRandomProfile();
  }
  getImageUrl(imageData: string | File): SafeUrl {
    if (!imageData) {
      return 'assets/default-profile-image.jpg';
    }
  
    if (typeof imageData === 'string') {
      // Si es una cadena de texto, simplemente devuelve la URL segura
      return this.sanitizer.bypassSecurityTrustUrl(imageData);
    } else {
      // Si es un archivo, crea una URL para el objeto File
      const imageUrl = URL.createObjectURL(imageData);
      return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
    }
  }

  getImageUrl2(imageData: File): SafeUrl {
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
