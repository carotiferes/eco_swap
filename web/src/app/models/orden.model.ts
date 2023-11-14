
export interface OrdenModel {
	idOrden: Number;
    precioEnvio: Number;
    titulo: String;
    descripcion: String;
    idUsuarioOrigen: Number;
    idUsuarioDestino: Number;
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
    publicacionId: Number;
    colectaId: Number;
    esPublicacion: Boolean;
    fechaEnviosDTO: any;
    fechaADespachar: String;
}