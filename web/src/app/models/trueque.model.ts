import { ParticularModel } from "./particular.model";
import { PublicacionModel } from "./publicacion.model";

export interface TruequeModel {
	idTrueque: number,
	estadoTrueque: string,
	publicacionDTOorigen: PublicacionModel,
	publicacionDTOpropuesta: PublicacionModel
}