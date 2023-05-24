import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MenuModel } from 'src/app/models/menu.model';
import { TruequeComponent } from 'src/app/pages/trueque/trueque.component';
const menuData = require('../../data/menu.json')

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

	searchText: string = '';
	mainTabs: MenuModel[] = [];
	personalTabs: MenuModel[] = [];
	
	constructor(private router: Router){
		this.mainTabs = menuData.filter((item: MenuModel) => item.type == 'pages')
		this.personalTabs = menuData.filter((item: MenuModel) => item.type == 'personal')
	}

	goTo(path: string){
		this.router.navigateByUrl(path)
	}
}
