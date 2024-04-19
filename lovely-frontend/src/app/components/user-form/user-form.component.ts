import { Component, NgModule } from '@angular/core';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { FormsModule, FormBuilder, FormControl } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ FormsModule, HttpClientModule ],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent {
  
  registerSuccessMessage = false; // Variable para controlar la visibilidad del mensaje de registro exitoso
  errorMessage=false;
  user: User;
  model = new User('', '', '', '');

  constructor(private userService: UserService) { 
    this.user = new User('','','','');
  }

  onSubmit() {
    this.userService.registerUser(this.user).pipe(
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
  
  newUser() {
    this.model = new User('','','','');
  }
}
