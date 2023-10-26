import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ComprasService } from 'src/app/services/compras.service';
import { LogisticaService } from 'src/app/services/logistica.service';

@Component({
	selector: 'app-envio-modal',
	templateUrl: './envio-modal.component.html',
	styleUrls: ['./envio-modal.component.scss']
})
export class EnvioModalComponent {

	loading: boolean = false;
	loadingSave: boolean = false;
	ordenForm: FormGroup;

	// SE LLAMA DESDE EL card.component.ts

	constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<EnvioModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any, private logisticaService: LogisticaService,
		private compraService: ComprasService) {
			console.log(data);
			
		this.ordenForm = fb.group({
			peso: ['', Validators.required],
			codigoPostal: ['', Validators.required]
		})
	}

	crearOrden() {
		this.compraService.getMyCompras().subscribe({
			next: (res: any) => {
				const compras = res;
				const compra = compras.find((item: any) => item.idCompra == this.data.idAuxiliar)
				console.log(compra);
				
				if(compra) {
					this.logisticaService.getCostoEnvio().subscribe({
						next: (envio: any) => {
							const orden = {
								titulo: this.data.titulo,
								userIdDestino: compra.particularCompradorDTO.usuarioDTO.idUsuario,
								userIdOrigen: compra.publicacionDTO.particularDTO.usuarioDTO.idUsuario,
								publicacionOColectaId: this.data.id,
								esPublicacion: this.data.codigo == 'Compra',
								cantidad: 1,
								idDeItems: [
								  0
								],
								costoEnvio: envio.precio
							}
							/* this.logisticaService.crearOrden(orden).subscribe({
								next: (res: any) => {
					
								}
							}) */
							
						}
					})
				}
			}
		})
	}

}
