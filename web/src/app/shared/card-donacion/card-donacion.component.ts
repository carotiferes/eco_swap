import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ColectaModel } from 'src/app/models/colecta.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-card-donacion',
  templateUrl: './card-donacion.component.html',
  styleUrls: ['./card-donacion.component.scss']
})
export class CardDonacionComponent implements OnInit{

	@Input() donacion!: DonacionModel;
	@Input() id_colecta!: number;
	@Input() usuarioHeader: any;
	@Input() buttons: {name: string, icon: string, color: string, status: string, disabled: boolean}[] = [];

	@Output() statusChanged = new EventEmitter<any>();

	userData: any
	caracteristicas: string = '';

	constructor(private donacionesService: DonacionesService){	}
	
	ngOnInit(): void {
		//console.log(this.donacion, this.usuarioHeader,this.donacion.caracteristicaDonacion);
		for (const [i, caract] of this.donacion.caracteristicaDonacion.entries()) {
			if(caract.caracteristica && i == 0) this.caracteristicas = caract.caracteristica
			else if(caract.caracteristica) this.caracteristicas += ' - '+caract.caracteristica
		}
	}

	getImage(image: any) {
		return this.donacionesService.getImagen(image)
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

	changeEstadoDonacion(donacion: DonacionModel, status: string) {
		let title = '';
		let text = '';
		let confirm = '';
		let cancel = '';
		let deny = '';
		let icon: 'success' | 'warning' = 'warning';

		switch (status) {
			case 'CANCELADA':
				title = 'Confirmar Cancelación';
				text = '¿Estás seguro/a que querés cancelar esta donación? La acción es irreversible, pero podrás crear otra donación luego.';
				deny = 'Sí, cancelar donación';
				cancel = 'No, mantener donación';
				icon = 'warning';
				break;
			case 'ACEPTADA':
				title = 'Confirmar Aprobación';
				text = 'Confirmá que aceptás la donación. Esta acción es irreversible ya que comenzará con el proceso de envío.';
				confirm = 'Sí, aceptar donación';
				cancel = 'No, cancelar';
				icon = 'warning';
				break;
			case 'RECHAZADA':
				title = 'Confirmar Rechazo';
				text = '¿Estás seguro/a que querés rechazar esta donación? La acción es irreversible.';
				deny = 'Sí, rechazar donación';
				cancel = 'No, cancelar';
				icon = 'warning';
				break;
			default:
				title = 'Confirmar Acción';
				text = 'Cambiar el estado de la donación es irreversible, ¿Estás seguro/a que querés continuar?';
				confirm = 'Sí, continuar';
				cancel = 'No, cancelar';
				icon = 'warning';
				break;
		}
		Swal.fire({
			title,
			text,
			showConfirmButton: confirm != '',
			confirmButtonText: confirm,
			showDenyButton: deny != '',
			denyButtonText: deny,
			showCancelButton: cancel != '',
			cancelButtonText: cancel,
			icon,
			reverseButtons: true
		}).then(({ isConfirmed, isDenied }) => {
			if (isConfirmed || isDenied) {
				this.donacionesService.cambiarEstadoDonacion(this.id_colecta, donacion.idDonacion, {
					nuevoEstado: status
				}).subscribe({
					next: res => {
						console.log(res);
						this.donacion.estadoDonacion = status;
						this.statusChanged.emit();
					}
				})
			}
		})
	}

	getStars(puntaje: number){
		const array: any = [];
		for (let index = 0; index < puntaje; index++) {
			array.push(1)
		}
		return array;
	}
}
