import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { MapComponent } from './map/map.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
	declarations: [
		MapComponent
	],
	imports: [
		BrowserModule,
		BrowserAnimationsModule
	],
	exports: [
		MapComponent
	]
})
export class SharedModule { }
