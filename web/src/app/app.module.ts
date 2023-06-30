import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './pages/home/home.component';
import { HeaderComponent } from './layout/header/header.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { FooterComponent } from './layout/footer/footer.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './shared/material.module';
import { TruequeComponent } from './pages/trueque/trueque.component';
import { DonacionesComponent } from './pages/donaciones/donaciones.component';
import { RegistroComponent } from './pages/registro/registro.component';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PasswordPipe } from './pipes/password.pipe';
import { SolicitudComponent } from './pages/solicitud/solicitud.component';
import { PropuestaComponent } from './pages/propuesta/propuesta.component';
import { UsuarioService } from './services/usuario.service';
import { HttpBackEnd } from './services/httpBackend.service';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
	declarations: [
		AppComponent,
		HomeComponent,
		HeaderComponent,
		NotFoundComponent,
		FooterComponent,
		TruequeComponent,
		DonacionesComponent,
		RegistroComponent,
		PerfilComponent,
		PasswordPipe,
		SolicitudComponent,
		PropuestaComponent,
  LoginComponent
	],
	imports: [
		BrowserModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		MaterialModule,
		NgbModule,
		HttpClientModule,
		FormsModule,
		ReactiveFormsModule
	],
	providers: [
		HttpBackEnd,
		UsuarioService,
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
