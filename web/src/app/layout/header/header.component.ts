import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MenuModel } from 'src/app/models/menu.model';
import { AuthService } from 'src/app/services/auth.service';
const menuData = require('../../data/menu.json')

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

	url: string = 'home';

	searchText: string = '';
	mainTabs: MenuModel[] = [];
	personalTabs: MenuModel[] = [];

	//userData: any;
	isUserLoggedIn: boolean;

	constructor(private router: Router, private auth: AuthService){
		this.mainTabs = menuData.filter((item: MenuModel) => item.type == 'pages')
		this.personalTabs = menuData.filter((item: MenuModel) => item.type == 'personal')
		this.url = router.url;

		this.isUserLoggedIn = auth.isUserLoggedIn
	}

	goTo(path: string){
		this.router.navigateByUrl(path)
	}

	logOut(){
		
	}
}
