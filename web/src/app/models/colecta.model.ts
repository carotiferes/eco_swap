import { DonacionModel } from "./donacion.model";
import { ProductoModel } from "./producto.model";

export interface ColectaModel {
	idColecta: number,
	titulo: string,
	//tituloSolicitud: string,
	descripcion: string,
	//cantidadDisponible: number,
	idFundacion: number,
	fundacion: string,
	imagen: string,
	fechaSolicitud: Date,
	productos: ProductoModel[],
	donaciones: DonacionModel[]
}