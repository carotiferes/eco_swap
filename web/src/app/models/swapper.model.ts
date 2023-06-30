export interface SwapperModel {
	id_swapper: number,
	id_perfil: number, // FK
	s_nombre: string,
	s_apellido: string,
	f_nacimiento: Date,
}