import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ColectaModel } from 'src/app/models/colecta.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';
import { DateAdapter } from '@angular/material/core';
const TIPOS = ['COLCHONES_Y_FRAZADAS','LIBROS','MUEBLES','OTROS','SALUD','TECNOLOGIA']
const ESTADOS = ['BUEN_ESTADO','ROTO_PERO_UTIL','ROTO']

@Component({
  selector: 'app-form-colecta',
  templateUrl: './form-colecta.component.html',
  styleUrls: ['./form-colecta.component.scss'],
  providers: [
	{provide: MAT_DATE_LOCALE, useValue: 'en-GB'},
  ]
})
export class FormColectaComponent  {

	colectaForm: FormGroup;
	screenWidth: number;
	
	fileName: any = 'Subir Imagen';
	images: any[] = [];

	tipos_productos: string[];
	estados_productos: string[];

	loadingImg: boolean = false;

	constructor(private fb: FormBuilder, private route: ActivatedRoute, private donacionesService: DonacionesService,
		private auth: AuthService, private router: Router, private dateAdapter: DateAdapter<Date>) {

		this.dateAdapter.setLocale('es');
		this.tipos_productos = TIPOS;
		this.estados_productos = ESTADOS;

		this.colectaForm = fb.group({
			s_titulo: ['', Validators.required],
			s_descripcion: [''],
			//id_fundacion: [userData?.id || ''],
			productos: this.fb.array([]),
			file_name: [this.fileName],
			file: [''],
			file_source: [''],
			fecha_inicio: ['', Validators.required],
			fecha_fin: ['', Validators.required]
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	get getProductosArray() {
		return <FormArray>this.colectaForm.get('productos');
	}

	agregarProducto() {
		const producto = this.fb.group({
			s_descripcion: ['', Validators.required],
			n_cantidad_solicitada: [''],
			tipo_producto: ['', Validators.required],
			estado: ['-']
		});

		let productos = this.getProductosArray;
		productos.push(producto);
	}

	crearColecta() {
		console.log(this.colectaForm.value);
		if(this.colectaForm.valid){
			const productos: any[] = [];
			for (const producto of this.getProductosArray.value) {
				productos.push({
					tipoProducto: producto.tipo_producto,
					cantidadRequerida: producto.n_cantidad_solicitada,
					descripcion: producto.s_descripcion,
					estado: producto.estado
				})
			}
			console.log('prdos', productos);
			
			this.donacionesService.crearColecta({
				titulo: this.colectaForm.controls['s_titulo'].value,
				descripcion: this.colectaForm.controls['s_descripcion'].value,
				productos,
				imagen: this.colectaForm.controls['file_source'].value[0],
				fechaInicio: this.colectaForm.controls['fecha_inicio'].value,
				fechaFin: this.colectaForm.controls['fecha_fin'].value
			}).subscribe({
				next: (res) => {
					//console.log(res, JSON.parse(JSON.stringify(res)).descripcion);
					if(JSON.parse(JSON.stringify(res)).descripcion)	{
						this.showMessage('¡Colecta Creada!', 'La colecta se creó exitosamente. Los particulares te contactarán pronto!', 'success')
						this.router.navigateByUrl('mis-colectas')
					}
					else this.showMessage('Ocurrió un error', 'No pudimos crear la colecta. Intentá nuevamente luego.', 'error')
				},
				error: (error) => {
					console.log('error al crear colecta', error);
					this.showMessage('Ocurrió un error', 'No pudimos crear la colecta. Intentá nuevamente luego.', 'error')
				}
			})
		} else {
			this.showMessage('Error en los campos', 'Hay un error en los campos ingresados, por favor revisalos y volvé a intentar.', 'error')
		}
	}

	removeProducto(i: number) {
		let productos = this.getProductosArray;
		productos.removeAt(i);
	}

	onFileChange(event: any) {
		if (event.target.files && event.target.files[0]) {
			var filesAmount = event.target.files.length;
			for (let i = 0; i < filesAmount; i++) {
				var reader = new FileReader();
				reader.onload = (event: any) => {
					this.images.push(event.target.result);
					this.colectaForm.patchValue({
						file_source: this.images
					});
				}
				reader.readAsDataURL(event.target.files[i]);
			}
			this.loadingImg = false;	
		}
	}

	removeImagen(url: string){
		let imgIndex = this.colectaForm.controls['file_source'].value.findIndex((item: string) => item == url)
		this.colectaForm.controls['file_source'].value.splice(imgIndex, 1)
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
