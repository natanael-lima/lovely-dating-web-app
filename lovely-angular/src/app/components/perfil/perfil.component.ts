import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // Importa CommonModule
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})
export class PerfilComponent {
  currentSection: string = 'perfil'; // Sección actual, inicialmente 'perfil'
 // Función para cambiar la sección actual
  changeSection(section: string): void {
    this.currentSection = section;
  }
}
