import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ColectasComponent } from './colectas.component';

const routes: Routes = [
  {
    path: '',
    component: ColectasComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ColectasRoutingModule {}