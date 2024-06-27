import { HttpInterceptorFn, HttpRequest } from '@angular/common/http';

export const customInterceptor: HttpInterceptorFn = (req, next) => {
  const myToken = sessionStorage.getItem('token');
  
  if (myToken && myToken.length > 0) {

    let modifiedReq;
    if (req.body instanceof FormData) {
      
      // Si la URL de la solicitud incluye '/api/user/updateProfilePhoto', cambia el tipo de contenido a multipart/form-data
      modifiedReq = req.clone({
        setHeaders: {
          'Accept': 'application/json', // cambie a json porque envia un request json + file que seria multi
          'Authorization': `Bearer ${myToken}`
        }
      });
    } else {
      // De lo contrario, deja el tipo de contenido como application/json
      modifiedReq = req.clone({
        setHeaders: {
          'Content-Type': 'application/json; charset=utf-8',
          'Accept': 'application/json',
          'Authorization': `Bearer ${myToken}`
        }
      });
    }
    return next(modifiedReq);
  } else {
    return next(req);
  }
};