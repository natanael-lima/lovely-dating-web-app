import { Component, HostListener, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormBuilder, FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../services/login.service';
import { Router } from '@angular/router';
import { UserRequest } from '../../interfaces/userRequest';
import { catchError, forkJoin, tap, throwError } from 'rxjs';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { UserDTO } from '../../interfaces/userDTO';
import { PreferenceDTO } from '../../interfaces/preferenceDTO';
import { PasswordRequest } from '../../interfaces/passwordRequest';
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


  currentProfile!: UserDTO; //new
  

  photos: string[] = Array(4).fill(null); //Photo edit profile

  interestsList: string[] = [
    'Música', 'Viajes', 'Deportes', 'Arte', 'Fotografía',
    'Literatura', 'Ciencia', 'Moda', 'Historia', 'Cine'
  ];
  
  checkedInterests: { [key: string]: boolean } = {};
  selectedInterests: string[] = [];
  maxInterests: number = 4;

// Propiedades para el formulario de edición password
  password: PasswordRequest ={
    currentPassword:'',
    newPassword:''
  };
  oldPassword!: String;
  newPassword!: String;
  newPasswordRepeat!: String;
  constructor(private userService:UserService, private formBuilder:FormBuilder,private http: HttpClient, private loginService:LoginService,private router:Router,private sanitizer: DomSanitizer ){
    this.currentProfile = {
      id: 0,
      username: '',
      password: '',
      lastname: '',
      name: '',
      role: '',
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
  // Inicializa checkedInterests al cargar los datos del perfil
  initializeInterests() {
    const currentInterests = this.currentProfile.preference?.interests || [];
    console.log('Intereses actuales:', currentInterests);
  
    this.interestsList.forEach(interest => {
      this.checkedInterests[interest] = currentInterests.includes(interest);
      console.log(`${interest}: ${this.checkedInterests[interest]}`);
    });
  }
  // Manejar cambios en los checkboxes
  onCheckboxChange(event: Event, interest: string) {
    const target = event.target as HTMLInputElement;
    const currentlySelected = Object.values(this.checkedInterests).filter(Boolean).length;

    if (target.checked && currentlySelected >= this.maxInterests) {
      // Si ya hay 4 seleccionados y se intenta marcar otro, prevenimos la acción
      event.preventDefault();
      target.checked = false;
      return;
    }

    this.checkedInterests[interest] = target.checked;
  }
  isCheckboxDisabled(): boolean {
    const selectedCount = Object.values(this.checkedInterests).filter(Boolean).length;
    return selectedCount >= this.maxInterests;
  }
  // Obtener los intereses seleccionados
  getSelectedInterests(): string[] {
    return Object.keys(this.checkedInterests).filter(interest => this.checkedInterests[interest]);
  }

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
    this.currentProfile.preference.distance = +(event.target as HTMLInputElement).value;
  }
  


  ngOnInit(): void {
    this.userService.getCurrentProfile().subscribe(
      (user: UserDTO) => {
        this.currentProfile = user;
              this.getAllCountries();
              this.initializeInterests();
              // Verifica si hay una foto de perfil en el perfil actual
              if (this.currentProfile.profileDetail.photo) {
                // Se guarda la URL de la imagen y se la muestra en el perfil  
                this.selectedImageURL = this.getImageUrl(this.currentProfile.profileDetail.photo);
              } else {
                // Si no hay foto de perfil, usa la URL de la imagen por defecto
                this.selectedImageURL = this.defaultImageURL;
              }
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
      this.currentProfile.profileDetail.photo = file;
      this.currentProfile.profileDetail.photoFileName = file.name;
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
updateInterests() {
  const selectedInterests = Object.keys(this.checkedInterests)
    .filter(interest => this.checkedInterests[interest]);
  
  if (this.currentProfile.preference) {
    this.currentProfile.preference.interests = selectedInterests;
  }
  
  // Aquí puedes llamar a tu método para guardar en la BD
  // this.saveToDatabase();
}
updatePreference() {
  if (this.currentProfile.preference) {
    const selectedInterests = this.getSelectedInterests();
    this.currentProfile.preference.interests = selectedInterests;

    const dataProfileUpdate: PreferenceDTO = {
      id: this.currentProfile.preference.id, // Asegúrate de tener el ID de la preferencia
      maxAge: this.currentProfile.preference.maxAge,
      minAge: this.currentProfile.preference.minAge,
      likeGender: this.currentProfile.preference.likeGender,
      location: this.currentProfile.preference.location,
      distance: this.currentProfile.preference.distance,
      interests: this.currentProfile.preference.interests
    };

    this.userService.updateUserPreference(dataProfileUpdate).pipe(
      tap(response => {
        console.log("Preferece update succesfull:", response);
        this.uploadSuccess = true;
        setTimeout(() => {
          this.uploadSuccess = false;
        }, 2000); // Cerrar el modal después de 2 segundos
        //this.showSuccessToast();
      }),
      catchError(error => {
        console.log("Error update preference");
        return throwError(() => error);
      })
    ).subscribe();
  }
}

updatePhoto() {
  if (this.currentProfile.profileDetail.photo instanceof File) {
    const formData = new FormData();
    formData.append('req', new Blob([JSON.stringify({
      id: this.currentProfile.profileDetail.id,
      phone: this.currentProfile.profileDetail.phone,
      gender: this.currentProfile.profileDetail.gender,
      birthDate: this.currentProfile.profileDetail.birthDate,
      description: this.currentProfile.profileDetail.description,
      work: this.currentProfile.profileDetail.work,
    })], {
      type: 'application/json'
    }));

    // Verificar si hay una nueva imagen seleccionada para el usuario
    if (this.currentProfile.profileDetail.photo instanceof File) {
      // Agregar la nueva imagen al FormData
      formData.append('file', this.currentProfile.profileDetail.photo, this.currentProfile.profileDetail.photoFileName);
      formData.append('keepCurrentImage', 'false');
      console.log("update only photo");
    }

    this.userService.updateUserDetail(formData,this.currentProfile.id).pipe(
      tap(response => {
        console.log("Photo update successfull:", response);
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


updateProfileDetailAndBasic() {
    const formData = new FormData();
    formData.append('req', new Blob([JSON.stringify({
      id: this.currentProfile.profileDetail.id,
      phone: this.currentProfile.profileDetail.phone,
      gender: this.currentProfile.profileDetail.gender,
      birthDate: this.currentProfile.profileDetail.birthDate,
      description: this.currentProfile.profileDetail.description,
      work: this.currentProfile.profileDetail.work,
    })], {
      type: 'application/json'
    }));
    formData.append('keepCurrentImage', 'true');

  console.log('Archivo:',formData.get('file'))
  console.log('Objeto:',formData.get('req'))
  console.log('id: ',this.currentProfile.profileDetail.id)

  this.userService.updateUserDetail(formData,this.currentProfile.id).pipe(
    tap(response => {
      // Lógica adicional después de actualizar el perfil
      console.log("Respuesta del servidor profileDetail:", response);
      if (response) {
        this.uploadSuccess = true;
          setTimeout(() => {
            this.uploadSuccess = false;
          }, 2000); // Cerrar el modal después de 2 segundos
        console.log("Se actualizo correcto profileDetail:", response.message);
      } else {
        console.error('Error al actualizar la profileDetail:', response.message);
      }

    }),
    catchError(error => {
      console.error('Error al actualizar la profileDetail:', error.message);
      return throwError(error);
    })
   ).subscribe();

   const dataUserUpdate: UserRequest = {
     id: this.currentProfile.id, // Asegúrate de tener el ID de la preferencia
     name: this.currentProfile.name,
     lastname: this.currentProfile.lastname,
     username: this.currentProfile.username
   };
   this.userService.updateUserBasic(dataUserUpdate).pipe(
    tap(response => {
      console.log("UserBasic update successfull:", response);
      //this.uploadSuccess = true;
      this.showSuccessToast();
    }),
    catchError(error => {
      console.log("Error update UserBasic");
      return throwError(() => error);
    })
  ).subscribe();
}

changePassword() {
  console.log('Entré a cambiar la contraseña');

  if (!this.currentProfile.id) {
    console.log('No hay usuario para cambiar la contraseña');
    return;
  }

  if (this.newPassword !== this.newPasswordRepeat) {
    console.log('Las contraseñas no coinciden');
    return;
  }
  console.log('Las contraseñas coinciden');
  this.password.newPassword = this.newPassword.toString();
  this.password.currentPassword = this.oldPassword.toString();

  this.userService.updatePassword(this.currentProfile.id,this.password)
    .subscribe(response => {
      console.log('Password actualizado:', response);
      this.uploadSuccess = true;
          setTimeout(() => {
            this.uploadSuccess = false;
          }, 2000); // Cerrar el modal después de 2 segundos
      // Cerrar el modal programáticamente
  });
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
