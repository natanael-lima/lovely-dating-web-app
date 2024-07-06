import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './dashboard-admin.component.html',
  styleUrl: './dashboard-admin.component.css'
})
export class DashboardAdminComponent {
  currentSection: string = 'analytics'; // Sección actual, inicialmente 'perfil'
  constructor(){
    
  }

   // Función para cambiar la sección actual
   changeSection(section: string) {
    this.currentSection = section;

  }
}
