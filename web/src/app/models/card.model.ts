
export interface CardModel {
	id: number
	imagen: string;
	titulo: string;
	valorPrincipal: string;
	valorSecundario?: string;
	estado?: string;
	fecha?: any;
	fechaString?: string;
	usuario: {
		id: number;
		imagen: string;
		nombre: string;
		puntaje: number;
		localidad: string;
	};
	buttons: {
		name: string;
		icon: string;
		color: string;
		status: string;
	}[];
	action: 'select' | 'access' | 'detail' | 'list' | 'trueque';
	disabled?: boolean;
	idAuxiliar?: number;
	codigo?: string;
	isSelected?: boolean;
}