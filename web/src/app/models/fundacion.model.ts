import { PerfilModel } from "./perfil.model";
import { SolicitudModel } from "./solicitud.model";

export interface FundacionModel {
	idFundacion: number,
	nombre: string,
	cuil: string,
	perfil: PerfilModel,
	solicitudes: SolicitudModel[]
}