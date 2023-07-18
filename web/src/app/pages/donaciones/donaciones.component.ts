import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DonacionModel } from 'src/app/models/donacion.model';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { MapComponent } from 'src/app/shared/map/map.component';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-donaciones',
	templateUrl: './donaciones.component.html',
	styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent {

	donaciones: DonacionModel[] = [/* {
		id_propuesta: 1,
		id_producto: 1,
		id_swapper: 1,
		descripcion: 'Frazadas',
		estado: 'NUEVA',
		cantidad_propuesta: 2,
		swapper: {
			id_swapper: 1,
			id_perfil: 1,
			f_nacimiento: new Date(),
			s_nombre: 'Swapper',
			s_apellido: 'Test'
		},
		imagenes: ['assets/test_prop/alim_no_perec.jpg', 'assets/test_prop/gatito1.jpeg']
	}, {
		id_propuesta: 1,
		id_producto: 1,
		id_swapper: 1,
		descripcion: 'Frazadas',
		estado: 'ACEPTADA',
		cantidad_propuesta: 2,
		swapper: {
			id_swapper: 1,
			id_perfil: 1,
			f_nacimiento: new Date(),
			s_nombre: 'Swapper',
			s_apellido: 'Test'
		},
		imagenes: ['']
	} */];

	constructor(public dialog: MatDialog, private donacionesService: DonacionesService){
		// TODO: GET PROPUESTAS FROM BACK

		this.donaciones.map((item:any) => {
			item['last_status'] = item.estado
		})
	}

	zoomImage(img: string){
		Swal.fire({
			html: `<img src="${img}" style="width: 100%"/>`,
			showConfirmButton: false,
			showCloseButton: true
		})
	}

	changeEstadoPropuesta(donacion: DonacionModel, event: any){
		console.log(donacion, event);
		
		this.donaciones.map((item: any) => {
			if(item == donacion) {
				if(event.checked) item.estado = item['last_status'];
				else item.estado = 'CANCELADA'
			}
		})
		console.log(this.donaciones);
		//this.donacionesService.cambiarEstadoPropuesta()
	}

	openDialog() {
		this.dialog.open(MapComponent, {
			maxWidth: '70vw',
			maxHeight: '60vh',
			height: '100%',
			width: '100%',
			panelClass: 'full-screen-modal'
		  });
	  }

}
