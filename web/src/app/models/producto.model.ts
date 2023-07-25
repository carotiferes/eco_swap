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
	colecta: ColectaModel,
	donaciones: DonacionModel[]
	/* id_producto: number,
	c_tipo_producto: number,
	id_publi_solic: number,
	s_descripcion: string,
	n_cantidad_solicitada: number,
	n_cantidad_recibida: number,
	s_estado: string, */
}