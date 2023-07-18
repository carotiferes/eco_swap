import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { TruequeComponent } from './pages/trueque/trueque.component';
import { ColectasComponent } from './pages/colectas/colectas.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { authGuard } from './core/auth.guard';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { ColectaComponent } from './pages/colecta/colecta.component';
import { FormPropuestaComponent } from './pages/form-propuesta/form-propuesta.component';
import { LoginComponent } from './pages/login/login.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { FormColectaComponent } from './pages/form-colecta/form-colecta.component';
import { PropuestasComponent } from './pages/propuestas/propuestas.component';

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
	path: 'colecta/:id_solicitud',
	component: ColectaComponent,
	canActivate: [authGuard]
},{
	path: 'propuesta/:id_solicitud',
	component: FormPropuestaComponent,
	canActivate: [authGuard]
},{
	path: 'mis-propuestas',
	component: PropuestasComponent,
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
