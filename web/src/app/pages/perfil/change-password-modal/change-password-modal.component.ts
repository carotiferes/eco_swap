import { Component, Inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { UsuarioService } from 'src/app/services/usuario.service';
import Swal from 'sweetalert2';

@Component({
	selector: 'app-change-password-modal',
	templateUrl: './change-password-modal.component.html',
	styleUrls: ['./change-password-modal.component.scss']
})
export class ChangePasswordModalComponent {
	
	loading: boolean = true;
	passwordForm: FormGroup;
	loadingSave: boolean = false;

	passwordIcon: string = 'visibility_off';
	passwordType: string = 'password';

	showError: boolean = false;

	constructor(public dialogRef: MatDialogRef<ChangePasswordModalComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any, private fb: FormBuilder, private router: Router,
		private usuarioService: UsuarioService, private auth: AuthService){
			console.log(data);
			this.passwordForm = fb.group({
				currentPassword: ['', Validators.required],
				newPassword: ['', [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!*\-_.,])[a-zA-Z\d@#$%^&+=!*\-_.,]{8,32}$/), this.validarConfirmPassword(),]],
				confirmNewPassword: ['', [Validators.required, this.validarConfirmPassword()]]
			})
			this.loading = false;
	}

	savePassword(){
		this.loadingSave = true;
		
		Swal.fire({
			title: 'Confirmar Cambio de Contraseña',
			text: 'Si confirmás el cambio de contraseña, vas a tener que volver a iniciar sesión.',
			icon: 'warning'
		}).then(({isConfirmed}) => {
			if(isConfirmed) {
				const body = {
					nuevaPassword: this.passwordForm.controls['newPassword'].value,
					confirmarPassword: this.passwordForm.controls['confirmNewPassword'].value
				}
				this.usuarioService.changePassword(body).subscribe({
					next: (res: any) => {
						console.log(res);
						Swal.fire('¡Éxito!', 'Se cambió tu contraseña, ahora volvé a iniciar sesión', 'success').then(() => {
							this.auth.logout()
							this.router.navigate(['login'])
							this.dialogRef.close()
						})
					}, error: () => this.loadingSave = false
				})
			} else this.dialogRef.close()
		})
	}

	togglePassword() {
		this.passwordType = this.passwordType === 'text' ? 'password' : 'text';
		this.passwordIcon = this.passwordIcon === 'visibility' ? 'visibility_off' : 'visibility';
	}

	validarConfirmPassword(): ValidatorFn {
		return (control: AbstractControl): { [key: string]: any } | null => {
			if (control.value) {
				const password = this.passwordForm.controls['newPassword'].value;
				const confirmPassword = this.passwordForm.controls['confirmNewPassword'].value;
				if (password && confirmPassword && password != confirmPassword) return { differentPassword: true };
				else {
					this.passwordForm.controls['newPassword'].setErrors(null);
					this.passwordForm.controls['confirmNewPassword'].setErrors(null);
				}
			}
			return null; // Valor válido
		};
	}
}
