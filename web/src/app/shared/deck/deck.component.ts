import { Component, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { CardModel } from 'src/app/models/card.model';

@Component({
	selector: 'app-deck',
	templateUrl: './deck.component.html',
	styleUrls: ['./deck.component.scss']
})
export class DeckComponent {
	@Input() cardList: CardModel[] = [];
	@Input() app: 'colectas' | 'donaciones' | 'publicaciones' = 'colectas';
	paginatedCardList: CardModel[] = [];

	@ViewChild(MatPaginator) paginator!: MatPaginator;
	pageSize = 4;

	ngOnChanges(changes: any): void {
		console.log('change', this.cardList);
		this.paginatedCardList = this.cardList.slice(0, this.pageSize);
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedCardList = this.cardList.slice(startIndex, endIndex);
	}
}
