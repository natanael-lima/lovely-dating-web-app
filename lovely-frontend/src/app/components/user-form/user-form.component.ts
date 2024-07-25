import { Component, NgModule, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { FormsModule, FormBuilder, FormControl } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Router } from '@angular/router';
import { UserDTO } from '../../interfaces/userDTO';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent implements OnInit{
  activeSection: 'profilePersonal' | 'profileDetail' | 'profilePreference' = 'profilePersonal';
  registerSuccessMessage = false; // Variable para controlar la visibilidad del mensaje de registro exitoso
  errorMessage=false;
  selectedImageURL: string | ArrayBuffer = '';
  usernameExists = false;
  countries : any [] = [];
  userRequest!: UserDTO; //new
  interestsList: string[] = [
    'Música', 'Viajes', 'Deportes', 'Arte', 'Fotografía',
    'Literatura', 'Ciencia', 'Moda', 'Historia', 'Cine'
  ];
  
  checkedInterests: { [key: string]: boolean } = {};
  
  constructor(private userService: UserService,private router: Router) { 
    this.userRequest = {
      id: 0,
      username: '',
      password: '',
      lastname: '',
      name: '',
      role: '',
      state: '',
      isVisible: false,
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
  ngOnInit(): void {
    this. getAllCountries();
  }

  
  // Método para obtener los intereses seleccionados
  getSelectedInterests() {
    return Object.keys(this.checkedInterests).filter(key => this.checkedInterests[key]);
  }
  nextSection() {
    if (this.activeSection === 'profilePersonal') {
      this.activeSection = 'profileDetail';
    } else if (this.activeSection === 'profileDetail') {
      this.activeSection = 'profilePreference';
    }
  }
  previousSection() {
    if (this.activeSection === 'profilePreference') {
      this.activeSection = 'profileDetail';
    } else if (this.activeSection === 'profileDetail') {
      this.activeSection = 'profilePersonal';
    }
  }
  resetErrorMessage() {
    this.errorMessage = false;
  }

  onSubmit() {
    if (this.activeSection === 'profilePersonal') {
       this.nextSection();
    } else {
      // Crear un nuevo FormData
      const formData = new FormData();
      const selectedInterests = this.getSelectedInterests();
      this.userRequest.preference.interests = selectedInterests;//falta limitar a que no pueda marcar +4 intereses

      formData.append('request', new Blob([JSON.stringify({
        username: this.userRequest.username,
        password: this.userRequest.password,
        lastname: this.userRequest.lastname,
        name: this.userRequest.name,
        preference: {
          maxAge: this.userRequest.preference.maxAge,
          minAge: this.userRequest.preference.minAge,
          likeGender: this.userRequest.preference.likeGender,
          location: this.userRequest.preference.location,
          distance: 1,
          interests: this.userRequest.preference.interests
        },
        profileDetail: {
          phone: this.userRequest.profileDetail.phone,
          gender: this.userRequest.profileDetail.gender,
          birthDate: this.userRequest.profileDetail.birthDate.toString(),
          description: this.userRequest.profileDetail.description,
          work: this.userRequest.profileDetail.work,
          photoFileName: this.userRequest.profileDetail.photoFileName
        }
      })], {
        type: 'application/json'
      }));  
    
      // Agregar el archivo de foto (si existe) en FormData
      if (this.userRequest.profileDetail.photo) {
        formData.append('file', this.userRequest.profileDetail.photo, this.userRequest.profileDetail.photoFileName);
      }
      
      console.log('data:',formData.get('request'))

      this.userService.registerUser(formData).pipe(
        tap(response => {
          // Registro exitoso, mostrar el mensaje
          if (response) {
            this.registerSuccessMessage = true;
          }
        }),
        catchError(error => {
          this.errorMessage = true;
          console.error('Error al registrar el usuario:', error);
          // Puedes manejar otros tipos de errores aquí
          return throwError(error);
        })
      ).subscribe();
    }
  } 
  
  onFileSelect(event: any) {
    const file = event?.target?.files?.[0];
    
    if (file) {
      console.log("entre file");
      this.userRequest.profileDetail.photo = file;
      this.userRequest.profileDetail.photoFileName = file.name;
      console.log("tengo file ",this.userRequest.profileDetail.photo);
        // Crear una URL local para la imagen seleccionada
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (event) => {
            this.selectedImageURL = event.target?.result as string;
        };
    }
  }
  onFileInputClick(): void {
    const inputElement = document.getElementById('file-input') as HTMLInputElement;
    if (inputElement) {
        inputElement.click(); // Simula el clic en el input de tipo file
    }
  }

  //this.router.navigate(['/registration-profile', response.id]);
  newUser() {
    //this.userRequest = new User('','','','');
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

  checkUsername() {
    this.userService.checkUsernameExists(this.userRequest.username).subscribe(
      response => {
        this.usernameExists = response.exists;
        if (this.usernameExists) {
          this.errorMessage = true;
          console.log('Usuario existent:',this.errorMessage);
        } else {
          
        }
      },
      error => {
        console.error('Error al verificar el nombre de usuario:', error);
        // Manejar el error según tu necesidad
      }
    );
  }

}
