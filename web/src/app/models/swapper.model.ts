import { DonacionModel } from "./donacion.model";

export interface SwapperModel {
	idSwapper: number,
	perfil: number, // FK
	nombre: string,
	apellido: string,
	dni: string,
	cuil: string,
	fechaNacimiento: Date,
	tipoDocumento: string,
	propuestas: DonacionModel[]
}