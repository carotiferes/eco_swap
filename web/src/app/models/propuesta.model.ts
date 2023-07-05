import { SwapperModel } from "./swapper.model"

export interface PropuestaModel {
	id_propuesta: number,
	id_producto: number,
	descripcion: string,
	estado: string,
	cantidad_propuesta: number,
	imagenes: string[],
	id_swapper: number,
	swapper: SwapperModel
}