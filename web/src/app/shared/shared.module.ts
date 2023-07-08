import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { MapComponent } from './map/map.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SpinnerComponent } from './spinner/spinner.component';
import { MaterialModule } from './material/material.module';

@NgModule({
	declarations: [
		MapComponent,
		SpinnerComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		MaterialModule
	],
	exports: [
		MapComponent,
		SpinnerComponent
	]
})
export class SharedModule { }
