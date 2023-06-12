import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.scss']
})
export class RegistroComponent {
	registroForm: FormGroup;

	isSwapper: boolean = true;

	constructor(private fb: FormBuilder){
		this.registroForm = fb.group({
			s_email: ['', Validators.required],
			s_usuario: ['', Validators.required],
			s_nombre: [''],
			s_apellido: [''],
			f_nacimiento: [''],
			s_razon_social: [''],
			s_direccion: [''] // revisar si es un string o de otra tabla
		})
	}

	ngOnInit(): void {
		this.selectTipoPerfil()
	}

	selectTipoPerfil(){
		Swal.fire({
			title: '¡Te damos la bienvenida!',
			text: `Nos alegra que te sumes a nuestra comunidad, antes de seguir, 
			necesitamos saber si te querés registrar como fundación o como Swapper`,
			confirmButtonText: 'Represento a una fundación',
			confirmButtonColor: '#87db59',
			cancelButtonText: 'Quiero ser Swapper',
			showCancelButton: true,
			cancelButtonColor: '#ae59db',
			icon: 'question',
			allowOutsideClick: false,
			allowEscapeKey: false
		}).then(result => {
			console.log(result);
			if(result.isConfirmed){ // FUNDACION
				this.isSwapper = false;
			} else { // SWAPPER

			}
		})
	}

	crearCuenta(){
		console.log('registro');
		
	}

}
