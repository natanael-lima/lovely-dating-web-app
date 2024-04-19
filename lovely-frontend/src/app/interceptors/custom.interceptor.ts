import { HttpInterceptorFn } from '@angular/common/http';

export const customInterceptor: HttpInterceptorFn = (req, next) => {
  const myToken = sessionStorage.getItem('token');
  // Verificar si la solicitud es una carga de archivo
  //const isFileUpload = req.headers.get('Content-Type') === 'multipart/form-data';
  //&& !isFileUpload
  
  if (myToken) {
    let modifiedReq;
    if (req.url.includes('/api/user/updateProfilePhoto')) {
      // Si la URL de la solicitud incluye '/api/user/updateProfilePhoto', cambia el tipo de contenido a multipart/form-data
      modifiedReq = req.clone({
        setHeaders: {
          'Content-Type': 'multipart/form-data; charset=utf-8',
          'Accept': 'multipart/form-data',
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