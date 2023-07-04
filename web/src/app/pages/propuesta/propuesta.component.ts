import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SolicitudModel } from 'src/app/models/solicitud.model';
const db = require('../../data/db.json')

@Component({
	selector: 'app-propuesta',
	templateUrl: './propuesta.component.html',
	styleUrls: ['./propuesta.component.scss']
})
export class PropuestaComponent {

	propuestaForm: FormGroup;
	solicitud: SolicitudModel;
	screenWidth: number;
	
	//imgToShow: string | ArrayBuffer | null | undefined;
	fileName: any = 'Subir Imagen';
	images: any[] = [];

	constructor(private fb: FormBuilder, private route: ActivatedRoute,) {

		let id_solicitud: string;
		let producto: any;
		route.paramMap.subscribe(params => {
			console.log(params);
			id_solicitud = params.get('id_solicitud') || '';
		})
		/* route.queryParamMap.subscribe(params => {
			producto = params.get('id')
			console.log('prod', producto);
		}) */

		this.propuestaForm = fb.group({
			producto: [''],
			caracteristicas: this.fb.array([]),
			file_name: [this.fileName],
			file: [''],
			file_source: [''],
			n_cantidad: ['']
		})
		//this.propuestaForm.controls['producto'].setValue(producto)
		this.solicitud = db.solicitudes.find((item: SolicitudModel) => item.id_solicitud.toString() == id_solicitud)

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	get getCaracteristicasArray() {
		return <FormArray>this.propuestaForm.get('caracteristicas');
	}

	agregarCaracteristica(caract?: number) {
		const caracteristica = this.fb.group({
			s_descripcion: [''],
		});

		let caracteristicas = this.getCaracteristicasArray;
		caracteristicas.push(caracteristica);
	}

	confirmarPropuesta() {
		console.log(this.propuestaForm.value);

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

					this.propuestaForm.patchValue({
						file_source: this.images
					});
				}

				reader.readAsDataURL(event.target.files[i]);
				console.log(this.images, this.propuestaForm.controls['file_source']);
				
			}
		}
	}

	removeImagen(url: string){
		let imgIndex = this.propuestaForm.controls['file_source'].value.findIndex((item: string) => item == url)
		this.propuestaForm.controls['file_source'].value.splice(imgIndex, 1)
		console.log(url);
		let imgI = this.propuestaForm.controls['file_source'].value.findIndex((item: string) => item == url)
		this.images.splice(imgI, 1)
	}
}
