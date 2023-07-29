import { Component } from '@angular/core';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	images = ['assets/banners/ecoswap_banner1.png', 'assets/banners/ecoswap_banner2.png']

	constructor(private service: UsuarioService){
	}
}
