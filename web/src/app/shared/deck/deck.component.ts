import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
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
	@Input() fullScreenWidth: boolean = false;

	@Output() statusChanged = new EventEmitter<any>();
	@Output() cardSelected = new EventEmitter<any>();

	paginatedCardList: CardModel[] = [];

	@ViewChild(MatPaginator) paginator!: MatPaginator;
	@Input() pageSize = 4;

	ngOnChanges(changes: any): void {
		//console.log('change', this.cardList);
		if(this.pageSize == 4) this.pageSize = this.fullScreenWidth ? 3 : 4;
		this.paginatedCardList = this.cardList.slice(0, this.pageSize);
	}

	changePage(event: any) {
		const startIndex = event.pageIndex * event.pageSize;
		const endIndex = startIndex + event.pageSize;
		this.paginatedCardList = this.cardList.slice(startIndex, endIndex);
	}

	cardStatusChanged(event:any) {
		this.statusChanged.emit(event)
	}

	cardWasSelected(event: any){
		this.cardSelected.emit(event)
	}
}
