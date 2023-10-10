import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-images-modal',
  templateUrl: './images-modal.component.html',
  styleUrls: ['./images-modal.component.scss']
})
export class ImagesModalComponent implements OnInit{

	images: {id: number, name: string}[] = [];
	selectedImage?: number;

	loading: boolean = true;

	constructor(public dialogRef: MatDialogRef<ImagesModalComponent>){}
	
	ngOnInit(): void {
		for (let index = 15; index <= 25; index++) {
			this.images.push({
				id: index,
				name:`assets/perfiles/perfiles-${index}.jpg`
			})
		}
		this.loading = false;
	}

	selectImage(id: number) {
		this.selectedImage = id;
	}

	confirm() {
		this.dialogRef.close(this.selectedImage);
	}

	close() {
		this.dialogRef.close();
	}
}
