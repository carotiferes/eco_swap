import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { TruequesService } from 'src/app/services/trueques.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';
import { CredencialesMpModalComponent } from '../perfil/credenciales-mp-modal/credenciales-mp-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { UsuarioModel } from 'src/app/models/usuario.model';

@Component({
  selector: 'app-form-publicacion',
  templateUrl: './form-publicacion.component.html',
  styleUrls: ['./form-publicacion.component.scss']
})
export class FormPublicacionComponent {

	publicacionForm: FormGroup;
	images: any[] = [];

	loading: boolean = false;
	showErrors: boolean = false;
	loadingSave: boolean = false;

	focusMin: boolean = false;
	focusMax: boolean = false;
	focusVenta: boolean = false;

	hasMPCredentials: boolean = false;
	user?: UsuarioModel;

	constructor(private fb: FormBuilder, private truequeService: TruequesService, private router: Router,
		private auth: AuthService, private usuarioService: UsuarioService, private dialog: MatDialog){
		this.publicacionForm = fb.group({
			titulo: ['', Validators.required],
			descripcion: ['', Validators.required],
			valorMinimo: ['', Validators.required],
			valorMaximo: ['', Validators.required],
			finalidadVenta: [false],
			precioVenta: [''],
			caracteristicas: this.fb.array([]),
			file: ['', Validators.required],
			file_source: [''],
			tipoProducto: [''],
			idParticular: [''],
		})

		usuarioService.getUserByID(auth.getUserID()).subscribe({
			next: (res: any) => {
				this.hasMPCredentials = !!res && !!res.particularDTO && !!res.particularDTO.accessToken && !!res.particularDTO.publicKey
				this.user = res;
				console.log(this.user);
				
			}
		})
	}

	get getCaracteristicasArray() {
		return <FormArray>this.publicacionForm.get('caracteristicas');
	}

	agregarCaracteristica(caract?: number) {
		const caracteristica = this.fb.group({
			s_descripcion: ['', [Validators.pattern(/[^;]/g), Validators.required]],
		});

		let caracteristicas = this.getCaracteristicasArray;
		caracteristicas.push(caracteristica);
	}

	removeCaracteristica(i: number) {
		let caracteristicas = this.getCaracteristicasArray;
		caracteristicas.removeAt(i);
	}

	onFileChange(event: any) {
		if (event.target.files && event.target.files[0]) {
			var filesAmount = event.target.files.length;
			for (let i = 0; i < filesAmount; i++) {
				var reader = new FileReader();

				reader.onload = (event: any) => {
					this.images.push(event.target.result);

					this.publicacionForm.patchValue({
						file_source: this.images
					});
				}

				reader.readAsDataURL(event.target.files[i]);
				console.log(this.images, this.publicacionForm.controls['file_source']);

			}
		}
	}

	removeImagen(i:number) {
		this.images.splice(i, 1)
		this.publicacionForm.patchValue({
			file_source: this.images
		});
	}

	publicar(){
		this.showErrors = true;
		if(this.publicacionForm.valid){
			this.loadingSave = true;
			let caracteristicas: any[] = this.getCaracteristicasArray.value || [];
			let sendCaracteristicas: string[] = [];
			caracteristicas.forEach(item => {
				sendCaracteristicas.push(item.s_descripcion);
			})
			console.log('PUBLICAR: ', this.publicacionForm.value);
			const body = {
				titulo: this.publicacionForm.controls['titulo'].value,
				descripcion: this.publicacionForm.controls['descripcion'].value,
				imagen: this.publicacionForm.controls['file_source'].value,
				tipoProducto: 'OTROS' ,//TODO: this.publicacionForm.controls['tipoProducto'].value,
				caracteristicasProducto: sendCaracteristicas,
				fechaPublicacion: new Date(),//this.publicacionForm.controls['fechaPublicacion'].value,
				esVenta: this.publicacionForm.controls['finalidadVenta'].value,
				precioVenta: this.publicacionForm.controls['precioVenta'].value,
				valorTruequeMin: this.publicacionForm.controls['valorMinimo'].value,
				valorTruequeMax: this.publicacionForm.controls['valorMaximo'].value,
				//valorMaximoValido: this.publicacionForm.controls['valorMaximoValido'].value,
			}
	
			console.log('body', body);
			
			this.truequeService.crearPublicacion(body).subscribe({
				next: (res: any) => {
					console.log('publicacion creada',res);
					Swal.fire({
						title: '¡Creada!',
						text: 'La publicación se creó correctamente, ya podes hacer un intercambio!',
						icon: 'success'
					}).then(({isConfirmed}) => {
						this.router.navigate(['mis-publicaciones'])
					})
					this.loadingSave = false;
				},
				error: (error) => {
					console.log('error creando publicacion', error);
					this.loadingSave = false;
					//Swal.fire('¡Error!', 'Ocurrió un error al crear la publicación. Intentalo nuevamente', 'error')
				}
			})
		}
	}

	allowVenta(event: any) {
		if(event.checked) this.publicacionForm.controls['precioVenta'].addValidators(Validators.required);
		else this.publicacionForm.controls['precioVenta'].removeValidators(Validators.required)
	}

	credencialesMP() {
		const dialogRef = this.dialog.open(CredencialesMpModalComponent, {
			maxWidth: '60vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal',
			data: {user: this.user, publicKey: this.user?.particularDTO.publicKey, accessToken: this.user?.particularDTO.accessToken}
		});
		dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
			if(result) this.hasMPCredentials = true
		})
	}
}
