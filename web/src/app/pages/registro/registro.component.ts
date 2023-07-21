import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import Swal from 'sweetalert2';
import { DateAdapter } from '@angular/material/core';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss'],
  providers: [{provide: MAT_DATE_LOCALE, useValue: 'en-GB'},]
})
export class RegistroComponent {
	registroForm: FormGroup;

	isSwapper: boolean = true;
	loading = true;

	screenWidth: number;

	constructor(private fb: FormBuilder, private dateAdapter: DateAdapter<Date>){
		this.registroForm = fb.group({
			s_email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/)]],
			//s_usuario: ['', Validators.required],
			s_nombre: [''],
			s_apellido: [''],
			f_nacimiento: [''],
			s_dni: [''],
			s_cuit: [''],
			//s_nombre: ['',],
			s_direccion: [''] // revisar si es un string o de otra tabla
		})
		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.dateAdapter.setLocale('es-AR'); // DD/mm/YYYY
	}

	ngOnInit(): void {
		this.selectTipoPerfil()
	}

	selectTipoPerfil(){
		Swal.fire({
			title: '¡Te damos la bienvenida!',
			text: `Nos alegra que te sumes a nuestra comunidad, antes de seguir, 
			necesitamos saber si te querés registrar como fundación o como Particular`,
			confirmButtonText: 'Represento a una fundación',
			confirmButtonColor: '#87db59',
			cancelButtonText: 'No soy de una fundación',
			showCancelButton: true,
			cancelButtonColor: '#ae59db',
			icon: 'question',
			allowOutsideClick: false,
			allowEscapeKey: false
		}).then(result => {
			console.log(result);
			if(result.isConfirmed){ // FUNDACION
				this.isSwapper = false;
				this.registroForm.controls['s_nombre'].addValidators(Validators.required)
				this.registroForm.controls['s_cuit'].addValidators(Validators.required)
			} else { // SWAPPER
				this.registroForm.controls['s_nombre'].addValidators(Validators.required)
				this.registroForm.controls['s_apellido'].addValidators(Validators.required)
				this.registroForm.controls['f_nacimiento'].addValidators(Validators.required)
				this.registroForm.controls['s_dni'].addValidators(Validators.required)

			}
			console.log(this.isSwapper);
			this.loading = false
		})
	}

	crearCuenta(formValue: any){
		console.log('registro', formValue);
		if(this.registroForm.valid){
			// TODO: SAVE IN REAL DB
			
				// IF OK THEN MESSAGE:
				this.showMessage('¡Gracias por registrarte!',
				`Te enviamos un email a la cuenta que ingresaste,
				para que confirmes tu cuenta y crees un usuario y contraseña.`,
				'No recibí el email', 'send_again', 'success')

		}
	}

	showMessage(title: string, text: string, confirm: string, origin: string,
		icon: 'success' | 'error' | 'warning'){
			Swal.fire({
				title,
				text,
				confirmButtonText: confirm,
				icon
			}).then(({isConfirmed}) => {
				if(origin == 'send_again' && isConfirmed){
					// TODO: RESEND EMAIL
				}
			})
	}

}
