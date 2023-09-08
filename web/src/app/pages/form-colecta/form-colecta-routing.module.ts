import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormColectaComponent } from './form-colecta.component';

const routes: Routes = [
  {
    path: '',
    component: FormColectaComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FormColectaRoutingModule {}