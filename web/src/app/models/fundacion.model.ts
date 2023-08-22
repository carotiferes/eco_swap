import { UsuarioModel } from "./usuario.model";
import { ColectaModel } from "./colecta.model";

export interface FundacionModel {
	idFundacion: number,
	nombre: string,
	cuil: string,
	usuario: UsuarioModel,
	colectas: ColectaModel[],
	puntaje?: number
}