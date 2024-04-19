
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { ChatComponent } from './components/chat/chat.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { MatchComponent } from './components/match/match.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { authGuardGuard } from './guards/auth-guard.guard';

export const routes: Routes = [
    { path: 'user/login', component: LoginComponent },
    { path: 'user/signup', component: UserFormComponent },
    { path: 'perfil',  canActivate: [authGuardGuard],component: PerfilComponent },
    { path: 'home', canActivate: [authGuardGuard], component: HomeComponent },
    { path: 'match', canActivate: [authGuardGuard], component: MatchComponent },
    { path: 'chat/:userId1/:userId2', component: ChatComponent },
    { path: '', redirectTo: '/user/login', pathMatch: 'full' },
    { path: '**', redirectTo: '/user/login' }
  ];
 
