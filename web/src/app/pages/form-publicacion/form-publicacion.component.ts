import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-form-publicacion',
  templateUrl: './form-publicacion.component.html',
  styleUrls: ['./form-publicacion.component.scss']
})
export class FormPublicacionComponent {

	publicacionForm: FormGroup;
	images: any[] = [];

	loading: boolean = false;

	constructor(private fb: FormBuilder, private truequeService: TruequesService, private router: Router){
		this.publicacionForm = fb.group({
			titulo: ['', Validators.required],
			descripcion: ['', Validators.required],
			valorMinimo: ['', Validators.required],
			valorMaximo: ['', Validators.required],
			finalidadVenta: [false],
			precioVenta: [''],
			caracteristicas: this.fb.array([]),
			file: [''],
			file_source: [''],
			tipoProducto: [''],
			idParticular: [''],
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
		if(this.publicacionForm.valid){
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
				},
				error: (error) => {
					console.log('error creando publicacion', error);
					Swal.fire('¡Error!', 'Ocurrió un error al crear la publicación. Intentalo nuevamente', 'error')
				}
			})
		}
	}
}
