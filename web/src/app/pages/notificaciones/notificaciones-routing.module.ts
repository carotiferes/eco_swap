import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotificacionesComponent } from './notificaciones.component';

const routes: Routes = [
  {
    path: '',
    component: NotificacionesComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class NotificacionesRoutingModule {}