
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { ChatComponent } from './components/chat/chat.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { MatchComponent } from './components/match/match.component';
import { UserFormComponent } from './components/user-form/user-form.component';

export const routes: Routes = [
    { path: 'user/login', component: LoginComponent },
    { path: 'user/signup', component: UserFormComponent },
    { path: 'home', component: HomeComponent },
    { path: 'match', component: MatchComponent },
    { path: 'chat/:userId', component: ChatComponent },
    { path: 'perfil', component: PerfilComponent },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: '**', redirectTo: '/home' }
  ];
 
