import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent implements OnInit {
  selectedImageURL: SafeUrl | string | ArrayBuffer | null = null;
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
    minAge: 0
  };
  currentUser: UserRequest ={
    id:0,
    name:'',
    lastname:'',
    username:''
  };


  isLoggedIn: boolean = false;

  constructor(private userService:UserService, private formBuilder:FormBuilder,private http: HttpClient, private loginService:LoginService,private router:Router,private sanitizer: DomSanitizer ){
    
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
getImageUrl(imageData: ArrayBuffer): SafeUrl {
  // Asegúrate de que los datos de la imagen estén en el formato correcto (base64)
  if (imageData && typeof imageData === 'string') {
    const imageUrl = 'data:image/jpeg;base64,' + imageData;
    return this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  } else {
    // Si los datos de la imagen no están en el formato correcto, devuelve una URL de imagen predeterminada o null
    return this.defaultImageURL;
  }
}
// Función para cambiar la sección actual
changeSection(section: string): void {
    this.currentSection = section;
}
// Funcionar para cargar foto de perfil
onFileSelect(event: any) {
    console.log(event);
    const file = event.target.files[0]; // Obtiene el archivo seleccionado

    if (file) {
      const reader = new FileReader(); // Crea un FileReader para leer el archivo
      reader.onload = () => { // Se ejecuta cuando la lectura del archivo es completada
        // Actualizar el campo de la imagen con los datos binarios de la imagen
        this.selectedImageURL = reader.result as string;
        // Llama a la función para actualizar el perfil con la foto
        this.updateProfilePhoto(file);
  
      };
      reader.readAsDataURL(file); // Lee el archivo como una URL de datos
    }
}

// Función para actualizar el perfil con la foto
updateProfilePhoto(file: File){

  if (file && file.size > 0) {
    // Crea un objeto FormData
  const formData = new FormData();

  const profileDataJSON = JSON.stringify({
    id: this.currentUserProfile.id,
    userId: this.currentUserProfile.userId,
    photo: null,
    photoFileName: null
  });

  formData.append('req', profileDataJSON);
  formData.append('photoFile', file, file.name);

  console.log('Archivo file:',formData.get('photoFile'))
  console.log('Archivo data:',formData.get('req'))

  // Llama a la función del servicio para actualizar el perfil con la foto
  this.userService.updateUserProfilePhoto(formData).pipe(
    tap(response => {
      // Lógica adicional después de actualizar la foto
      alert("Actualizacion Photo");
      console.log("Actualización de la foto exitosa", response);
     }),
     catchError(error => {
      alert("error update photo");
      // Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
      return throwError(error);
    })
  ).subscribe();
  }    
}


uploadFile(event: any) {
  const file = event.target.files[0];
  const formData = new FormData();
  formData.append('photoFile', file,file.name);
  const profileDataJSON = JSON.stringify({
    id: this.currentUserProfile.id,
    userId: this.currentUserProfile.userId,
    photo: null,
    photoFileName: null
  });

  formData.append('req', profileDataJSON);
  this.http.put<any>('http://localhost:3000/api/user/updateProfilePhoto', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
  .subscribe(response => {
    // Handle successful response
    alert("Success");
  }, error => {
    // Handle error response
    alert("Error");
  });
}    

// Función para actualizar el perfil and user
updateUserAndProfile()
  {
    this.userService.updateUser(this.currentUser).pipe(
    tap(response => {
      // Lógica adicional después de actualizar el usuario
      alert("Actualizacion User");
    }),
    catchError(error => {
      alert("error update user");
      // Puedes manejar el error aquí, por ejemplo, mostrar un mensaje de error
      return throwError(error);
    })
  ).subscribe();

  this.userService.updateUserProfile(this.currentUserProfile).pipe(
    tap(response => {
      // Lógica adicional después de actualizar el perfil
      alert("Actualizacion Perfil");
    }),
    catchError(error => {
      alert("error update perfil");
      return throwError(error);
    })
   ).subscribe();
  }

  onLogout(): void {
    // Llamar al método logout() del servicio LoginService
    this.loginService.logoutUser();
    this.router.navigateByUrl('/user/login');
  }

 



}
