import { DonacionModel } from "./donacion.model";
import { FundacionModel } from "./fundacion.model";
import { ProductoModel } from "./producto.model";

export interface ColectaModel {
	idColecta: number,
	titulo: string,
	descripcion: string,
	fundacionDTO: FundacionModel,
	imagen: string,
	fechaInicio: Date,
	fechaFin: Date,
	productos: ProductoModel[],
	donaciones: DonacionModel[]
}