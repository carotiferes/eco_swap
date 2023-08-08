import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse, } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError, timer } from 'rxjs';
import { catchError, finalize, map, mergeMap } from 'rxjs/operators';
import { ShowErrorService } from '../services/show-error.service';
import { AuthService } from '../services/auth.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

	private token: string | null = '';

	constructor(private authService: AuthService, private showErrorService: ShowErrorService) { }
	
	intercept( request: HttpRequest<unknown>, next: HttpHandler ): Observable<HttpEvent<unknown>> {
		// Agrega el token a los encabezados de la solicitud
		this.token = this.authService.getUserToken();
		if(this.token) {
				request = request.clone({
				setHeaders: {
					Authorization: `Bearer ${this.token}`,
				},
			});
		}

		return next.handle(request).pipe(
			map((event: HttpEvent<any>) => {
				if (event instanceof HttpResponse) { }
				return event;
			  }),
			catchError((err) => {
				let error: BackendError = { message: '', severity: ErrorSeverity.INFO, code: '' };
				
				if (err instanceof ErrorEvent) { // client side error
					error = this.handleUnknownError();
					console.log('Error del lado del cliente', JSON.stringify(err));
					this.showErrorService.show('Error!', err.error.descripcion)

				} else { // server side error
					error = this.handleBackendError(error, err);
					console.log('Server error with code: ' + JSON.stringify(err));
					this.showErrorService.show('Error!', err.error.descripcion)
				}
				return throwError(() => error);
			})
		);
	}

	private handleUnknownError(): BackendError {
		// this is not from backend. Format our own message.
		return {
			message: 'Unknown error!',
			severity: ErrorSeverity.FATAL,
			code: 'UNKNOWN_ERROR',
		};
	}

	private handleBackendError(error: BackendError, err: any): BackendError {
		// Backend returned error, format it here
		return {
			title: err.error?.title || 'Default title',
			message:
				err.error && err.error.message
					? err.error.message
					: err.error
						? err.error
						: err.message,
			severity: ErrorSeverity.ERROR,
			code: err.error?.identifierCode
				? err.error.identifierCode
				: 'BACKEND_ERROR',
		};
	}
}

export enum ErrorSeverity {
	INFO = 'INFO',
	WARNING = 'WARNING',
	ERROR = 'ERROR',
	FATAL = 'FATAL',
}

export interface BackendError {
	title?: string;
	message: string;
	severity: ErrorSeverity;
	code: string;
}

export const genericRetryStrategy = ({ maxRetryAttempts = 3, scalingDuration = 1000, excludedStatusCodes = [] }: {
	maxRetryAttempts?: number; scalingDuration?: number; excludedStatusCodes?: number[];
} = {}) =>
	(attempts: Observable<any>) => {
		return attempts.pipe(
			mergeMap((error, i) => {
				const retryAttempt = i + 1;
				// if maximum number of retries have been met
				// or response is a status code we don't wish to retry, throw error
				if (
					retryAttempt > maxRetryAttempts ||
					excludedStatusCodes.find((e) => e === error.status)
				) {
					return throwError(error);
				}
				console.log(
					`Attempt ${retryAttempt}: retrying in ${retryAttempt * scalingDuration
					}ms`
				);
				// retry after 1s, 2s, etc...
				return timer(retryAttempt * scalingDuration);
			}),
			finalize(() => console.log('We are done!'))
		);
	};