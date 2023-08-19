import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { HeaderComponent } from './layout/header/header.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { FooterComponent } from './layout/footer/footer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './shared/material/material.module';
import { PublicacionesComponent } from './pages/publicaciones/publicaciones.component';
import { ColectasComponent } from './pages/colectas/colectas.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PasswordPipe } from './pipes/password.pipe';
import { ColectaComponent } from './pages/colecta/colecta.component';
import { FormDonacionComponent } from './pages/form-donacion/form-donacion.component';
import { UsuarioService } from './services/usuario.service';
import { HttpBackEnd } from './services/httpBackend.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormColectaComponent } from './pages/form-colecta/form-colecta.component';
import { HttpErrorInterceptor } from './interceptors/error.interceptor';
import { DonacionesComponent } from './pages/donaciones/donaciones.component';
import { SharedModule } from './shared/shared.module';
import { SafeHtmlPipe } from './pipes/safe-html.pipe';
import { ErrorComponent } from './pages/error/error.component';
import { FormPublicacionComponent } from './pages/form-publicacion/form-publicacion.component';
import { PhoneNumberPipe } from './pipes/phone-number.pipe';
import { DateAdapter } from '@angular/material/core';
import { CustomDateAdapter } from './pipes/date-adapter';
import { PublicacionComponent } from './pages/publicacion/publicacion.component';

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		HeaderComponent,
		NotFoundComponent,
		FooterComponent,
		PublicacionesComponent,
		ColectasComponent,
		RegistroComponent,
		PerfilComponent,
		PasswordPipe,
		ColectaComponent,
		FormDonacionComponent,
		LoginComponent,
		FormColectaComponent,
  		DonacionesComponent,
    	SafeHtmlPipe,
		FormPublicacionComponent,
     	ErrorComponent,
      	PhoneNumberPipe,
       PublicacionComponent
	],
	imports: [
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
