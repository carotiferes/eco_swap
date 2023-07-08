import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { DonacionesService } from 'src/app/services/donaciones.service';
const db = require('../../data/db.json')

@Component({
	selector: 'app-propuesta',
	templateUrl: './form-propuesta.component.html',
	styleUrls: ['./form-propuesta.component.scss']
})
export class FormPropuestaComponent {

	propuestaForm: FormGroup;
	solicitud!: SolicitudModel;
	screenWidth: number;
	
	fileName: any = 'Subir Imagen';
	images: any[] = [];

	constructor(private fb: FormBuilder, private route: ActivatedRoute, private donacionesService: DonacionesService) {

		let id_solicitud: string;
		route.paramMap.subscribe(params => {
			console.log(params);
			id_solicitud = params.get('id_solicitud') || '';
		})

		this.propuestaForm = fb.group({
			producto: ['', Validators.required],
			caracteristicas: this.fb.array([]),
			file_name: [this.fileName],
			file: [''],
			file_source: [''],
			n_cantidad: ['', Validators.required]
		})

		donacionesService.getSolicitudes().subscribe((res: any) => {
			this.solicitud = res.find((item: any) => item.idSolicitud == id_solicitud)
		})
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
