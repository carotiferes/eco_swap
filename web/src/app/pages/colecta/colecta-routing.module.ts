import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ColectaComponent } from './colecta.component';

const routes: Routes = [
  {
    path: '',
    component: ColectaComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ColectaRoutingModule {}