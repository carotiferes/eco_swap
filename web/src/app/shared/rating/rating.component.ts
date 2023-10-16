import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-rating',
	templateUrl: './rating.component.html',
	styleUrls: ['./rating.component.scss']
})
export class RatingComponent {
	@Input() puntuacion: number = 3.6;
	@Input() remSize: number = 1;
	@Input() color: string = 'var(--opiniones)';
}
