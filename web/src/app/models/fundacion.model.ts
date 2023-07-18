import { PerfilModel } from "./perfil.model";
import { ColectaModel } from "./colecta.model";

export interface FundacionModel {
	idFundacion: number,
	nombre: string,
	cuil: string,
	perfil: PerfilModel,
	solicitudes: ColectaModel[]
}