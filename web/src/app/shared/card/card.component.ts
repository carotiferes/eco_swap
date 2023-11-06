import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CardModel } from 'src/app/models/card.model';
import { MainCardPublicacionComponent } from 'src/app/pages/publicacion/main-card-publicacion/main-card-publicacion.component';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { TruequesService } from 'src/app/services/trueques.service';
import Swal from 'sweetalert2';
import { ListComponent } from '../list/list.component';
import { EnvioModalComponent } from 'src/app/shared/envio-modal/envio-modal.component';

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
	@Output() cardUnselected = new EventEmitter<any>();
	@Output() modalClosed = new EventEmitter<any>();

	iconMap: { [key: string]: string } = {
		'APROBADO': 'verified', 'APROBADA': 'verified',
		'RECIBIDO': 'add_task', 'RECIBIDA': 'add_task',
		'PENDIENTE': 'pending', 'EN_ESPERA': 'schedule',
		'ABIERTA': 'lock_open', 'CERRADA': 'lock'
	};
	colorMap: { [key: string]: string } = {
		'APROBADO': 'green', 'APROBADA': 'green',
		'RECIBIDO': 'green', 'RECIBIDA': 'green',
		'PENDIENTE': 'purple',
		'ABIERTA': 'green', 'EN_ESPERA': 'purple'
	};

	isSwapper: boolean = false;

	constructor(private truequesService: TruequesService, private router: Router,
		public dialog: MatDialog, private auth: AuthService, private donacionesService: DonacionesService) {
			this.isSwapper = auth.isUserSwapper()
		}

	clicked(card: CardModel) {
		switch (card.action) {
			case 'select':
				if(this.cardData){
					this.cardData.isSelected = !this.cardData.isSelected;
					if(this.cardData.isSelected) this.cardSelected.emit(card.id);
					else this.cardUnselected.emit(card.id);
				}
				break;
			case 'detail':
				this.showDetail(card)
				break;
			case 'trueque':
				this.router.navigate(['publicacion/' + card.idAuxiliar])
				break;
			default:
				const url = this.app == 'colectas' ? 'colecta/' : (this.app == 'publicaciones' ? 'publicacion/' : 'colecta/')
				const id = this.app == 'colectas' || this.app == 'publicaciones' ? card.id : card.idAuxiliar
				this.router.navigate([url + id])
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
					this.openDialog(component, data, '80vh')
				}
			})
		}
	}


	openDialog(component: any, data: any, height: string = '60vh', width: string = '80vw') {
		const dialogRef = this.dialog.open(component, {
			maxWidth: width,
			maxHeight: height,
			width: '100%',
			panelClass: 'full-screen-modal',
			data
		});
		dialogRef.afterClosed().subscribe((result) => {
			this.modalClosed.emit({result, componente: component.name})
		})
	}

	buttonClicked(card: CardModel, button: any) {
		if(button.status == 'INFO') {
			if(card.action == 'list') this.openDialog(ListComponent, card);
			else if(card.action == 'trueque') this.router.navigate(['publicacion/' + card.idAuxiliar])
			else if(card.codigo == 'Compra' || this.app == 'donaciones' && button.name == 'Configurar envío') this.openDialog(EnvioModalComponent, {card}, '70vh', '60vw')
			else if(button.name == 'Ver envío') this.openDialog(EnvioModalComponent, {cards: [card]}, '70vh', '60vw')
			else if(card.action == 'select' && card.codigo == 'Donación'){
				this.clicked(card)
			}
		} else {
			this.changeStatus(card, button.status)
		}
	}

	changeStatus(card: CardModel, newStatus: string) {
		let title = '';
		let text = '';
		let confirm = '';
		let cancel = '';
		let deny = '';
		let icon: 'success' | 'warning' = 'warning';

		const palabra = card.codigo ? card.codigo.toLowerCase() : this.app == 'publicaciones' ? 'propuesta de trueque' : 'donación';

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
			case 'EN_ESPERA':
				title = 'Confirmar Envío en Persona';
				text = 'Esto significa que vos tenés que llevar la donación a la fundación. Podés ver la dirección ingresando a su perfil.';
				deny = 'Sí, confirmar';
				cancel = 'No, cancelar';
				icon = 'warning';
				break;
			case 'CERRADA':
					title = 'Confirmar Cierre';
					text = '¿Estás seguro/a que querés cerrar esta ' + palabra + '? Se cancelarán todos sus trueques pendientes. Esta acción es irreversible.';
					deny = 'Sí, confirmar';
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
				if(newStatus == 'CERRADA') {
					this.truequesService.cerrarPublicacion(card.id).subscribe({
						next: (res: any) => {
							//console.log(res);
							Swal.fire('Se cambió el estado!', 'Se guardó el nuevo estado de la ' + palabra, 'success').then(() => {
								this.statusChanged.emit()
							})
						}
					})
				} else if (this.app == 'publicaciones' && card.idAuxiliar) {
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
