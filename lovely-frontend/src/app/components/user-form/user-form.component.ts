import { Component, NgModule } from '@angular/core';
import { User, UserProfile } from '../../models/user';
import { UserService } from '../../services/user.service';
import { FormsModule, FormBuilder, FormControl } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ FormsModule, HttpClientModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent {
  activeSection: 'user' | 'profile' = 'user';
  registerSuccessMessage = false; // Variable para controlar la visibilidad del mensaje de registro exitoso
  errorMessage=false;
  user: User;
  model = new User('', '', '', '');
  selectedImageURL: string | ArrayBuffer = '';
  
  constructor(private userService: UserService,private router: Router) { 
    this.user = new User('','','','');
    this.user.profile = new UserProfile();

    
  }

  usernameExists = false;


  nextSection() {
    this.activeSection = 'profile';
  }
  resetErrorMessage() {
    this.errorMessage = false;
}



  onSubmit() {
    if (this.activeSection === 'user') {
       this.nextSection();
    } else {
      const formData = new FormData();

        // Agregar campos de datos del usuario
        formData.append('name', this.user.name);
        formData.append('lastname', this.user.lastname);
        formData.append('username', this.user.username);
        formData.append('password', this.user.password);

        // Agregar campos de datos del perfil
        formData.append('profile.location', this.user.profile.location);
        formData.append('profile.gender', this.user.profile.gender);
        formData.append('profile.age', this.user.profile.age.toString());
        formData.append('profile.likeGender', this.user.profile.likeGender);
        formData.append('profile.maxAge', this.user.profile.maxAge.toString());
        formData.append('profile.minAge', this.user.profile.minAge.toString());

        // Agregar archivo de imagen de perfil
        if (this.user.profile.photo) {
          formData.append('photoFile', this.user.profile.photo, this.user.profile.photo.name);
          //formData.append('profile.photoFileName', this.user.profile.photo.name);
        }
        console.log('data:',formData.get('profile.photoFileName'))
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
        this.user.profile.photo = file;
        this.user.profile.photoFileName = file.name;

        // Crear una URL local para la imagen seleccionada
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (event) => {
            this.selectedImageURL = event.target?.result as string;
        };
    }
  }
  
  //this.router.navigate(['/registration-profile', response.id]);
  newUser() {
    this.model = new User('','','','');
  }

  checkUsername() {
    this.userService.checkUsernameExists(this.user.username).subscribe(
      response => {
        this.usernameExists = response.exists;
        if (this.usernameExists) {
          this.errorMessage = true;
          // Puedes mostrar un mensaje al usuario aquí si lo deseas
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
