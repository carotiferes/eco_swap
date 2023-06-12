import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { TruequeComponent } from './pages/trueque/trueque.component';
import { DonacionesComponent } from './pages/donaciones/donaciones.component';
import { RegistroComponent } from './pages/registro/registro.component';

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
	path: '**',
	component: HomeComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
