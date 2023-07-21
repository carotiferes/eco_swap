import { ProductoModel } from "./producto.model"
import { ParticularModel } from "./particular.model"

export interface DonacionModel {
	idDonacion: number,
	descripcion: string,
	cantidadDonacion: number,
	estadoDonacion: string,
	particular: ParticularModel
	producto: ProductoModel,
	caracteristicaPropuesta: string[],
	imagenes: any,
}