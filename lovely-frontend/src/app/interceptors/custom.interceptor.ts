import { HttpInterceptorFn } from '@angular/common/http';

export const customInterceptor: HttpInterceptorFn = (req, next) => {
  const myToken = sessionStorage.getItem('token');

  if (myToken) {
    const modifiedReq = req.clone({
      setHeaders: {
        'Content-Type': 'application/json; charset=utf-8',
        'Accept': 'application/json',
        'Authorization': `Bearer ${myToken}`
      }
    });
    return next(modifiedReq); // Devolver la solicitud modificada al siguiente interceptor
  } else {
    return next(req); // Devolver la solicitud original al siguiente interceptor
  }
};