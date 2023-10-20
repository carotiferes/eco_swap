import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AuthGuard } from './core/auth.guard';
import { NotFoundComponent } from './pages/not-found/not-found.component';

const routes: Routes = [
	{ path: '', component: HomeComponent },
	{ path: 'home', loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule), canActivate: [AuthGuard] },
	{ path: 'login', loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule), data: { hideFooter: true }, canActivate: [AuthGuard] },
	{ path: 'registro', loadChildren: () => import('./pages/registro/registro.module').then(m => m.RegistroModule), canActivate: [AuthGuard] },
	{ path: 'publicaciones', loadChildren: () => import('./pages/publicaciones/publicaciones.module').then(m => m.PublicacionesModule), canActivate: [AuthGuard] },
	{ path: 'publicacion/:id_publicacion', loadChildren: () => import('./pages/publicacion/publicacion.module').then(m => m.PublicacionModule), canActivate: [AuthGuard] },
	{ path: 'not-found', loadChildren: () => import('./pages/not-found/not-found.module').then(m => m.NotFoundModule), canActivate: [AuthGuard] },
	{ path: 'error', loadChildren: () => import('./pages/error/error.module').then(m => m.ErrorModule), canActivate: [AuthGuard] },
	{ path: 'colectas', loadChildren: () => import('./pages/colectas/colectas.module').then(m => m.ColectasModule), canActivate: [AuthGuard] },
	{ path: 'colecta/:id_colecta', loadChildren: () => import('./pages/colecta/colecta.module').then(m => m.ColectaModule), canActivate: [AuthGuard] },
	{
		path: 'mi-perfil',
		loadChildren: () => import('./pages/perfil/perfil.module').then(m => m.PerfilModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'perfil/:id_usuario',
		loadChildren: () => import('./pages/perfil/perfil.module').then(m => m.PerfilModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'form-publicacion',
		loadChildren: () => import('./pages/form-publicacion/form-publicacion.module').then(m => m.FormPublicacionModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'donacion/:id_colecta',
		loadChildren: () => import('./pages/form-donacion/form-donacion.module').then(m => m.FormDonacionModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'form-colecta',
		loadChildren: () => import('./pages/form-colecta/form-colecta.module').then(m => m.FormColectaModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'mis-donaciones',
		loadChildren: () => import('./pages/donaciones/donaciones.module').then(m => m.DonacionesModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'mis-colectas',
		loadChildren: () => import('./pages/colectas/colectas.module').then(m => m.ColectasModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'mis-publicaciones',
		loadChildren: () => import('./pages/publicaciones/publicaciones.module').then(m => m.PublicacionesModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'mis-compras',
		loadChildren: () => import('./pages/publicaciones/publicaciones.module').then(m => m.PublicacionesModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'edit-perfil',
		loadChildren: () => import('./pages/registro/registro.module').then(m => m.RegistroModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'reset-password',
		loadChildren: () => import('./pages/registro/registro.module').then(m => m.RegistroModule),
		canActivate: [AuthGuard]
	},
	{
		path: 'notificaciones',
		loadChildren: () => import('./pages/notificaciones/notificaciones.module').then(m => m.NotificacionesModule),
		canActivate: [AuthGuard]
	},
	{ path: '**', component: NotFoundComponent },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
