
export interface OrdenModel {
	idOrden: number;
    precioEnvio: number;
    titulo: String;
    descripcion: String;
    idUsuarioOrigen: number;
    idUsuarioDestino: number;
    nombreCalle: String;
    altura: String;
    piso: String;
    dpto: String;
    barrio: String;
    ciudad: String;
    codigoPostal: String;
    nombreUserDestino: String;
    nombreUserOrigen: String;
    telefono: String;
    productosADonarDeOrdenList: any;
    publicacionId: number;
    colectaId: number;
    esPublicacion: Boolean;
    listaFechaEnvios: any[];
    fechaADespachar: String;
	fechaCreacionOrdenEnvio: Date;
}