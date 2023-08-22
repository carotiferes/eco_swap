import { DonacionModel } from "./donacion.model";
import { FundacionModel } from "./fundacion.model";
import { ParticularModel } from "./particular.model";
import { ProductoModel } from "./producto.model";

export interface PublicacionModel {
	idPublicacion: number,
	titulo: string,
	descripcion: string,
	estado: string,
	particularDTO: ParticularModel,
	fechaPublicacion: Date,
	imagenes: string,
	parsedImagenes?: string[],
	tipoPublicacion: string,
	precioVenta: number,
	valorTruequeMax: number,
	valorTruequeMin: number
}