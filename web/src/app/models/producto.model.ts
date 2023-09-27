import { ColectaModel } from "./colecta.model";
import { DonacionModel } from "./donacion.model";

export interface ProductoModel {
	idProducto: number,
	descripcion: string,
	tipoProducto: string,
	cantidadSolicitada: number,
	cantidadRecibida: number,
	estado?: string,
	publicacion?: number,
	colectaDTO: ColectaModel,
	donaciones: DonacionModel[]
}