import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DonacionModel } from 'src/app/models/donacion.model';
import { OrdenModel } from 'src/app/models/orden.model';
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

	loading: boolean = true;
	loadingSave: boolean = false;
	ordenForm: FormGroup;

	userOrders: OrdenModel[] = [];
	ordersToShow: OrdenModel[] = [];

	costoEnvio?: number;

	user?: UsuarioModel;
	loadingCosto: boolean = false;
	type: 'compra' | 'unaDonacion' | 'variasDonaciones' = 'compra';

	compra: any;

	hasCancelledEnvio: boolean = false;
	isNuevoEnvio: boolean = false;

	// SE LLAMA DESDE EL card.component.ts

	constructor(private fb: FormBuilder, public dialogRef: MatDialogRef<EnvioModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any, private logisticaService: LogisticaService,
		private compraService: ComprasService, private usuarioService: UsuarioService,
		private auth: AuthService, private donacionesService: DonacionesService) {
		console.log(data);

		this.ordenForm = fb.group({
			peso: ['', Validators.required],
			disponibilidad: ['', Validators.required]
		})
	}

	ngOnInit(): void {
		if (this.data.cards) this.type = 'variasDonaciones';
		else if (this.data.card.codigo == 'Donación') this.type = 'unaDonacion';
		if(this.data.newEnvio) {
			this.isNuevoEnvio = true;
			this.loading = false;
		} 
		this.getUserOrders()
	}

	getUserOrders() {
		this.ordersToShow.splice(0)
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
					this.ordersToShow = this.userOrders.filter(order => {
						return order.productosADonarDeOrdenList.some((producto: any) => {
							return uniqueIdDonacionValues.includes(producto.idDonacion);
						});
					});
					this.ordersToShow.sort((a, b) => new Date(a.fechaCreacionOrdenEnvio).getTime() - new Date(b.fechaCreacionOrdenEnvio).getTime());
					if(!this.isNuevoEnvio && this.ordersToShow.length > 0 && this.ordersToShow[this.ordersToShow.length - 1].listaFechaEnvios.find(item => item.estado == 'CANCELADO')){
						this.hasCancelledEnvio = true;
					}

				} else {
					this.compraService.getMyCompras().subscribe({
						next: (res: any) => {
							const compras = res;
							const compra = compras.find((item: any) => item.idCompra == this.data.card.idAuxiliar)
							if(compra) {
								this.compra = compra;
								this.ordenForm.controls['peso'].setValue(compra.publicacionDTO.peso)
							}
							this.ordersToShow = this.userOrders.filter(order => order.publicacionId == this.data.card.id)
						}
					})
				}

				if (this.ordersToShow.length == 0) {
					this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
						next: (res: any) => {
							this.user = res;
							console.log(this.user);
							this.loading = false;
						}
					})
				} else this.loading = false;
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
			if (this.compra) {
				this.logisticaService.getCostoEnvio(this.ordenForm.value.peso).subscribe({
					next: (envio: any) => {
						const orden = {
							titulo: this.data.card.titulo,
							userIdDestino: this.compra.particularCompradorDTO.usuarioDTO.idUsuario,
							userIdOrigen: this.compra.publicacionDTO.particularDTO.usuarioDTO.idUsuario,
							idPublicacion: this.compra.publicacionDTO.idPublicacion,
							costoEnvio: envio.precio
						}
						this.enviarOrden(orden);
					}
				})
			}
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

	nuevoEnvio() {
		this.dialogRef.close('newEnvio');
	}

}
