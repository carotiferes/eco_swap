import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-propuesta',
	templateUrl: './form-propuesta.component.html',
	styleUrls: ['./form-propuesta.component.scss']
})
export class FormPropuestaComponent implements OnInit {

	propuestaForm: FormGroup;
	solicitud!: SolicitudModel;
	screenWidth: number;

	images: any[] = [];

	userData: any;

	loading: boolean = true;
	id_solicitud?: string;

	constructor(private fb: FormBuilder, private route: ActivatedRoute, private router: Router,
		private donacionesService: DonacionesService, private auth: AuthService) {

		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_solicitud = params.get('id_solicitud') || '';
		})

		this.propuestaForm = fb.group({
			producto: ['', Validators.required],
			caracteristicas: this.fb.array([]),
			file: [''],
			file_source: ['', Validators.required],
			n_cantidad: ['', Validators.required],
			mensaje: ['']
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.userData = auth.getUserData();
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
			s_descripcion: ['', [Validators.pattern(/[^;]/g), Validators.required]],
		});

		let caracteristicas = this.getCaracteristicasArray;
		caracteristicas.push(caracteristica);
	}

	confirmarPropuesta() {
		if(this.propuestaForm.valid){
			let caracteristicas: any[] = this.getCaracteristicasArray.value || [];
			let sendCaracteristicas: string[] = [];
			caracteristicas.forEach(item => {
				sendCaracteristicas.push(item.s_descripcion);
			})
			const objetoToSend = {
				idSwapper: this.userData.id_swapper,
				solicitudProductoModel: {
					tipoProducto: "MUEBLES",
					productoId: this.propuestaForm.controls['producto'].value,
					cantidadOfrecida: this.propuestaForm.controls['n_cantidad'].value,
					mensaje: this.propuestaForm.controls['mensaje'].value,
					caracteristicas: sendCaracteristicas,
					imagenes: this.propuestaForm.controls['file_source'].value
				}
			}
			console.log(objetoToSend);
	
			this.donacionesService.crearPropuesta(this.solicitud.idSolicitud, objetoToSend).subscribe(res => {
				console.log(res);
				if(JSON.parse(JSON.stringify(res)).descripcion)	{
					this.showMessage('Propuesta Creada!', 'La propuesta se creó exitosamente. Ahora te toca a vos! Llevá tu donación a la fundación para que la puedan empezar a usar.', 'success')
					this.router.navigateByUrl('solicitud/'+ this.id_solicitud)
				}
				else this.showMessage('Ocurrió un error', 'No pudimos crear la propuesta. Intentá nuevamente luego.', 'error')
			})
		} else this.showMessage('Error en los campos.', 'Revisá los campos y completalos correctamente.', 'error')
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

	removeImagen(i:number) {
		this.images.splice(i, 1)
		this.propuestaForm.patchValue({
			file_source: this.images
		});
	}

	showMessage(title: string, text: string, icon:'warning'| 'error'| 'success'| 'info'| 'question'){
		Swal.fire({
			title,
			text,
			icon,
			confirmButtonText: '¡OK!'
		})
	}
}
