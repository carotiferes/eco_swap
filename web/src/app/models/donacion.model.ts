import { ProductoModel } from "./producto.model"
import { ParticularModel } from "./particular.model"

export interface DonacionModel {
	idDonacion: number,
	descripcion: string,
	cantidadDonacion: number,
	estadoDonacion: string,
	particularDTO: ParticularModel
	producto: ProductoModel,
	caracteristicaDonacion: any[],
	imagenes: any,
	parsedImagenes: string[],
	nombreProducto?: string
}