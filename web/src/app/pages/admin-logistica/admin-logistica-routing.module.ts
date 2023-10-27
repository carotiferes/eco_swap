import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLogisticaComponent } from './admin-logistica.component';

const routes: Routes = [
  {
    path: '',
    component: AdminLogisticaComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminLogisticaRoutingModule {}
