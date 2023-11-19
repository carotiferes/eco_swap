import { Component, ViewChild } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatPaginator } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { NotificacionModel } from 'src/app/models/notificacion.model';
import { NotificacionesService } from 'src/app/services/notificaciones.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-notificaciones',
	templateUrl: './notificaciones.component.html',
	styleUrls: ['./notificaciones.component.scss']
})
export class NotificacionesComponent {

	notifications: NotificacionModel[] = [];
	selectedNotifications: number[] = [];
	paginatedNotifications: NotificacionModel[] = [];
	hoveredItem: any;
	
	@ViewChild(MatPaginator) paginator!: MatPaginator;
	pageSize = 10;

	loading: boolean = true;
	loadingSave:  boolean = false;

	refreshHeader: number = 0;

	constructor(private notificacionesService: NotificacionesService, private router: Router) {}

	ngOnInit(): void {
		this.getNotificaciones()
	}
	
	getNotificaciones() {
		this.notificacionesService.getMisNotificaciones().subscribe({
			next: (res: any) => {
				this.notifications = res;
				this.notifications.sort((a, b) => {
					if (a.estadoNotificacion === 'LEIDO' && b.estadoNotificacion !== 'LEIDO') {
						return 1; // 'a' comes before 'b'
					} else if (a.estadoNotificacion !== 'LEIDO' && b.estadoNotificacion === 'LEIDO') {
						return -1; // 'b' comes before 'a'
					} else {
						return 0; // No change in order
					}
				});
				this.paginatedNotifications = this.notifications.slice(0, this.pageSize)
				this.loading = false;
			}
		})
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedNotifications = this.notifications.slice(startIndex, endIndex);
	}

	access(notificacion: NotificacionModel) {
		this.notificacionesService.leerNotificaciones([notificacion.idNotificacion]).subscribe({
			next: (res: any) => { this.refreshHeader++; }
		})
		switch (notificacion.tipoNotificacion) {
			case 'DONACION': // NUEVA DONACION --> VA A LA COLECTA
				this.router.navigate(['colecta', notificacion.idReferenciaNotificacion])
				break;
			case 'TRUEQUE': // NUEVO TRUEQUE --> VA A LA PUBLICACION ORIGEN
				this.router.navigate(['publicacion', notificacion.idReferenciaNotificacion])
				break;
			case 'NUEVO_ESTADO_DONACION': // CAMBIA ESTADO DONACION --> VA A LA COLECTA CORRESPONDIENTE
				this.router.navigate(['colecta', notificacion.idReferenciaNotificacion])
				break;
			case 'NUEVO_ESTADO_TRUEQUE': // CAMBIA ESTADO TRUEQUE --> VA A LA PUBLICACION ORIGEN
				this.router.navigate(['publicacion', notificacion.idReferenciaNotificacion])
				break;
			case 'NUEVO_ESTADO_ORDEN_ENVIO': // CAMBIA ESTADO ENVIO
				if(notificacion.titulo.includes('donación')) {
					this.router.navigate(['mis-donaciones'])
				} else {
					this.router.navigate(['mis-compras'])
				}
				break;
			default:
				break;
		}
	}

	selectNotification(event: MatCheckboxChange, notificacion: NotificacionModel) {
		if(event.checked) {
			this.selectedNotifications.push(notificacion.idNotificacion)
		} else {
			this.selectedNotifications = this.selectedNotifications.filter(item => item != notificacion.idNotificacion)
		}
	}

	leerNotificaciones() {
		this.loadingSave = true;
		this.notificacionesService.leerNotificaciones(this.selectedNotifications).subscribe({
			next: (res: any) => {
				Swal.fire('¡Excelente!', 'Las notificaciones seleccionadas se marcaron como leidas.', 'success');
				this.selectedNotifications = [];
				this.getNotificaciones();
				this.loadingSave = false;
				this.refreshHeader++;
			}
		})
	}
}
