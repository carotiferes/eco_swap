import { NgModule } from '@angular/core';
import { MaterialModule } from 'src/app/shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PasswordPipe } from './password.pipe';
import { PhoneNumberPipe } from './phone-number.pipe';
import { SafeHtmlPipe } from './safe-html.pipe';

@NgModule({
	declarations: [
		PasswordPipe,
    	SafeHtmlPipe,
      	PhoneNumberPipe
	],
	exports: [
		PasswordPipe,
    	SafeHtmlPipe,
      	PhoneNumberPipe
	],
	imports: [
		MaterialModule,
		NgbModule,
	]
})
export class PipesModule { }