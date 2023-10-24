import { Component, Input, SimpleChanges } from '@angular/core';
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
	@Input() refresh?: number;

	url: string = 'home';

	searchText: string = '';
	mainTabs: MenuModel[] = [];
	personalTabs: MenuModel[] = [];

	userData: any;
	isUserLoggedIn: boolean;
	profileType: 'particular' | 'fundacion';

	notifications: number = 0;
	userID?: number;

	constructor(private router: Router, private auth: AuthService, private usuarioService: UsuarioService){
		this.mainTabs = menuData.filter((item: MenuModel) => item.type == 'pages')
		this.personalTabs = menuData.filter((item: MenuModel) => item.type == 'personal')
		this.url = router.url;

		this.isUserLoggedIn = auth.isUserLoggedIn
		this.profileType = auth.isUserSwapper() ? 'particular' : 'fundacion';

		if (this.isUserLoggedIn) {
			this.userID = this.auth.getUserID()
			this.getUserData()
		}
	}

	ngOnChanges(changes: SimpleChanges): void {
		//Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
		//Add '${implements OnChanges}' to the class.
		console.log(changes['refresh']);
		if(changes['refresh'] && !changes['refresh'].isFirstChange() && changes['refresh'].currentValue != changes['refresh'].previousValue){
			this.getUserData()
		}
	}

	getUserData() {
		if(this.userID) {
			this.usuarioService.getUserByID(this.userID).subscribe({
				next: (res: any) => {
					this.userData = res;
					//console.log(this.userData);
	
					// GET NOTIFICATIONS
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
