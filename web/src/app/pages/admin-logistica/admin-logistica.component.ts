import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LogisticaService } from 'src/app/services/logistica.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-admin-logistica',
	templateUrl: './admin-logistica.component.html',
	styleUrls: ['./admin-logistica.component.scss']
})
export class AdminLogisticaComponent {

	ordenForm: FormGroup;

	estados: string[] = ['CANCELADO', 'ENVIADO', 'POR_DESPACHAR', 'RECIBIDO'];

	constructor(private fb: FormBuilder, private logisticaServie: LogisticaService) {
		this.ordenForm = fb.group({
			idOrden: ['', /* Validators.required */],
			nuevoEstado: ['', /* Validators.required */]
		})
	}

	enviar() {
		console.log(this.ordenForm.value);
		this.logisticaServie.cambiarEstadoOrden(this.ordenForm.value.idOrden, this.ordenForm.value.nuevoEstado).subscribe({
			next: (res: any) => {
				Swal.fire('¡Éxito!', 'Se cambió el estado de la orden exitosamente', 'success')
				this.ordenForm.patchValue({idOrden: '', nuevoEstado: ''});
				this.ordenForm.markAsPristine();
				this.ordenForm.markAsUntouched();
				this.ordenForm.setErrors(null);
			}
		})
	}
}
