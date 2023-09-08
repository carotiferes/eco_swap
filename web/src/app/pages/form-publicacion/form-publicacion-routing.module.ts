import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormPublicacionComponent } from './form-publicacion.component';

const routes: Routes = [
  {
    path: '',
    component: FormPublicacionComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class FormPublicacionRoutingModule {}