import { Component } from '@angular/core';
import { User } from '../../models/user';
import { UserService } from '../../services/user.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ FormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent {

  user: User = new User();
  selectedFile: File | null = null;

  constructor(private userService: UserService) { }

  onSubmit() {
    if (this.selectedFile) {
      this.userService.registerUser(this.user, this.selectedFile).subscribe(
        (response) => {
          console.log('Usuario registrado con éxito:', response);
          // Restablecer el formulario y la selección de archivo
          this.user = new User();
          this.selectedFile = null;
        },
        (error) => {
          console.error('Error al registrar el usuario:', error);
        }
      );
    } else {
      console.error('Debes seleccionar un archivo de imagen.');
    }
  }

  onFileSelected(event: any) {
    const files: FileList = event.target.files;
    if (files.length > 0) {
      this.selectedFile = files[0];
      this.user.fotoPerfil = this.selectedFile; // Asignar el archivo seleccionado al campo fotoPerfil
    }
  }
  
}
