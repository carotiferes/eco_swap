import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
	selector: 'app-admin-logistica',
	templateUrl: './admin-logistica.component.html',
	styleUrls: ['./admin-logistica.component.scss']
})
export class AdminLogisticaComponent {

	ordenForm: FormGroup;

	estados: string[] = ['ESTADO1', 'ESTADO2', 'ESTADO3'];

	constructor(private fb: FormBuilder) {
		this.ordenForm = fb.group({
			idOrden: ['', Validators.required],
			nuevoEstado: ['', Validators.required]
		})
	}

	enviar() {
		console.log(this.ordenForm.value);
		
	}
}
