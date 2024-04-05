import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { LoginService } from '../services/login.service';


export const authGuardGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state:RouterStateSnapshot) => {
      if (inject(LoginService).isLoggedIn.value) {
        return true; // Si el usuario está autenticado, permitir acceso a la ruta
      } else {
        inject(Router).navigate(['/login']); // Si no está autenticado, redirigir a la página de inicio de sesión
        return false;
      }
};
