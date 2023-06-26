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

const routes: Routes = [{
	path: '',
	component: HomeComponent
}, {
	path: 'trueques',
	component: TruequeComponent
}, {
	path: 'donaciones',
	component: DonacionesComponent
}, {
	path: 'registro',
	component: RegistroComponent
},{
	path: 'perfil',
	component: PerfilComponent,
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
	component: HomeComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
