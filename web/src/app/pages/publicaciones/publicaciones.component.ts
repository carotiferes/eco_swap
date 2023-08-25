import { Component } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { ProductosService } from 'src/app/services/productos.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-trueque',
	templateUrl: './publicaciones.component.html',
	styleUrls: ['./publicaciones.component.scss']
})
export class PublicacionesComponent {

	origin: 'all' | 'myPublicaciones' | 'myCompras' = 'all';
	formFiltros: FormGroup;
	tipos_productos: any[] = [];

	loading: boolean = false;

	publicacionesToShow: PublicacionModel[] = [];
	filtros: any;

	constructor(private router: Router, private auth: AuthService, private fb: FormBuilder,
		private productosService: ProductosService, private showErrorService: ShowErrorService,
		private truequesService: TruequesService){

		this.formFiltros = fb.group({
			fundacion: [''],
			codigoPostal: [''],
			tipoProducto: ['']
		})

		if (router.url == '/mis-publicaciones') this.origin = 'myPublicaciones';
		if (router.url == '/mis-compras') this.origin = 'myCompras';

		this.getTiposProductos();
		this.filtrarPublicaciones()
	}

	addPublicacion(){
		this.router.navigate(['form-publicacion'])
		if(this.auth.isUserLoggedIn) {
			this.router.navigate(['form-publicacion'])
		} else {
			Swal.fire({
				title: '¡Necesitás una cuenta!',
				text: 'Para poder crear una publicación, tenés que usar tu cuenta.',
				icon: 'warning',
				confirmButtonText: 'Iniciar sesión',
				showCancelButton: true,
				cancelButtonText: 'Cancelar'
			}).then(({isConfirmed}) => {
				if(isConfirmed) this.router.navigate(['login'])
			})
		}
	}

	getTiposProductos() {
		this.productosService.getTiposProductos().subscribe({
			next: (v: any) => {
				//console.log('productos', v);
				this.tipos_productos = v;
			},
			error: (e) => {
				console.error('error', e);
				this.showErrorService.show('Error!', 'Ha ocurrido un error al traer los tipos de producto')
			},
			//complete: () => console.info('complete')
		});
	}

	filtrarPublicaciones() {
		if(this.origin == 'all'){
			this.filtros = {};
			const codigoPostal = this.formFiltros.controls['codigoPostal'].value;
			const tipoProducto = this.formFiltros.controls['tipoProducto'].value;

			if (codigoPostal) this.filtros['codigoPostal'] = codigoPostal;
			if (tipoProducto) this.filtros['tipoProducto'] = tipoProducto;

			this.truequesService.getPublicaciones(this.filtros).subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
				}
			})
		} else if(this.origin == 'myPublicaciones'){
			this.truequesService.getMisPublicaciones().subscribe({
				next: (data: any) => {
					console.log(data);
					this.publicacionesToShow = data;
					this.publicacionesToShow.map(item => {
						item.parsedImagenes = item.imagenes.split('|')
					})
				}
			})
		} else { // myCompras

		}
	}

	goToPublicacion(publicacion: PublicacionModel){
		this.router.navigate(['publicacion/'+publicacion.idPublicacion])
	}

	limpiarFiltros() {
		this.formFiltros.reset()
		this.filtrarPublicaciones()
	}

	getImagen(img: string) {
		return this.truequesService.getImagen(img)
	}
}
