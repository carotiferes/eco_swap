import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-images-modal',
  templateUrl: './images-modal.component.html',
  styleUrls: ['./images-modal.component.scss']
})
export class ImagesModalComponent implements OnInit{

	images: {id: number, name: string}[] = [];
	selectedImage?: number;

	loading: boolean = true;
	loadingSave: boolean = false;

	constructor(public dialogRef: MatDialogRef<ImagesModalComponent>, private usuarioService: UsuarioService){}
	
	ngOnInit(): void {
		for (let index = 15; index <= 25; index++) {
			this.images.push({
				id: index,
				name:`assets/perfiles/perfiles-${index}.jpg`
			})
		}
		setTimeout(() => {
			this.loading = false;
		}, 3300);
	}

	selectImage(id: number) {
		this.selectedImage = id;
	}

	confirm() {
		console.log(this.selectedImage);
		if(this.selectedImage) {
			this.loadingSave = true;
			this.usuarioService.editAvatar(this.selectedImage.toString()).subscribe({
				next: (res: any) => {
					this.loadingSave = false;
					Swal.fire('¡Excelente!', 'Tu avatar se cambió con éxito!', 'success')
					this.dialogRef.close(this.selectedImage);
				}
			})
		} else Swal.fire('Error!', 'Seleccioná una imagen para cambiar tu avatar!', 'error')
	}

	close() {
		this.dialogRef.close();
	}
}
