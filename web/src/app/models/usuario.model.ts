export interface UsuarioModel {
	idPerfil: number,
	username: string,
	email: string,
	swapper: boolean,
	password: string,
	puntaje: number,
	telefono: string,
	opiniones: any[],
	direcciones: any[]
}