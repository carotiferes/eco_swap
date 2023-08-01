import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-form-publicacion',
  templateUrl: './form-publicacion.component.html',
  styleUrls: ['./form-publicacion.component.scss']
})
export class FormPublicacionComponent {

	publicacionForm: FormGroup;
	images: any[] = [];

	loading: boolean = false;

	constructor(private fb: FormBuilder){
		this.publicacionForm = fb.group({
			titulo: ['', Validators.required],
			descripcion: ['', Validators.required],
			idParticular: [''],
			file: [''],
			file_source: [''],
			tipoProducto: [''],
			caracteristicas: this.fb.array([]),
			finalidad: [''],
			valorMinimo: [''],
			valorMaximo: [''],
			precioVenta: ['']
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
		console.log('PUBLICAR: ', this.publicacionForm.value);
		
	}
}
