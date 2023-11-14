
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
	buttons: CardButtonModel[];
	action: 'select' | 'access' | 'detail' | 'list' | 'trueque';
	disabled?: boolean;
	idAuxiliar?: number;
	codigo?: string;
	isSelected?: boolean;
	estadoAux?: string;
}

export interface CardButtonModel {
	name: string;
	icon: string;
	color: string;
	status: string;
	action: 'navigate' | 'ver_envio' | 'configurar_envio' | 'change_status' | 'click_card' | 'add_or_remove' | 'list' | 'opinar'; 
	disabled?: boolean;
}