import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Injectable({
	providedIn: 'root'
})
export class ShowErrorService {

	constructor(private router: Router) { }

	show(title: string, text: string){
		Swal.fire({
			title,
			text,
			icon: 'error'
		})
		//this.router.navigate(['error'])
	}
}
