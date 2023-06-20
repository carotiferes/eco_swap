import { Component } from '@angular/core';
import { PerfilModel } from 'src/app/models/perfil.model';
const db = require('../../data/db.json')

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent {
	isSwapper: boolean = false; // CHANGE WHEN BRINGING USER INFO
	user: PerfilModel;
	userInfo: any;

	columns: number = 2;
	colspan: number = 1;
	screenWidth: number = 0;

	constructor(){
		// TODO: GET USER INFO
		this.user = db.perfiles[0];
		this.userInfo = db.swappers[0];
		this.configureColumns();
	}
	
	configureColumns(){
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		/* if(this.screenWidth > 576) {this.columns = 4;}
		else this.columns = 2; */
	}
}
