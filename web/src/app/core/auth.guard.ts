import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const AuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
	// Obtiene el servicio AuthService mediante inyección de dependencias
	const authService = inject(AuthService);

	if (authService.isUserLoggedIn) {
		return true;
	} else {
		// Si el usuario no ha iniciado sesión, redirige al componente de inicio de sesión
		const router = inject(Router);
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
