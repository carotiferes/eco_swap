import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';

@Component({
	selector: 'app-notificaciones',
	templateUrl: './notificaciones.component.html',
	styleUrls: ['./notificaciones.component.scss']
})
export class NotificacionesComponent {

	notifications: any[] = [{name: 'name', descrip: 'descrip'},{name: 'name', descrip: 'descrip'}];
	paginatedNotifications: any[] = [];
	hoveredItem: any;
	
	@ViewChild(MatPaginator) paginator!: MatPaginator;
	pageSize = 6;

	ngOnInit(): void {
		this.paginatedNotifications = this.notifications.slice(0, this.pageSize);
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedNotifications = this.notifications.slice(startIndex, endIndex);
	}

	access(notificacion: any) {
		// switch with notification type
	}
}
