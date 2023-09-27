import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PublicacionComponent } from './publicacion.component';

const routes: Routes = [
  {
    path: '',
    component: PublicacionComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PublicacionRoutingModule {}