import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { PublicacionesComponent } from './pages/publicaciones/publicaciones.component';
import { ColectasComponent } from './pages/colectas/colectas.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { AuthGuard } from './core/auth.guard';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { ColectaComponent } from './pages/colecta/colecta.component';
import { FormDonacionComponent } from './pages/form-donacion/form-donacion.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { FormColectaComponent } from './pages/form-colecta/form-colecta.component';
import { DonacionesComponent } from './pages/donaciones/donaciones.component';
import { ErrorComponent } from './pages/error/error.component';
import { FormPublicacionComponent } from './pages/form-publicacion/form-publicacion.component';
import { PublicacionComponent } from './pages/publicacion/publicacion.component';

const routes: Routes = [
	{ path: '', component: HomeComponent },
	{ path: 'home', loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule) },
	{ path: 'login', loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule) },
	{ path: 'registro', loadChildren: () => import('./pages/registro/registro.module').then(m => m.RegistroModule) },
	{ path: 'publicaciones', loadChildren: () => import('./pages/publicaciones/publicaciones.module').then(m => m.PublicacionesModule) },
	{ path: 'publicacion/:id_publicacion', loadChildren: () => import('./pages/publicacion/publicacion.module').then(m => m.PublicacionModule) },
	{ path: 'not-found', loadChildren: () => import('./pages/not-found/not-found.module').then(m => m.NotFoundModule) },
	{ path: 'error', loadChildren: () => import('./pages/error/error.module').then(m => m.ErrorModule) },
	{ path: 'colectas', loadChildren: () => import('./pages/colectas/colectas.module').then(m => m.ColectasModule) },
	{ path: 'colecta/:id_colecta', loadChildren: () => import('./pages/colecta/colecta.module').then(m => m.ColectaModule) },
	{
		path: 'perfil',
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
	{ path: '**', component: NotFoundComponent },
];

@NgModule({
	imports: [RouterModule.forRoot(routes)],
	exports: [RouterModule]
})
export class AppRoutingModule { }
