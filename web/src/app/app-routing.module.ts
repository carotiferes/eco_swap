import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { TruequeComponent } from './pages/trueque/trueque.component';
import { DonacionesComponent } from './pages/donaciones/donaciones.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { authGuard } from './core/auth.guard';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { SolicitudComponent } from './pages/solicitud/solicitud.component';
import { PropuestaComponent } from './pages/propuesta/propuesta.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { FormSolicitudComponent } from './pages/form-solicitud/form-solicitud.component';

const routes: Routes = [{
	path: '',
	component: LoginComponent
}, {
	path: 'home',
	component: HomeComponent
}, {
	path: 'trueques',
	component: TruequeComponent
}, {
	path: 'donaciones',
	component: DonacionesComponent
}, {
	path: 'donaciones/:id_fundacion',
	component: DonacionesComponent
},{
	path: 'registro',
	component: RegistroComponent
},{
	path: 'perfil',
	component: PerfilComponent,
	canActivate: [authGuard]
},{
	path: 'perfil/:id',
	component: PerfilComponent,
	canActivate: [authGuard]
},{
	path: 'form-solicitud',
	component: FormSolicitudComponent,
	canActivate: [authGuard]
},{
	path: 'solicitud/:id_solicitud',
	component: SolicitudComponent,
	canActivate: [authGuard]
},{
	path: 'propuesta/:id_solicitud',
	component: PropuestaComponent,
	canActivate: [authGuard]
},{
	path: '**',
	component: NotFoundComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
