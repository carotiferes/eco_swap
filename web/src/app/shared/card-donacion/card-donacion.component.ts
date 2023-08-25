import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ColectaModel } from 'src/app/models/colecta.model';
import { DonacionModel } from 'src/app/models/donacion.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-card-donacion',
  templateUrl: './card-donacion.component.html',
  styleUrls: ['./card-donacion.component.scss']
})
export class CardDonacionComponent implements OnInit{

	@Input() donacion!: DonacionModel;
	@Input() id_colecta!: number;
	@Input() usuarioHeader: any;
	@Input() buttons: {name: string, icon: string, color: string, status: string, disabled: boolean}[] = [];

	@Output() statusChanged = new EventEmitter<any>();

	userData: any
	caracteristicas: string = '';

	constructor(private donacionesService: DonacionesService){	}
	
	ngOnInit(): void {
		console.log(this.donacion, this.usuarioHeader,this.donacion.caracteristicaDonacion);
		for (const [i, caract] of this.donacion.caracteristicaDonacion.entries()) {
			if(caract.caracteristica && i == 0) this.caracteristicas = caract.caracteristica
			else if(caract.caracteristica) this.caracteristicas += ' - '+caract.caracteristica
		}
	}

	getImage(image: any) {
		return this.donacionesService.getImagen(image)
	}

	zoomImage(img?: string){
		if(img){
			Swal.fire({
				html: `<img src="${this.getImage(img)}" style="width: 100%"/>`,
				showConfirmButton: false,
				showCloseButton: true
			})
		}
	}

	changeEstadoDonacion(donacion: DonacionModel, status: string){
		this.donacionesService.cambiarEstadoDonacion(this.id_colecta, donacion.idDonacion, {
			nuevoEstado: status
		}).subscribe(res => {
			console.log(res);
			this.donacion.estadoDonacion = status;
			this.statusChanged.emit();
		})
	}

	getStars(puntaje: number){
		const array: any = [];
		for (let index = 0; index < puntaje; index++) {
			array.push(1)
		}
		return array;
	}
}
