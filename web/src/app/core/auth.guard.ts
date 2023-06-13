import { CanActivateFn } from '@angular/router';
import { MenuModel } from '../models/menu.model';
const menus = require('../data/menu.json')


export const authGuard: CanActivateFn = (route, state) => {
	console.log(route, state);
	localStorage.getItem('user')
	//menus.find((item: MenuModel) => item.path == route.url)
	return true;
};
