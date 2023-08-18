import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { TruequesComponent } from './pages/trueques/trueques.component';
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

const routes: Routes = [{
	path: '',
	component: HomeComponent
}, {
	path: 'home',
	component: HomeComponent
}, {
	path: 'login',
	component: LoginComponent
}, {
	path: 'trueques',
	component: TruequesComponent
}, {
	path: 'colectas',
	component: ColectasComponent
}, {
	path: 'mis-colectas',
	component: ColectasComponent,
	canActivate: [AuthGuard]
},{
	path: 'registro',
	component: RegistroComponent
},{
	path: 'perfil',
	component: PerfilComponent,
	canActivate: [AuthGuard]
},{
	path: 'perfil/:id',
	component: PerfilComponent,
	canActivate: [AuthGuard]
},{
	path: 'form-colecta',
	component: FormColectaComponent,
	canActivate: [AuthGuard]
},{
	path: 'colecta/:id_colecta',
	component: ColectaComponent,
	//canActivate: [AuthGuard]
},{
	path: 'donacion/:id_colecta',
	component: FormDonacionComponent,
	canActivate: [AuthGuard]
},{
	path: 'mis-donaciones',
	component: DonacionesComponent,
	canActivate: [AuthGuard]
},{
	path: 'reset-password',
	component: RegistroComponent
},{
	path: 'form-publicacion',
	component: FormPublicacionComponent,
	//canActivate: [AuthGuard] TODO: CAMBIAR CUANDO LA INFO DEL USER ESTE EN LOCAL STORAGE
},{
	path: 'publicacion/:id_publicacion',
	component: PublicacionComponent,
},{
	path: 'error',
	component: ErrorComponent
},{
	path: '**',
	component: NotFoundComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
