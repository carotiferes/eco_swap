import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { TruequeComponent } from './pages/trueque/trueque.component';
import { ColectasComponent } from './pages/colectas/colectas.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { authGuard } from './core/auth.guard';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { ColectaComponent } from './pages/colecta/colecta.component';
import { FormDonacionComponent } from './pages/form-donacion/form-donacion.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { FormColectaComponent } from './pages/form-colecta/form-colecta.component';
import { DonacionesComponent } from './pages/donaciones/donaciones.component';
import { ErrorComponent } from './pages/error/error.component';

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
	path: 'colectas',
	component: ColectasComponent
}, {
	path: 'colectas/:id_fundacion',
	component: ColectasComponent
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
	path: 'form-colecta',
	component: FormColectaComponent,
	canActivate: [authGuard]
},{
	path: 'colecta/:id_colecta',
	component: ColectaComponent,
	canActivate: [authGuard]
},{
	path: 'donacion/:id_colecta',
	component: FormDonacionComponent,
	canActivate: [authGuard]
},{
	path: 'mis-donaciones',
	component: DonacionesComponent,
	canActivate: [authGuard]
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
