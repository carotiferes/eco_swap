import { Component } from '@angular/core';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	images = [
		'assets/banners/portadas-01.jpg', 'assets/banners/portadas-02.jpg',
		'assets/banners/portadas-03.jpg', 'assets/banners/portadas-04.jpg'
	]

	constructor(private service: UsuarioService){
	}
}
