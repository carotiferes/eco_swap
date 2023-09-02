import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormDonacionComponent } from './form-donacion.component';

const routes: Routes = [
  {
    path: '',
    component: FormDonacionComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FormDonacionRoutingModule {}