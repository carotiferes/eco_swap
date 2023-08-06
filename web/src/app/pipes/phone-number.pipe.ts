import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'phoneNumber'
})
export class PhoneNumberPipe implements PipeTransform {

	transform(value: string): string {
		if (!value) {
		  return '';
		}
	
		// Limpia el número de teléfono de espacios y guiones
		const telefonoLimpio = value.replace(/\D/g, '');
	
		// Verifica si el número de teléfono tiene al menos 8 dígitos (incluyendo el código de área 11)
		if (telefonoLimpio.length >= 8) {
		  // Formatea el número de teléfono como 11 xxxx-xxxx
		  const codigoArea = telefonoLimpio.substring(0, 2);
		  const primeraParte = telefonoLimpio.substring(2, 6);
		  const segundaParte = telefonoLimpio.substring(6);
	
		  return `${codigoArea} ${primeraParte}-${segundaParte}`;
		} else {
		  // Si el número no tiene suficientes dígitos, devuelve el número limpio sin formato
		  return telefonoLimpio;
		}
	}

}
