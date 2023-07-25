import { ProductoModel } from "./producto.model"
import { ParticularModel } from "./particular.model"

export interface DonacionModel {
	idDonacion: number,
	descripcion: string,
	cantidadDonacion: number,
	estadoDonacion: string,
	particular: ParticularModel
	producto: ProductoModel,
	caracteristicaDonacion: string[],
	imagenes: any,
	parsedImagenes: string[],
	nombreProducto?: string
}