
export interface CardModel {
	id: number
	imagen: string;
	titulo: string;
	valorPrincipal: string;
	valorSecundario?: string;
	estado?: string;
	fecha: any;
	usuario: {
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
	action: 'select' | 'access';
	/* selectable: boolean;
	accessOnClick:  */
	/* id: number,
	titulo: string,
	descripcion: string,
	usuario: ParticularModel | FundacionModel,
	imagenes: string, // parsedImagenes
	fecha: string, // parsedFecha
	itemsDatos: any[],
	productos: ProductoModel[],
	donaciones: DonacionModel[] */
}