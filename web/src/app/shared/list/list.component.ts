import { Component, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TruequesService } from 'src/app/services/trueques.service';

@Component({
	selector: 'app-list',
	templateUrl: './list.component.html',
	styleUrls: ['./list.component.scss']
})
export class ListComponent {
	@Input() list: {id: string, title: string, description: string, img?: string, estado: string}[] = []
	paginatedList: {id: string, title: string, description: string, img?: string, estado: string}[] = []
	@Input() title: string = 'Propuestas';

	hoveredItem: any;
	pageSize = 10;

	iconMap: { [key: string]: string } = {
		'APROBADO': 'verified', 'APROBADA': 'verified',
		'RECIBIDO': 'add_task', 'RECIBIDA': 'add_task',
		'PENDIENTE': 'pending',
		'ABIERTA': 'lock_open', 'CERRADA': 'lock'
	};
	colorMap: { [key: string]: string } = {
		'APROBADO': 'var(--success)', 'APROBADA': 'var(--success)',
		'RECIBIDO': 'var(--success)', 'RECIBIDA': 'var(--success)',
		'PENDIENTE': 'purple',
		'ABIERTA': 'var(--success)',
	};

	constructor(public dialogRef: MatDialogRef<ListComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
	private truequesService: TruequesService, private router: Router) {}

	ngOnInit(): void {
		console.log(this.data);
		if(this.data.action && this.data.action == 'list') this.getPropuestas(this.data.id)
		this.paginatedList = this.list.slice(0, this.pageSize);
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedList = this.list.slice(startIndex, endIndex);
	}

	getPropuestas(idPublicacionPropuesta: number) {
		this.truequesService.getPropuestasFromPublicacion(idPublicacionPropuesta).subscribe({
			next: (res: any) => {
				console.log(res);
				const auxList = res || [];
				console.log(auxList);
				
				for (const item of auxList) {
					this.list.push({
						id: item.publicacionDTOorigen.idPublicacion,
						title: item.publicacionDTOorigen.titulo,
						description: item.publicacionDTOorigen.descripcion,
						img: this.getImagen(item.publicacionDTOorigen.imagenes.split('|')[0]),
						estado: item.estadoTrueque
					})
					
				}
		this.paginatedList = this.list.slice(0, this.pageSize);

			}
		})
	}

	access(item: any) {
		console.log('access',item);
		this.dialogRef.close()
		this.router.navigate(['publicacion/'+item.id])
	}

	getImagen(img: string) {
		return this.truequesService.getImagen(img)
	}
}
