import { Component } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { ProductosService } from 'src/app/services/productos.service';
import { ShowErrorService } from 'src/app/services/show-error.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-trueque',
	templateUrl: './publicaciones.component.html',
	styleUrls: ['./publicaciones.component.scss']
})
export class PublicacionesComponent {

	origin: 'all' | 'myPublicaciones' | 'myVentas' = 'all';
	userData: any;
	formFiltros: FormGroup;
	tipos_productos: any[] = [];

	loading: boolean = false;

	publicacionesToShow: PublicacionModel[] = [{
		idPublicacion: 1,
		titulo: 'titulo',
		descripcion: 'descripcion',
		estado: 'estado',
		particular: {
			idParticular: 1,
			usuario: 1,
			nombre: 'nombre',
			apellido: 'apellido',
			dni: 'dni',
			cuil: 'cuil',
			fechaNacimiento: new Date(),
			tipoDocumento: 'tipoDocumento',
			publicaciones: [],
			donaciones: []
		},
		fechaPublicacion: new Date(),
		imagenes: 'assets/TEST_abrigos.jpg',
		tipoPublicacion: 'VENTA',
		precioVenta: 1000,
		valorTruequeMin: 500,
		valorTruequeMax: 700,
	}];

	constructor(private router: Router, private auth: AuthService, private fb: FormBuilder,
		private productosService: ProductosService, private showErrorService: ShowErrorService){
		this.userData = auth.getUserData();

		this.formFiltros = fb.group({
			fundacion: [''],
			codigoPostal: [''],
			tipoProducto: ['']
		})
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
				console.log('productos', v);
				this.tipos_productos = v;
			},
			error: (e) => {
				console.error('error', e);
				this.showErrorService.show('Error!', 'Ha ocurrido un error al traer los tipos de producto')
			},
			complete: () => console.info('complete')
		});
	}

	filtrarPublicaciones() {
		
	}

	goToPublicacion(publicacion: PublicacionModel){
		this.router.navigate(['publicacion/'+publicacion.idPublicacion])
	}

	limpiarFiltros() {
		this.formFiltros.reset()
		this.filtrarPublicaciones()
	}
}
