import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './layout.component';

const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: 'home',
        loadChildren: () => import('../pages/home/home.module').then((m) => m.HomeModule),
        canActivate: [],
      },
      {
        path: '',
        loadChildren: () => import('../pages/home/home.module').then((m) => m.HomeModule),
        canActivate: [],
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LayoutRoutingModule {}
