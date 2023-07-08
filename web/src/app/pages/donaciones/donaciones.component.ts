import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FundacionModel } from 'src/app/models/fundacion.model';
import { SolicitudModel } from 'src/app/models/solicitud.model';
import { AuthService } from 'src/app/services/auth.service';
import { DonacionesService } from 'src/app/services/donaciones.service';
import { FundacionesService } from 'src/app/services/fundaciones.service';

@Component({
  selector: 'app-donaciones',
  templateUrl: './donaciones.component.html',
  styleUrls: ['./donaciones.component.scss']
})
export class DonacionesComponent implements OnInit {

	solicitudes: SolicitudModel[] = [];
	fundacion?: FundacionModel;

	idFundacion?: string;
	solicitudesFundacion: SolicitudModel[] = []

	showSolicitudes: SolicitudModel[] = [];
	userData: any;

	constructor(private router: Router, private route: ActivatedRoute, private auth: AuthService,
		private donacionesService: DonacionesService, private fundacionesService: FundacionesService){
		route.paramMap.subscribe(params => {
			this.idFundacion = params.get('id_fundacion') || undefined;
			//this.fundacion = db.fundaciones.find((item: FundacionModel) => item.idFundacion == Number(this.idFundacion))
			console.log(params, this.idFundacion);
			if(this.idFundacion){
				fundacionesService.getFundacion(this.idFundacion).subscribe((res:any) => {
					this.fundacion = res
					console.log(res);
				})
				console.log(this.fundacion);
			}
		})
		
		this.userData = auth.getUserData().userData;


	}
	
	ngOnInit() {
		this.donacionesService.getSolicitudes().subscribe((res:any) => {
			console.log(res);
			this.solicitudes = res;
			this.showSolicitudes = this.solicitudes;

			this.showSolicitudes.map(item => {
				item.imagen = this.donacionesService.getImagen(item.imagen)
			})
		})
		/* if(this.idFundacion){
			// TODO: FILTER DONACIONES
			this.solicitudesFundacion = this.solicitudes.filter(item => item.fundacion.idFundacion == Number(this.idFundacion))
			this.showSolicitudes = this.solicitudesFundacion
		} else {
			this.showSolicitudes = this.solicitudes;
		} */

	}

	goToSolicitud(solicitud: SolicitudModel){
		this.router.navigateByUrl('solicitud/'+solicitud.idSolicitud)
	}

	addSolicitud(){
		this.router.navigateByUrl('form-solicitud')
		
	}
}
