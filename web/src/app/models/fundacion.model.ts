import { UsuarioModel } from "./usuario.model";
import { ColectaModel } from "./colecta.model";
import { DireccionModel } from "./direccion.model";

export interface FundacionModel {
	idFundacion: number,
	nombre: string,
	cuil: string,
	usuarioDTO: UsuarioModel,
	colectas: ColectaModel[],
	puntaje?: number,
	direcciones: DireccionModel[]
}