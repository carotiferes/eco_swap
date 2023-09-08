import { Component } from '@angular/core';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	screenWidth: number;
	folder: string = 'banners';
	images: string[] = []

	constructor(private service: UsuarioService){
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		if(this.screenWidth < 576) {
			this.folder = 'banners-mobile';
		}
		for (let index = 1; index <= 4; index++) {
			this.images.push(`assets/${this.folder}/0${index}.jpg`)
			
		}
	}
}
