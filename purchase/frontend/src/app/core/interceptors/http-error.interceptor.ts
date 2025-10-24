import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const httpErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Just log to console, don't show alert
      // The component will handle showing toasters
      console.error('HTTP Error:', error);
      
      // Pass the error to the component
      return throwError(() => error);
    })
  );
};
