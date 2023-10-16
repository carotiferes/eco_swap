import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-opinar-modal',
	templateUrl: './opinar-modal.component.html',
	styleUrls: ['./opinar-modal.component.scss']
})
export class OpinarModalComponent {
	loading: boolean = false;
	descripcion: string = '';
	valoracion: number = 0;

	constructor(public dialogRef: MatDialogRef<OpinarModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any) {}

	saveRating() {
		console.log(this.descripcion, this.valoracion);
		
	}
}
