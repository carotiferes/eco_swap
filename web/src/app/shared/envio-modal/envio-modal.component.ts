import { DatePipe } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DonacionModel } from 'src/app/models/donacion.model';
import { UsuarioModel } from 'src/app/models/usuario.model';
import { AuthService } from 'src/app/services/auth.service';
import { ComprasService } from 'src/app/services/compras.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { LogisticaService } from 'src/app/services/logistica.service';
import { UsuarioService } from 'src/app/services/usuario.service';
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

	userOrders: any[] = []

	costoEnvio?: number;

	yaTieneEnvio: any;
	user?: UsuarioModel;
	loadingCosto: boolean = false;
	type: 'compra' | 'unaDonacion' | 'variasDonaciones' = 'compra';

	// SE LLAMA DESDE EL card.component.ts

	constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<EnvioModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any, private logisticaService: LogisticaService,
		private compraService: ComprasService, private usuarioService: UsuarioService,
		private auth: AuthService, private donacionesService: DonacionesService,
		private datePipe: DatePipe) {
		console.log(data);

		this.ordenForm = fb.group({
			peso: ['', Validators.required],
			disponibilidad: ['', Validators.required]
		})

		if (data.cards) this.type = 'variasDonaciones';
		else if (data.card.codigo == 'Donación') this.type = 'unaDonacion';
		this.getUserOrders()
	}

	getUserOrders() {
		this.logisticaService.obtenerMisOrdenes(this.type != 'compra' ? 'donaciones' : 'publicaciones').subscribe({
			next: (res: any) => {
				if (res.length > 0) {
					this.userOrders = res;
				}
				console.log(this.userOrders);
				if (this.type != 'compra') {
					// Get the list of unique idDonacion values from the cards array
					const uniqueIdDonacionValues = this.type == 'variasDonaciones' ? [...new Set(this.data.cards.map((card: any) => card.idDonacion || card.id))] : [this.data.card.id];
					console.log(uniqueIdDonacionValues);

					// Find the orders that match the idDonacion from the cards array
					const matchingOrders = this.userOrders.find(order => {
						return order.productosADonarDeOrdenList.some((producto: any) => {
							return uniqueIdDonacionValues.includes(producto.idDonacion);
						});
					});
					if (matchingOrders) this.yaTieneEnvio = matchingOrders;

				} else {
					this.yaTieneEnvio = this.userOrders.find(order => order.publicacionId == this.data.card.id)
				}

				if (!this.yaTieneEnvio) {
					this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
						next: (res: any) => {
							this.user = res;
							console.log(this.user);

						}
					})
				}
			}
		})
	}

	calcularEnvio() {
		this.loadingCosto = true;
		this.logisticaService.getCostoEnvio(this.ordenForm.value.peso).subscribe({
			next: (envio: any) => {
				this.costoEnvio = envio.precio;
				this.loadingCosto = false;
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
		}).then(({ isConfirmed }) => {
			if (isConfirmed) this.confirmarOrden();
		})
	}

	confirmarOrden() {
		this.loadingSave = true;
		if (this.type == 'variasDonaciones') {
			const donaciones: DonacionModel[] = this.data.cards;
			console.log('donaciones', donaciones);
			this.configurarDonaciones(donaciones);

		} else if (this.type == 'unaDonacion') {
			this.donacionesService.getMisDonaciones().subscribe({
				next: (donaciones: any) => {
					const donacion = donaciones.find((item: DonacionModel) => item.idDonacion == this.data.card.id)
					this.configurarDonaciones([donacion])
				}
			})
		} else { // UNA COMPRA
			this.compraService.getMyCompras().subscribe({
				next: (res: any) => {
					const compras = res;
					const compra = compras.find((item: any) => item.idCompra == this.data.card.idAuxiliar)
					if (compra) {
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

	configurarDonaciones(donaciones: DonacionModel[]) {
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
	}

	enviarOrden(orden: any) {
		this.logisticaService.crearOrden(orden).subscribe({
			next: (res: any) => {
				this.loadingSave = false;
				Swal.fire('¡Excelente!', 'Tu orden se creó con éxito', 'success')
				this.dialogRef.close(true)
			}
		})
	}

}
