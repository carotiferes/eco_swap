import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
	selector: 'app-trueque',
	templateUrl: './trueques.component.html',
	styleUrls: ['./trueques.component.scss']
})
export class TruequesComponent {

	userData: any;

	constructor(private router: Router, private auth: AuthService){
		this.userData = auth.getUserData();
	}

	addPublicacion(){
		this.router.navigate(['form-publicacion'])
	}
}
