import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CardModel } from 'src/app/models/card.model';
import { MainCardPublicacionComponent } from 'src/app/pages/publicacion/main-card-publicacion/main-card-publicacion.component';
import { AuthService } from 'src/app/services/auth.service';
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
	
	isSelected: boolean = false;

	constructor(private truequesService: TruequesService, private router: Router,
		public dialog: MatDialog, private auth: AuthService) { }

	clicked(card: CardModel) {
		switch (card.action) {
			case 'select':
				this.isSelected = true;
				break;
			case 'detail':
				this.showDetail(card)
				break;
			default:
				const url = this.app == 'colectas' ? 'colecta/' : (this.app == 'publicaciones' ? 'publicacion/' : 'donacion/')
				this.router.navigate([url+card.id])
				break;
		}
	}

	showDetail(card: CardModel) {
		let component: any;
		let data: any;
		if(this.app == 'publicaciones') {
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

			switch (newStatus) {
				case 'CANCELADO':
					title = 'Confirmar Cancelación';
					text = '¿Estás seguro/a que querés cancelar esta donación? La acción es irreversible, pero podrás crear otra donación luego.';
					deny = 'Sí, cancelar donación';
					cancel = 'No, mantener donación';
					icon = 'warning';
					break;
				case 'ACEPTADO':
					title = 'Confirmar Aprobación';
					text = 'Confirmá que aceptás la donación. Esta acción es irreversible ya que comenzará con el proceso de envío.';
					confirm = 'Sí, aceptar donación';
					cancel = 'No, cancelar';
					icon = 'warning';
					break;
				case 'RECHAZADO':
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
					if(this.app == 'publicaciones') {
						this.truequesService.cambiarEstadoTrueque(card.id, newStatus).subscribe({
							next: (res: any) => {
								console.log(res);
								this.statusChanged.emit()
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
