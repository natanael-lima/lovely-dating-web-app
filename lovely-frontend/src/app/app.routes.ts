
import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ChatComponent } from './components/chat/chat.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { MatchComponent } from './components/match/match.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { authGuardGuard } from './guards/auth-guard.guard';
import { NotificationComponent } from './components/notification/notification.component';
import { DashboardAdminComponent } from './components/admin/dashboard-admin/dashboard-admin.component';
import { LoginAdminComponent } from './components/admin/login-admin/login-admin.component';
import { ExploreComponent } from './components/explore/explore.component';
import { HomeComponent } from './components/home/home.component';


export const routes: Routes = [
    { path: 'home', component: HomeComponent},
    { path: 'user/signin', component: LoginComponent },
    { path: 'user/signup', component: UserFormComponent },
    { path: 'admin', component: LoginAdminComponent },
    { path: 'admin/dashboard', component: DashboardAdminComponent},
    { path: 'perfil',  canActivate: [authGuardGuard],component: PerfilComponent },
    { path: 'search', canActivate: [authGuardGuard], component: ExploreComponent },
    { path: 'notification', canActivate: [authGuardGuard], component: NotificationComponent },
    { path: 'match/:userId1/:userId2', canActivate: [authGuardGuard], component: MatchComponent },
    //{ path: 'chat/:userId1/:userId2', component: ChatComponet },
    { path: '', redirectTo: '/home', pathMatch: 'full' },
    { path: '**', redirectTo: '/home' }
  ];
 
