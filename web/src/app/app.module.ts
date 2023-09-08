import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './layout/footer/footer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './shared/material/material.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PasswordPipe } from './pipes/password.pipe';
import { UsuarioService } from './services/usuario.service';
import { HttpBackEnd } from './services/httpBackend.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpErrorInterceptor } from './interceptors/error.interceptor';
import { SharedModule } from './shared/shared.module';
import { SafeHtmlPipe } from './pipes/safe-html.pipe';
import { PhoneNumberPipe } from './pipes/phone-number.pipe';
import { DateAdapter } from '@angular/material/core';
import { CustomDateAdapter } from './pipes/date-adapter';
import { CommonModule } from '@angular/common';

@NgModule({
	declarations: [
		AppComponent,
		FooterComponent,
		
	],
	imports: [
		CommonModule,
		BrowserModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		MaterialModule,
		NgbModule,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule,
		SharedModule
	],
	providers: [
		HttpBackEnd,
		UsuarioService,
		{
			provide: HTTP_INTERCEPTORS,
			useClass: HttpErrorInterceptor,
			multi: true,
		},
		PhoneNumberPipe,
		{provide: DateAdapter, useClass: CustomDateAdapter }
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
