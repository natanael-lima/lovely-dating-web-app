
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { ChatComponent } from './components/chat/chat.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { MatchComponent } from './components/match/match.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { authGuardGuard } from './guards/auth-guard.guard';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { NotificationComponent } from './components/notification/notification.component';

export const routes: Routes = [
    { path: 'welcome', component: WelcomeComponent},
    { path: 'user/signin', component: LoginComponent },
    { path: 'user/signup', component: UserFormComponent },
    { path: 'perfil',  canActivate: [authGuardGuard],component: PerfilComponent },
    { path: 'search', canActivate: [authGuardGuard], component: HomeComponent },
    { path: 'notification', canActivate: [authGuardGuard], component: NotificationComponent },
    { path: 'match/:userId1/:userId2', canActivate: [authGuardGuard], component: MatchComponent },
    //{ path: 'match/:userId1/:userId2', component: MatchComponent },
    { path: '', redirectTo: '/welcome', pathMatch: 'full' },
    { path: '**', redirectTo: '/welcome' }
  ];
 
