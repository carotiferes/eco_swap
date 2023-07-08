import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PropuestaModel } from 'src/app/models/propuesta.model';
import { MapComponent } from 'src/app/shared/map/map.component';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-propuestas',
	templateUrl: './propuestas.component.html',
	styleUrls: ['./propuestas.component.scss']
})
export class PropuestasComponent {

	propuestas: PropuestaModel[] = [{
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
	}];

	constructor(public dialog: MatDialog){
		// TODO: GET PROPUESTAS FROM BACK

		this.propuestas.map((item:any) => {
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

	changeEstadoPropuesta(propuesta: PropuestaModel, event: any){
		console.log(propuesta, event);
		
		this.propuestas.map((item: any) => {
			if(item == propuesta) {
				if(event.checked) item.estado = item['last_status'];
				else item.estado = 'CANCELADA'
			}
		})
		console.log(this.propuestas);
		
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
