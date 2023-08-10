export interface UsuarioModel {
	idUsuario: number,
	username: string,
	password: string,
	salt: string,
	telefono: string,
	email: string,
	puntaje: number,
	intentos: number,
	bloqueado: boolean,
	swapper: boolean,
	opiniones: any[],
	direcciones: any[]
}