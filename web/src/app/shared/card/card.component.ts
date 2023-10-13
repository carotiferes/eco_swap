import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CardModel } from 'src/app/models/card.model';
import { MainCardPublicacionComponent } from 'src/app/pages/publicacion/main-card-publicacion/main-card-publicacion.component';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-card',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.scss']
})
export class CardComponent {

	@Input() app: 'colectas' | 'donaciones' | 'publicaciones' = 'colectas';
	@Input() cardData?: CardModel; //ColectaModel | DonacionModel | PublicacionModel;

	@Output() statusChanged = new EventEmitter<any>();
	@Output() cardSelected = new EventEmitter<any>();

	isSelected: boolean = false;

	iconMap: { [key: string]: string } = {
		'APROBADO': 'verified', 'APROBADA': 'verified',
		'RECIBIDO': 'add_task', 'RECIBIDA': 'add_task',
		'PENDIENTE': 'pending',
		'ABIERTA': 'lock_open', 'CERRADA': 'lock'
	};
	colorMap: { [key: string]: string } = {
		'APROBADO': 'green', 'APROBADA': 'green',
		'RECIBIDO': 'green', 'RECIBIDA': 'green',
		'PENDIENTE': 'purple',
		'ABIERTA': 'green',
	};

	constructor(private truequesService: TruequesService, private router: Router,
		public dialog: MatDialog, private auth: AuthService, private donacionesService: DonacionesService) { }

	clicked(card: CardModel) {
		switch (card.action) {
			case 'select':
				this.isSelected = true;
				this.cardSelected.emit(card.id);
				break;
			case 'detail':
				this.showDetail(card)
				break;
			default:
				const url = this.app == 'colectas' ? 'colecta/' : (this.app == 'publicaciones' ? 'publicacion/' : 'donacion/')
				this.router.navigate([url + card.id])
				break;
		}
	}

	showDetail(card: CardModel) {
		let component: any;
		let data: any;
		if (this.app == 'publicaciones') {
			component = MainCardPublicacionComponent;
			this.truequesService.getPublicacion(card.id).subscribe({
				next: async (res: any) => {
					data = {
						publicacion: await res,
						userData: {
							isLoggedIn: this.auth.isUserLoggedIn
						},
						userType: 'publicacionOrigen'
					}
					data.publicacion.parsedImagenes = data.publicacion.imagenes.split('|')
					this.openDialog(component, data)
				}
			})
		}
	}

	openDialog(component: any, data: any) {
		const dialogRef = this.dialog.open(component, {
			maxWidth: '80vw',
			maxHeight: '60vh',
			height: 'fit-content',
			width: '100%',
			panelClass: 'full-screen-modal',
			data
		});
		/* dialogRef.afterClosed().subscribe((result) => {
			console.log('closed', result);
		}) */
	}

	changeStatus(card: CardModel, newStatus: string) {
		let title = '';
		let text = '';
		let confirm = '';
		let cancel = '';
		let deny = '';
		let icon: 'success' | 'warning' = 'warning';

		const palabra = this.app == 'publicaciones' ? 'propuesta de trueque' : 'donación';

		switch (newStatus) {
			case 'CANCELADO':
				title = 'Confirmar Cancelación';
				text = '¿Estás seguro/a que querés cancelar esta ' + palabra + '? La acción es irreversible, pero podrás crear otra luego.';
				deny = 'Sí, cancelar';
				cancel = 'No, mantener';
				icon = 'warning';
				break;
			case 'ACEPTADO':
				title = 'Confirmar Aprobación';
				text = 'Confirmá que aceptás la ' + palabra + '. Esta acción es irreversible!' // ya que comenzará con el proceso de envío.';
				confirm = 'Sí, aceptar';
				cancel = 'No, cancelar';
				icon = 'warning';
				break;
			case 'RECHAZADO':
				title = 'Confirmar Rechazo';
				text = '¿Estás seguro/a que querés rechazar esta ' + palabra + '? La acción es irreversible.';
				deny = 'Sí, rechazar';
				cancel = 'No, cancelar';
				icon = 'warning';
				break;
			default:
				title = 'Confirmar Acción';
				text = 'Cambiar el estado de la ' + palabra + ' es irreversible, ¿Estás seguro/a que querés continuar?';
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
				if (this.app == 'publicaciones' && card.idAuxiliar) {
					this.truequesService.cambiarEstadoTrueque(card.idAuxiliar, newStatus).subscribe({
						next: (res: any) => {
							//console.log(res);
							Swal.fire('Se cambió el estado!', 'Se guardó el nuevo estado de la ' + palabra, 'success').then(() => {
								this.statusChanged.emit()
							})
						}
					})
				} else if (card.idAuxiliar) {
					this.donacionesService.cambiarEstadoDonacion(card.idAuxiliar, card.id, {
						nuevoEstado: newStatus
					}).subscribe({
						next: res => {
							//console.log(res);
							Swal.fire('Se cambió el estado!', 'Se guardó el nuevo estado de la ' + palabra, 'success').then(() => {
								this.statusChanged.emit();
							})
						}
					})
				}
			}
		})
	}

	getImagen(img: string) {
		return this.truequesService.getImagen(img)
	}
}
