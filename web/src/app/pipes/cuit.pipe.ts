import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'cuit'
})
export class CuitPipe implements PipeTransform {

	transform(cuit: string): string {
		if (!cuit) {
			return '';
		}

		// Remove any non-digit characters from the input
		const cleanCuit = cuit.replace(/\D/g, '');

		if (cleanCuit.length === 11) {
			// Format as XX-XXXXXXXX-X
			return `${cleanCuit.substring(0, 2)}-${cleanCuit.substring(2, 10)}-${cleanCuit.substring(10, 11)}`;
		} else {
			// If it doesn't have exactly 11 digits, return the cleaned CUIT
			return cleanCuit;
		}
	}
}
