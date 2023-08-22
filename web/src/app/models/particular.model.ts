import { DonacionModel } from "./donacion.model";

export interface ParticularModel {
	idParticular: number,
	usuario: any, // FK
	nombre: string,
	apellido: string,
	dni: string,
	cuil: string,
	fechaNacimiento: Date,
	tipoDocumento: string,
	publicaciones?: any[],
	donaciones?: DonacionModel[],
	puntaje: number
}