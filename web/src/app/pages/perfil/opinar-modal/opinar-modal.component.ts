import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OpinionesService } from 'src/app/services/opiniones.service';
import Swal from 'sweetalert2';

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
		@Inject(MAT_DIALOG_DATA) public data: any, private opinionesService: OpinionesService) {}

	saveRating() {
		this.opinionesService.crearOpinion({
			idUsuarioOpinado: this.data.usuario.idUsuario,
			valoracion: this.valoracion,
			descripcion: this.descripcion
		}).subscribe({
			next: (res: any) => {
				console.log(res);
				Swal.fire('¡Éxito!', 'Se guardó tu opinión sobre '+this.data.user.nombre+'!', 'success')
				this.dialogRef.close(true);
			},
			error: (error: any) => {
				Swal.fire('Error!', 'Ocurrió un error al guardar tu opinión sobre '+this.data.user.nombre+'. Volvé a intentarlo más tarde', 'error')
				console.log(error)
			}
		})
	}
}
