import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

const TIPOS = ['COLCHONES_Y_FRAZADAS','LIBROS','MUEBLES','OTROS','SALUD','TECNOLOGIA']
const ESTADOS = ['BUEN_ESTADO','ROTO_PERO_UTIL','ROTO']

@Component({
  selector: 'app-form-solicitud',
  templateUrl: './form-solicitud.component.html',
  styleUrls: ['./form-solicitud.component.scss']
})
export class FormSolicitudComponent {

	solicitudForm: FormGroup;
	screenWidth: number;
	
	fileName: any = 'Subir Imagen';
	images: any[] = [];

	tipos_productos: string[];
	estados_productos: string[];

	loadingImg: boolean = false;

	constructor(private fb: FormBuilder, private route: ActivatedRoute, private donacionesService: DonacionesService,
		private auth: AuthService, private router: Router) {

		this.tipos_productos = TIPOS;
		this.estados_productos = ESTADOS;

		let userData = auth.getUserData().userData;

		this.solicitudForm = fb.group({
			s_titulo: ['', Validators.required],
			s_descripcion: [''],
			id_fundacion: [userData.idUser || ''],
			productos: this.fb.array([]),
			file_name: [this.fileName],
			file: [''],
			file_source: [''],
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
	}

	get getProductosArray() {
		return <FormArray>this.solicitudForm.get('productos');
	}

	agregarProducto(caract?: number) {
		const producto = this.fb.group({
			s_descripcion: [''],
			n_cantidad_solicitada: [''],
			tipo_producto: [''],
		});

		let productos = this.getProductosArray;
		productos.push(producto);
	}

	test(){
		console.log(this.getProductosArray.value);
		
	}

	crearSolicitud() {
		console.log(this.solicitudForm.value);
		const productos: any[] = [];
		for (const producto of this.getProductosArray.value) {
			console.log('PROD', producto);
			
			productos.push({
				tipoProducto: producto.tipo_producto,
				cantidadRequerida: producto.n_cantidad_solicitada,
				descripcion: producto.s_descripcion,
			})
		}
		console.log('prdos', productos);
		
		this.donacionesService.crearSolicitud({
			titulo: this.solicitudForm.controls['s_titulo'].value,
			descripcion: this.solicitudForm.controls['s_descripcion'].value,
			idFundacion: this.solicitudForm.controls['id_fundacion'].value,
			productos,
			imagen: this.solicitudForm.controls['file_source'].value[0]
		}).subscribe((res) => {
			//console.log(res, JSON.parse(JSON.stringify(res)).descripcion);
			if(JSON.parse(JSON.stringify(res)).descripcion)	{
				this.showMessage('¡Solicitud Creada!', 'La solicitud se creó exitosamente. Los Swappers te contactarán pronto!', 'success')
				this.router.navigateByUrl('donaciones/'+ this.solicitudForm.controls['id_fundacion'].value)
			}
			else this.showMessage('Ocurrió un error', 'No pudimos crear la solicitud. Intentá nuevamente luego.', 'error')
		})
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

					this.solicitudForm.patchValue({
						file_source: this.images
					});
				}

				reader.readAsDataURL(event.target.files[i]);
				console.log(this.images, this.solicitudForm.controls['file_source']);
				
			}
			this.loadingImg = false;	
		}
	}

	removeImagen(url: string){
		let imgIndex = this.solicitudForm.controls['file_source'].value.findIndex((item: string) => item == url)
		this.solicitudForm.controls['file_source'].value.splice(imgIndex, 1)
		console.log(url);
		
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
