import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-donacion',
	templateUrl: './form-donacion.component.html',
	styleUrls: ['./form-donacion.component.scss']
})
export class FormDonacionComponent implements OnInit {

	donacionForm: FormGroup;
	colecta!: ColectaModel;
	screenWidth: number;

	images: any[] = [];

	loading: boolean = true;
	id_colecta?: string;

	constructor(private fb: FormBuilder, private route: ActivatedRoute, private router: Router,
		private donacionesService: DonacionesService, private auth: AuthService) {

		route.paramMap.subscribe(params => {
			console.log(params);
			this.id_colecta = params.get('id_colecta') || '';
		})

		this.donacionForm = fb.group({
			producto: ['', Validators.required],
			caracteristicas: this.fb.array([]),
			file: [''],
			file_source: ['', Validators.required],
			n_cantidad: ['', Validators.required],
			mensaje: ['']
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	ngOnInit(): void {
		this.donacionesService.getColecta(this.id_colecta).subscribe({
			next: (res: any) => {
				this.colecta = res;
				this.loading = false;
			},
			error: (error) => {
				console.log(error);
				
			}
		})
	}

	get getCaracteristicasArray() {
		return <FormArray>this.donacionForm.get('caracteristicas');
	}

	agregarCaracteristica(caract?: number) {
		const caracteristica = this.fb.group({
			s_descripcion: ['', [Validators.pattern(/[^;]/g), Validators.required]],
		});

		let caracteristicas = this.getCaracteristicasArray;
		caracteristicas.push(caracteristica);
	}

	confirmarDonacion() {
		console.log(this.donacionForm.value);
		
		if(this.donacionForm.valid){
			let caracteristicas: any[] = this.getCaracteristicasArray.value || [];
			let sendCaracteristicas: string[] = [];
			caracteristicas.forEach(item => {
				sendCaracteristicas.push(item.s_descripcion);
			})
			const objetoToSend = {
				tipoProducto: "MUEBLES",
				productoId: this.donacionForm.controls['producto'].value,
				cantidadOfrecida: this.donacionForm.controls['n_cantidad'].value,
				mensaje: this.donacionForm.controls['mensaje'].value,
				caracteristicas: sendCaracteristicas,
				imagenes: this.donacionForm.controls['file_source'].value
			}
			console.log(objetoToSend);
	
			this.donacionesService.crearDonacion(this.colecta.idColecta, objetoToSend).subscribe({
				next: (res) => {
					console.log(res);
					if(JSON.parse(JSON.stringify(res)).descripcion)	{
						this.showMessage('Donación Creada!', 'La donacion se creó exitosamente. Ahora te toca a vos! Llevá tu donación a la fundación para que la puedan empezar a usar.', 'success')
						this.router.navigateByUrl('colecta/'+ this.id_colecta)
					}
					else this.showMessage('Ocurrió un error', 'No pudimos crear la donacion. Intentá nuevamente luego.', 'error')
				},
				error: (error) =>{
					console.log('error creando donacion', error);
					this.showMessage('Ocurrió un error', 'No pudimos crear la donacion. Intentá nuevamente luego.', 'error')
				}
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
					this.donacionForm.patchValue({
						file_source: this.images
					});
				}
				reader.readAsDataURL(event.target.files[i]);
			}
		}
	}

	removeImagen(i:number) {
		this.images.splice(i, 1)
		this.donacionForm.patchValue({
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
