import { Component, OnInit } from '@angular/core';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-publicacion',
	templateUrl: './publicacion.component.html',
	styleUrls: ['./publicacion.component.scss']
})
export class PublicacionComponent implements OnInit {

	loading: boolean = false;
	publicacion: PublicacionModel;

	constructor(){
		// TODO: GET PUBLICACION FROM BACKEND
		this.publicacion = {
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
			imagenes: 'assets/TEST_abrigos.jpg|assets/TEST_juguetes.png',
			tipoPublicacion: 'VENTA',
			precioVenta: 1000,
			valorTruequeMin: 500,
			valorTruequeMax: 700,
		}
	}

	ngOnInit() {
		this.publicacion.parsedImagenes = this.publicacion.imagenes.split('|')
		
	}

	intercambiar() {

	}

	comprar() {

	}

	getImage(image: any) {
		return image;
		//TODO: GET IMAGE FROM PUBLICACION SERVICE
		//return this.donacionesService.getImagen(image)
	}

	zoomImage(img?: string){
		if(img){
			Swal.fire({
				html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
				showConfirmButton: false,
				showCloseButton: true
			})
		}
	}
}
