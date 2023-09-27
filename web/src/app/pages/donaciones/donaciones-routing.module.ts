import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DonacionesComponent } from './donaciones.component';

const routes: Routes = [
  {
    path: '',
    component: DonacionesComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DonacionesRoutingModule {}