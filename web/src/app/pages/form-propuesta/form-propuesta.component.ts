import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
const db = require('../../data/db.json')

@Component({
	selector: 'app-propuesta',
	templateUrl: './form-propuesta.component.html',
	styleUrls: ['./form-propuesta.component.scss']
})
export class FormPropuestaComponent implements OnInit {

	propuestaForm: FormGroup;
	solicitud!: SolicitudModel;
	screenWidth: number;

	fileName: any = 'Subir Imagen';
	images: any[] = [];

	userData: any;

	loading: boolean = true;
	id_solicitud?: string;

	constructor(private fb: FormBuilder, private route: ActivatedRoute,
		private donacionesService: DonacionesService, private auth: AuthService) {

		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})

		this.propuestaForm = fb.group({
			producto: ['', Validators.required],
			caracteristicas: this.fb.array([]),
			file_name: [this.fileName],
			file: [''],
			file_source: [''],
			n_cantidad: ['', Validators.required],
			mensaje: ['']
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.userData = auth.getUserData().userData;
	}

	ngOnInit(): void {
		this.donacionesService.getSolicitudes().subscribe((res: any) => {
			this.solicitud = res.find((item: any) => item.idSolicitud == this.id_solicitud)
			this.loading = false;
		})
	}

	get getCaracteristicasArray() {
		return <FormArray>this.propuestaForm.get('caracteristicas');
	}

	agregarCaracteristica(caract?: number) {
		const caracteristica = this.fb.group({
			s_descripcion: ['', Validators.pattern(/[^;]/g)],
		});

		let caracteristicas = this.getCaracteristicasArray;
		caracteristicas.push(caracteristica);
	}

	confirmarPropuesta() {
		console.log(this.propuestaForm.value);
		let caracteristicas: any[] = this.getCaracteristicasArray.value
		console.log('carac', caracteristicas);
		let stringCaracteristicas = '';
		caracteristicas.forEach(item => {
			stringCaracteristicas += item.s_descripcion + ';'
		})
		const objetoToSend = {
			idPerfilEmisor: this.userData.idUser,
			solicitudProductoModel: {
				tipoProducto: "MUEBLES",
				productoId: this.propuestaForm.controls['producto'].value,
				cantidadOfrecida: this.propuestaForm.controls['n_cantidad'].value,
				mensaje: this.propuestaForm.controls['mensaje'].value,
				caracteristicas: stringCaracteristicas,
				imagenB64: this.propuestaForm.controls['file_source'].value
			}
		}
		console.log(objetoToSend);

		this.donacionesService.crearPropuesta(this.solicitud.idSolicitud, objetoToSend).subscribe(res => {
			console.log(res);

		})
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

	removeImagen(url: string) {
		let imgIndex = this.propuestaForm.controls['file_source'].value.findIndex((item: string) => item == url)
		this.propuestaForm.controls['file_source'].value.splice(imgIndex, 1)
		console.log(url);
		let imgI = this.propuestaForm.controls['file_source'].value.findIndex((item: string) => item == url)
		this.images.splice(imgI, 1)
	}
}
