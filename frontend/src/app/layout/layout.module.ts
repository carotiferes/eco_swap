import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LayoutRoutingModule } from './layout-routing.module';

import { MaterialModule } from 'src/app/shared/material/material.module';
import { FooterComponent } from './footer/footer.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HeaderComponent } from './header/header.component';
import { LayoutComponent } from './layout.component';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
	LayoutComponent,
    HeaderComponent,
	SidebarComponent,
	FooterComponent
  ],
  imports: [
	CommonModule,
	LayoutRoutingModule,
	MaterialModule,
	MatIconModule
],
})
export class LayoutModule {}
