import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-credenciales-mp-modal',
	templateUrl: './credenciales-mp-modal.component.html',
	styleUrls: ['./credenciales-mp-modal.component.scss']
})
export class CredencialesMpModalComponent {
	
	loading: boolean = true;
	credencialesForm: FormGroup;
	loadingSave: boolean = false;

	constructor(public dialogRef: MatDialogRef<CredencialesMpModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any, private fb: FormBuilder, private usuarioService: UsuarioService){
			console.log(data);
			this.credencialesForm = fb.group({
				publicKey: [''],
				accessToken: ['']
			})
			if(data.publicKey) this.credencialesForm.controls['publicKey'].patchValue(data.publicKey)
			if(data.accessToken) this.credencialesForm.controls['accessToken'].patchValue(data.accessToken)
			this.loading = false;
	}

	saveCredenciales(){
		this.loadingSave = true;
		const particular = this.data.user.particularDTO;
		particular['publicKey'] = this.credencialesForm.controls['publicKey'].value;
		particular['accessToken'] = this.credencialesForm.controls['accessToken'].value;
		console.log(particular);
		
		this.usuarioService.editUser({
			telefono: this.data.user.telefono,
			particular: this.data.user.particularDTO,
			direccion: this.data.user.particularDTO.direcciones[0]
		}).subscribe({
			next: (res: any) => {
				console.log(res);
				this.loadingSave = false;
				Swal.fire('¡Genial!', 'Se guardaron tus credenciales, ya podés vender productos!', 'success').then(() => {
					this.dialogRef.close()
				})
			}, error: () => this.loadingSave = false
		})
	}

	downloadFile(): void {
		// Replace 'your-file-url' with the actual URL of the file you want to download
		const fileUrl = 'assets/mercado_pago.pdf';
	  
		const link = document.createElement('a');
		link.href = fileUrl;
		link.download = 'mercado_pago.pdf'; // Specify the desired filename
	  
		console.log(link);
		
		// Trigger a click event on the anchor element
		link.click();
	  }
}
