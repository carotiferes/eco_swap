import { Component } from '@angular/core';
import { UsuarioService } from 'src/app/services/usuario.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	constructor(private service: UsuarioService){
		service.test().subscribe(res => {
			console.log(res);
			
		})
	}
}
