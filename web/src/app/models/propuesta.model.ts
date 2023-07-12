import { ProductoModel } from "./producto.model"
import { SwapperModel } from "./swapper.model"

export interface PropuestaModel {
	idPropuesta: number,
	descripcion: string,
	cantidadPropuesta: number,
	estadoPropuesta: string,
	swapper: SwapperModel
	producto: ProductoModel,
	id_swapper: number,
	caracteristicaPropuesta: string[],
	imagenes: any,
}