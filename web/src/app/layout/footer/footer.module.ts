import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './footer.component';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
	declarations: [FooterComponent],
	exports: [FooterComponent],
	imports: [
		CommonModule,
		MaterialModule,
		NgbModule,
		FormsModule,
		ReactiveFormsModule,
		SharedModule
	]
})
export class FooterModule { }