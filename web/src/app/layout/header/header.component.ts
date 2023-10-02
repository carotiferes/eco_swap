import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { MenuModel } from 'src/app/models/menu.model';
import { AuthService } from 'src/app/services/auth.service';
import { UsuarioService } from 'src/app/services/usuario.service';
const menuData = require('../../data/menu.json')

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

	@Input() title!: string;

	url: string = 'home';

	searchText: string = '';
	mainTabs: MenuModel[] = [];
	personalTabs: MenuModel[] = [];

	userData: any;
	isUserLoggedIn: boolean;
	profileType: 'particular' | 'fundacion';

	constructor(private router: Router, private auth: AuthService, private usuarioService: UsuarioService){
		this.mainTabs = menuData.filter((item: MenuModel) => item.type == 'pages')
		this.personalTabs = menuData.filter((item: MenuModel) => item.type == 'personal')
		this.url = router.url;

		this.isUserLoggedIn = auth.isUserLoggedIn
		this.profileType = auth.isUserSwapper() ? 'particular' : 'fundacion';

		if (this.isUserLoggedIn) {
			usuarioService.getUserByID(auth.getUserID()).subscribe({
				next: (res: any) => {
					this.userData = res;
					//console.log(this.userData);
				}
			})
		}
	}

	goTo(path: string){
		this.router.navigateByUrl(path)
	}

	logOut(){
		this.auth.logout();
	}
}
