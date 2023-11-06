import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateFn, NavigationEnd } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const AuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
	// Obtiene el servicio AuthService mediante inyección de dependencias
	const authService = inject(AuthService);
	const router = inject(Router);
	
	const onlyFundacionesURLs = ['mis-colectas', 'form-colecta']
	const onlySwapperURLs = [
		'publicaciones', 'form-publicacion', 'mis-publicaciones', 
		'donacion', 'publicacion', 'mis-donaciones', 'mis-compras'
	]
	const noLoginNeededURLs = [
		'', 'home', 'login', 'registro', 'not-found',
		'publicaciones', 'publicacion',
		'colectas', 'colecta'
	]
	const onlyAdminsURLs = ['admin-logistica'];
	/* other (requieren login pero no un perfil especifico): 
		mi-perfil, perfil, edit-perfil, reset-password, notificaciones
	 */
	const url: string = route.url[0].path;
	
	if (authService.isUserLoggedIn) {
		if(authService.getUserID() == 999) {
			if(onlyAdminsURLs.some(item => item == url)) return true;
			else {
				router.navigate(['/admin-logistica'])
				return false
			}
		} else if(authService.isUserSwapper() && onlyFundacionesURLs.some(item => item == url) 
		|| !authService.isUserSwapper() && onlySwapperURLs.some(item => item == url)
		|| onlyAdminsURLs.some(item => item == url)) {
			router.navigate(['/home'])
			return false;
		}
		else return true;
	} else if(noLoginNeededURLs.includes(url)) {
		return true;
	}else {
		// Si el usuario no ha iniciado sesión, redirige al componente de inicio de sesión
		router.navigate(['/login']);
		return false;
	}
};

/* import { CanActivateFn } from '@angular/router';
import { MenuModel } from '../models/menu.model';
const menus = require('../data/menu.json')


export const AuthGuard: CanActivateFn = (route, state) => {
	console.log(route, state);
	const isLoggedIn = localStorage.getItem('userLoggedIn')
	console.log(isLoggedIn);
	if(isLoggedIn == 'true') return true;
	else return false;
	//menus.find((item: MenuModel) => item.path == route.url)
	//return true;
}; */
