import { Component, EventEmitter, Inject, Input, Optional, Output } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PublicacionModel } from 'src/app/models/publicacion.model';
import { LogisticaService } from 'src/app/services/logistica.service';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-main-card-publicacion',
  templateUrl: './main-card-publicacion.component.html',
  styleUrls: ['./main-card-publicacion.component.scss']
})
export class MainCardPublicacionComponent {

	@Input() publicacion?: PublicacionModel;
	@Input() userData: any;
	@Input() userType: 'notLoggedIn' | 'publicacionOrigen' | 'publicacionPropuesta' = 'notLoggedIn';

	@Output() intercambiarEvent = new EventEmitter<any>();
	@Output() comprarEvent = new EventEmitter<any>();

	constructor(private truequeService: TruequesService, @Optional() @Inject(MAT_DIALOG_DATA) public data: any,
	private logisticaService: LogisticaService
	){
		console.log(this.publicacion, data);
		if(data) {
			this.publicacion = data.publicacion
			this.userData = data.userData
			this.userType = data.userType
		}
	}

	getImage(image: any) {
		return this.truequeService.getImagen(image)
	}

	zoomImage(img?: string) {
		if (img) {
			Swal.fire({
				html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
				showConfirmButton: false,
				showCloseButton: true
			})
		}
	}

	intercambiar() {
		this.intercambiarEvent.emit()
	}

	comprar() {
		this.comprarEvent.emit()
	}

	getCostoEnvio() {
		this.logisticaService.getCostoEnvio().subscribe({
			next: (res: any) => {
				console.log(res);
				
			}
		})
	}
}
