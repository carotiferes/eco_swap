import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DonacionModel } from 'src/app/models/donacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { MapComponent } from 'src/app/shared/map/map.component';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-donaciones',
	templateUrl: './donaciones.component.html',
	styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent {

	donaciones: DonacionModel[] = [];
	userData: any;

	buttonsCard: {name: string, icon: string, color: string, status: string, disabled: string}[] = []

	constructor(public dialog: MatDialog, private donacionesService: DonacionesService,
		private auth: AuthService) {
		this.getDonaciones()
	}

	getDonaciones() {
		this.userData = this.auth.getUserData();
		console.log(this.userData);

		this.donacionesService.getAllDonaciones(this.userData.id_particular).subscribe((res: any) => {
			console.log(res);

			this.donaciones = res;
			this.donaciones.map((donacion: any) => {
				if(donacion.idDonacion) donacion['last_status'] = donacion.estado;
				if(donacion.imagenes) donacion.parsedImagenes = donacion.imagenes.split('|')
			})
		})

	}

	zoomImage(img: string) {
		Swal.fire({
			html: `<img src="${img}" style="width: 100%"/>`,
			showConfirmButton: false,
			showCloseButton: true
		})
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

	getImage(image: any) {
		return this.donacionesService.getImagen(image)
	}

}
