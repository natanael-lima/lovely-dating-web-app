import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormBuilder, FormsModule } from '@angular/forms';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { UserRequest } from '../../interfaces/userRequest';
import { ProfileRequest } from '../../interfaces/profileRequest';
import { catchError, forkJoin, tap, throwError } from 'rxjs';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
//import * as bootstrap from 'bootstrap';
declare var window: any;

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  emailTest: string = 'mandy@com';
  isMobile: boolean = false;
  showMenu: boolean = true;
  countries : any [] = [];
  selectedImageURL: SafeUrl | string | ArrayBuffer | null = null;
  uploadSuccess: boolean = false;
  isLoggedIn: boolean = false;
  defaultImageURL: string = 'https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg';
  currentSection: string = 'perfil'; // Sección actual, inicialmente 'perfil'
  currentUserProfile: ProfileRequest={
    id:0,
    userId:0,
    photo:null,
    photoFileName:'',
    location: '',
    gender: '',
    age: 0,
    likeGender: '',
    maxAge: 0,
    minAge: 0,
    timestamp:new Date()
  };
  currentUser: UserRequest ={
    id:0,
    name:'',
    lastname:'',
    username:''
  };

  
  photos: string[] = Array(4).fill(null); //Photo edit profile
  distance: number = 50; // Valor inicial de la barra de rango

  //Selecciona multiple imagenes
  onFileSelected(event: any, index: number): void {
    if (event.target.files && event.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.photos[index] = e.target.result;
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  }
  updateDistance(event: Event) {
    this.distance = +(event.target as HTMLInputElement).value;
  }
  constructor(private userService:UserService, private formBuilder:FormBuilder,private http: HttpClient, private loginService:LoginService,private router:Router,private sanitizer: DomSanitizer ){
    this.checkScreenSize();
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isMobile = window.innerWidth < 768;
    this.showMenu = true;
  }
  // Función para cambiar la sección actual
  changeSection(section: string) {
    this.currentSection = section;
    if (this.isMobile) {
      this.showMenu = false;
    }
  }

  goBack() {
    if (this.isMobile) {
      this.showMenu = true;
    }
  }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(
      (user: UserRequest) => {
        this.currentUser = user;
        // Una vez que se haya obtenido el usuario actual, llamamos a getCurrentUserProfile
        this.userService.getCurrentUserProfile(this.currentUser.id).subscribe(
          (userProfilee: ProfileRequest) => {
            // Asigna los datos del perfil actual a currentUserProfile
              this.currentUserProfile = userProfilee;
              this.getAllCountries();
              // Verifica si hay una foto de perfil en el perfil actual
              if (this.currentUserProfile.photo) {
                // Se guarda la URL de la imagen y se la muestra en el perfil  
                this.selectedImageURL = this.getImageUrl(this.currentUserProfile.photo);
              } else {
                // Si no hay foto de perfil, usa la URL de la imagen por defecto
                this.selectedImageURL = this.defaultImageURL;
              }
          },
          (error) => {
            console.error('Error al obtener el profile actual:', error);
          }
        );
      },
      (error) => {
        console.error('Error al obtener el usuario actual:', error);
      }
    );
  }

// Función para obtener la URL segura de la imagen
getImageUrl(imageData: File): SafeUrl {
  // Asegúrate de que los datos de la imagen estén en el formato correcto (base64)
  if (imageData && typeof imageData === 'string') {
    const imageUrl = 'data:image/jpeg;base64,' + imageData;
    return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  } else {
    // Si los datos de la imagen no están en el formato correcto, devuelve una URL de imagen predeterminada o null
    return this.defaultImageURL;
  }
}


// Funcionar para cargar foto de perfil
onFileSelect(event: any) {
    console.log(event);
    const file = event.target.files[0]; // Obtiene el archivo seleccionado

    if (file) {
      this.currentUserProfile.photo = file;
      this.currentUserProfile.photoFileName = file.name;
      const reader = new FileReader(); // Crea un FileReader para leer el archivo
      reader.onload = () => { // Se ejecuta cuando la lectura del archivo es completada
        // Actualizar el campo de la imagen con los datos binarios de la imagen
        this.selectedImageURL = reader.result as string;
      };
      reader.readAsDataURL(file); // Lee el archivo como una URL de datos
    }
}
showSuccessToast() {
  // Luego puedes usar directamente las funciones de Bootstrap
const toastElement = document.getElementById('profileUpdateToast');
if (toastElement) {
  const toast = new window.bootstrap.Toast(toastElement, {
    autohide: true,
    delay: 5000
  });
  toast.show();
}
}
updatePhoto() {
  if (this.currentUserProfile.photo instanceof File) {
    const formData = new FormData();

    formData.append('req', new Blob([JSON.stringify({
      id: this.currentUserProfile.id,
      userId: this.currentUserProfile.userId,
      location: this.currentUserProfile.location,
      gender: this.currentUserProfile.gender,
      age: (this.currentUserProfile.age),  // Asegurarse de que age sea un número
      likeGender: this.currentUserProfile.likeGender,
      maxAge: this.currentUserProfile.maxAge,
      minAge: this.currentUserProfile.minAge
    })], {
      type: 'application/json'
    }));
    // Verificar si hay una nueva imagen seleccionada para el user
    if (this.currentUserProfile.photo instanceof File) {
      // Agregar la nueva imagen al FormData
      console.log("update photo");
      formData.append('photoFile', this.currentUserProfile.photo, this.currentUserProfile.photoFileName);
      formData.append('keepCurrentImage', 'false');
  } 

    this.userService.updatePhotoAndProfile(formData).pipe(
      tap(response => {
        console.log("Foto actualizada:", response);
        //this.uploadSuccess = true;
        this.showSuccessToast();
      }),
      catchError(error => {
        console.log("Error update photo");
        return throwError(() => error);
      })
    ).subscribe();
  }
}


uploadFileAndProfile() {
  const formData = new FormData();
  // Convertir el objeto JSON a una cadena JSON y agregarlo al FormData
  formData.append('req', new Blob([JSON.stringify({
    id: this.currentUserProfile.id,
    userId: this.currentUserProfile.userId,
    location: this.currentUserProfile.location,
    gender: this.currentUserProfile.gender,
    age: (this.currentUserProfile.age),  // Asegurarse de que age sea un número
    likeGender: this.currentUserProfile.likeGender,
    maxAge: this.currentUserProfile.maxAge,
    minAge: this.currentUserProfile.minAge
  })], {
    type: 'application/json'
  }));
  // Agregar un parámetro indicando que se debe mantener la imagen actual
  formData.append('keepCurrentImage', 'true');


  console.log('Archivo:', formData.get('photoFile'));
  console.log('Objeto:',formData.get('req'))
  
  this.userService.updatePhotoAndProfile(formData).pipe(
    tap(response => {
      // Lógica adicional después de actualizar el perfil
      console.log("Respuesta del servidor userProfile:", response);
      if (response) {
        this.uploadSuccess = true;
          setTimeout(() => {
            this.uploadSuccess = false;
          }, 2000); // Cerrar el modal después de 2 segundos
        console.log("Se actualizo correcto userProfile:", response.message);
      } else {
        console.error('Error al actualizar la userProfile:', response.message);
      }

    }),
    catchError(error => {
      console.error('Error al actualizar la userProfile:', error.message);
      return throwError(error);
    })
   ).subscribe();
   this.userService.updateUser(this.currentUser).pipe(
    tap(response => {
      // Lógica adicional después de actualizar el usuario
      console.log("Respuesta del servidor user:", response);
      if (response) {
        console.log("Se actualizo correcto user:", response.message);
        this.uploadSuccess = true;
          setTimeout(() => {
            this.uploadSuccess = false;
          }, 2000); // Cerrar el modal después de 2 segundos
      } else {
        console.error('Error al actualizar la user:', response.message);
      }
    }),
    catchError(error => {
      console.error('Error al actualizar la user:', error.message);
      // Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
      return throwError(error);
    })
  ).subscribe();
}    

  onLogout(): void {
    // Llamar al método logout() del servicio LoginService
    this.loginService.logoutUser();
    this.router.navigateByUrl('/user/signin');
}

  getAllCountries(){
    this.userService.getCountries().subscribe(
      (data: any[]) => {
        this.countries = data; // Almacena los paises en la variable
        console.log("datos:"+ data)
      },
      (error) => {
        console.error('Error', error);
      }
    );
  }

  getFlagEmoji(countryCode: string): string {
    const codePoints = countryCode
      .toUpperCase()
      .split('')
      .map(char => 127397 + char.charCodeAt(0));
    return String.fromCodePoint(...codePoints);
  }

}
