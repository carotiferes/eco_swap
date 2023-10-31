import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DonacionModel } from 'src/app/models/donacion.model';
import { ComprasService } from 'src/app/services/compras.service';
import { LogisticaService } from 'src/app/services/logistica.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-envio-modal',
	templateUrl: './envio-modal.component.html',
	styleUrls: ['./envio-modal.component.scss']
})
export class EnvioModalComponent {

	loading: boolean = false;
	loadingSave: boolean = false;
	ordenForm: FormGroup;

	sendDonaciones: boolean = false;

	costoEnvio?: number ;

	// SE LLAMA DESDE EL card.component.ts

	constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<EnvioModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any, private logisticaService: LogisticaService,
		private compraService: ComprasService) {
			console.log(data);
			
		this.ordenForm = fb.group({
			peso: ['', Validators.required],
			disponibilidad: ['', Validators.required]
		})

		if(data.cards) this.sendDonaciones = true;
	}

	calcularEnvio() {
		this.logisticaService.getCostoEnvio(this.ordenForm.value.peso).subscribe({
			next: (envio: any) => {
				this.costoEnvio = envio.precio;
			}
		})
	}

	crearOrden() {
		console.log(this.data);
		Swal.fire({
			title: 'Confirmar envío',
			text: 'Los datos que ingresaste se guardarán y se creará una orden de envío. No podrás hacer cambios luego. ¿Deseás continuar?',
			icon: 'question',
			showCancelButton: true, cancelButtonText: 'CANCELAR',
			confirmButtonText: 'CONFIRMAR'
		}).then(({isConfirmed}) => {
			if(isConfirmed) this.confirmarOrden();
		})
	}

	confirmarOrden() {
		if(this.sendDonaciones) { // UNA O MAS DONACIONES
			const donaciones: DonacionModel[] = this.data.cards;
			console.log(donaciones);

			const productosToSend: { productoId: number, donacionId: number, cantidad: number }[] = [];

			const titulo: string[] = [];

			for (const donacion of donaciones) {
				productosToSend.push({
					productoId: donacion.producto.idProducto,
					donacionId: donacion.idDonacion,
					cantidad: donacion.cantidadDonacion
				})

				if (!titulo.includes(donacion.producto.descripcion)) {
					titulo.push(donacion.producto.descripcion);
				}
			}
			
			this.logisticaService.getCostoEnvio(this.ordenForm.value.peso).subscribe({
				next: (envio: any) => {
					const orden = {
						titulo: titulo.join(', '),
						userIdDestino: donaciones[0].producto.colectaDTO.fundacionDTO.usuarioDTO.idUsuario,
						userIdOrigen: donaciones[0].particularDTO.usuarioDTO.idUsuario,
						idColecta: donaciones[0].producto.colectaDTO.idColecta,
						listProductos: productosToSend,
						costoEnvio: envio.precio
					}
					this.enviarOrden(orden);
				}
			})
		} else { // UNA COMPRA
			this.compraService.getMyCompras().subscribe({
				next: (res: any) => {
					const compras = res;
					const compra = compras.find((item: any) => item.idCompra == this.data.card.idAuxiliar)
					if(compra) {
						this.logisticaService.getCostoEnvio(this.ordenForm.value.peso).subscribe({
							next: (envio: any) => {
								const orden = {
									titulo: this.data.card.titulo,
									userIdDestino: compra.particularCompradorDTO.usuarioDTO.idUsuario,
									userIdOrigen: compra.publicacionDTO.particularDTO.usuarioDTO.idUsuario,
									idPublicacion: compra.publicacionDTO.idPublicacion,
									costoEnvio: envio.precio
								}
								this.enviarOrden(orden);
							}
						})
					}
				}
			})
		}
	}

	enviarOrden(orden: any) {
		this.logisticaService.crearOrden(orden).subscribe({
			next: (res: any) => {
				Swal.fire('¡Excelente!', 'Tu orden se creó con éxito', 'success')
				console.log(res);
				
			}
		})
	}

}
