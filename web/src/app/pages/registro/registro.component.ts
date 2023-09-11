import { ChangeDetectorRef, Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import Swal from 'sweetalert2';
import { DateAdapter } from '@angular/material/core';
import { UsuarioService } from 'src/app/services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { PhoneNumberPipe } from 'src/app/pipes/phone-number.pipe';
import { CustomDateAdapter } from 'src/app/pipes/date-adapter';
import { Location } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
	selector: 'app-registro',
	templateUrl: './registro.component.html',
	styleUrls: ['./registro.component.scss'],
	providers: [
		{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' },
		{ provide: DateAdapter, useClass: CustomDateAdapter }
	]
})
export class RegistroComponent {
	mainForm: FormGroup;
	particularForm: FormGroup;
	fundacionForm: FormGroup;
	direccionForm: FormGroup;

	isSwapper: boolean = true;
	loading = true;

	screenWidth: number;

	origin: 'resetPassword' | 'createAccount' | 'editAccount' = 'createAccount';
	passwordIcon: string = 'visibility_off';
	passwordType: string = 'password';

	tiposDocumento: any[] = [];
	id_user?: number;

	constructor(private fb: FormBuilder, private dateAdapter: DateAdapter<Date>,
		private usuarioService: UsuarioService, private location: Location,
		private auth: AuthService, private router: Router, private snackbar: MatSnackBar,
		private cdr: ChangeDetectorRef, private phoneNumberPipe: PhoneNumberPipe) {
		this.mainForm = fb.group({
			//username: [''], TODO: POR AHORA USO EMAIL
			email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/)]],
			telefono: ['', [Validators.required, this.telefonoValidator()]],
			password: ['', [Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!*\-_.,])[a-zA-Z\d@#$%^&+=!*\-_.,]{8,32}$/), this.validarConfirmPassword(),]],
			confirmPassword: ['', [Validators.required, this.validarConfirmPassword()]],
		})
		this.particularForm = fb.group({
			nombre: ['', Validators.required],
			apellido: ['', Validators.required],
			tipoDocumento: ['', Validators.required],
			nroDocumento: ['', Validators.required], // TODO: por ahora paso nro a cuil y dni
			fechaNacimiento: ['', Validators.required],
			//cuil: [''],
		})
		this.fundacionForm = fb.group({
			nombre: ['', Validators.required],
			cuit: ['', Validators.required],
		})

		this.direccionForm = fb.group({
			direccion: ['', Validators.required],
			altura: ['', Validators.required],
			piso: [''],
			departamento: [''],
			codigoPostal: ['', Validators.required],
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.dateAdapter.setLocale('es-AR'); // DD/mm/YYYY

		const url = location.path()
		if (url == '/reset-password') {
			this.origin = 'resetPassword';
		} else if (url == '/edit-perfil') {
			this.origin = 'editAccount';
			this.mainForm.controls['password'].disable();
			this.mainForm.controls['confirmPassword'].disable();
			this.mainForm.controls['email'].disable();
		} else {
			this.mainForm.controls['telefono'].addValidators(Validators.required)
		}
	}

	ngOnInit(): void {
		if (this.origin == 'createAccount') this.selectTipoPerfil();
		else if(this.origin == 'editAccount'){
			this.loadUserInfo();
			this.loading = false;
		} else this.loading = false;
		this.getTiposDocumentos()
	}

	ngOnDestroy(): void {
		Swal.close()
	}

	loadUserInfo() {
		const id = this.auth.getUserID();
		if(id) {
			this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
				next: (user: any) => {
					console.log(user);
					this.mainForm.patchValue({
						email: user.email,
						telefono: user.telefono,
					})
	
					if(user.particularDTO){
						this.particularForm.patchValue({
							nombre: user.particularDTO.nombre,
							apellido: user.particularDTO.apellido,
							tipoDocumento: user.particularDTO.tipoDocumento,
							nroDocumento: user.particularDTO.dni,
							fechaNacimiento: user.particularDTO.fechaNacimiento,
						})
					} else {
						this.fundacionForm.patchValue({
							nombre: user.fundacionDTO.nombre,
							cuit: user.fundacionDTO.cuil
						})
					}
	
					this.direccionForm.patchValue({
						direccion: user.particularDTO ? user.particularDTO.direcciones[0].direccion : user.fundacionDTO.direcciones[0].direccion,
						altura: user.particularDTO ? user.particularDTO.direcciones[0].altura : user.fundacionDTO.direcciones[0].altura,
						piso: user.particularDTO ? user.particularDTO.direcciones[0].piso : user.fundacionDTO.direcciones[0].piso,
						departamento: user.particularDTO ? user.particularDTO.direcciones[0].departamento : user.fundacionDTO.direcciones[0].departamento,
						codigoPostal: user.particularDTO ? user.particularDTO.direcciones[0].codigoPostal : user.fundacionDTO.direcciones[0].codigoPostal,
					})
				},
				error: (error) => {
					console.log('error', error);
					
				}
			})
		} else Swal.fire('Error!', 'Ocurrió un error al traer tu información. Intentalo devuelta más tarde', 'error')
	}

	selectTipoPerfil() {
		Swal.fire({
			title: '¡Te damos la bienvenida!',
			text: `Nos alegra que te sumes a nuestra comunidad, antes de seguir, 
			necesitamos saber si te querés registrar como fundación o como Particular`,
			confirmButtonText: 'Represento a una fundación',
			confirmButtonColor: '#87db59',
			cancelButtonText: 'No soy de una fundación',
			showCancelButton: true,
			cancelButtonColor: '#ae59db',
			icon: 'question',
			allowOutsideClick: false,
			allowEscapeKey: false
		}).then(result => {
			console.log(result);
			if (result.isConfirmed) { // FUNDACION
				this.isSwapper = false;
				this.particularForm = this.fb.group({})
			} else { // SWAPPER
				this.fundacionForm = this.fb.group({})
			}
			console.log(this.isSwapper);
			this.loading = false
		})
	}

	getTiposDocumentos() {
		this.usuarioService.getTiposDocumentos().subscribe({
			next: (res: any) => {
				console.log('TIPOS DOC:', res);
				this.tiposDocumento = res;
			},
			error: (error) => {
				console.log('ERROR TIPOS DOC:', error);
				
			}
		})
	}

	submit() {
		console.log('registro', this.mainForm.value);
		if (this.mainForm.valid) {
			if (this.origin == 'resetPassword') {
				const body = {
					username: this.mainForm.controls['email'].value,
					nuevoPassword: this.mainForm.controls['password'].value,
					confirmarPassword: this.mainForm.controls['confirmPassword'].value
				}
				this.auth.resetPassword(body).subscribe({
					next: (res) => {
						console.log(res);

					},
					error: (error) => {
						console.log('error:', error);

					},
					//complete: () => {}
				})
			} else {
				if(this.direccionForm.valid){
					let user: any = {
						username: this.mainForm.controls['email'].value,
						email: this.mainForm.controls['email'].value,
						password: this.mainForm.controls['password'].value,
						telefono: this.mainForm.controls['telefono'].value,
						confirmPassword: this.mainForm.controls['confirmPassword'].value,
						direccion: {
							altura: this.direccionForm.controls['altura'].value,
							codigoPostal: this.direccionForm.controls['codigoPostal'].value,
							direccion: this.direccionForm.controls['direccion'].value,
							departamento: this.direccionForm.controls['departamento'].value,
							piso: this.direccionForm.controls['piso'].value,
						}
					};
	
					let validDataForm = false;
	
					if (this.isSwapper) {
						if (this.particularForm.valid) {
							user.particular = {
								fechaNacimiento: new Date(this.particularForm.controls['fechaNacimiento'].value),
								dni: this.particularForm.controls['nroDocumento'].value, // TODO: CAMBIAR A NRO DOC
								cuil: this.particularForm.controls['nroDocumento'].value,
								nombre: this.particularForm.controls['nombre'].value,
								apellido: this.particularForm.controls['apellido'].value,
								tipoDocumento: this.particularForm.controls['tipoDocumento'].value
							}
							validDataForm = true;
						} else {
							this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
						}
					} else {
						if (this.fundacionForm.valid) {
							user.fundacion = {
								nombre: this.fundacionForm.controls['nombre'].value,
								cuil: this.fundacionForm.controls['cuit'].value //TODO: que en back sea cuit
							}
							validDataForm = true;
						} else {
							this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
						}
					}
	
					console.log(user);
					if(validDataForm){
						if(this.origin == 'createAccount') {
							this.usuarioService.createUser(user).subscribe({
								next: (id_user: any) => {
									console.log('next', id_user);
									this.id_user = id_user;
									// TODO: REVISAR CON EMAILS. esperar 1 min antes de dejarlo enviar devuelta
									this.showMessage('¡Gracias por registrarte!',
										`Te enviamos un email a la cuenta que ingresaste,
										con un código para verificar tu cuenta. Por favor ingresalo a continuación.
										Si no verificás la cuenta ahora, podrás hacerlo la próxima vez que inicies sesión.`,
										'Confirmar', 'send_again', 'success', 'No recibí el email')
								},
								error: (e) => {
									console.error('error', e);
									if(e.message.includes('duplicate'))
									this.showMessage('Error!', 'Ya existe un usuario con el email ingresado.', 'OK', 'error', 'error')
									else this.showMessage('Error!', 'Ha ocurrido un error al crear la cuenta', 'OK', 'error', 'error')
								},
								//complete: () => console.info('signup complete')
							})
						} else {
							this.usuarioService.editUser(user).subscribe({
								next: (id_user: any) => {
									console.log('next', id_user);
									this.showMessage('¡Éxito!', `Tus datos se editaron exitosamente.`, 'Genial!', 'edit', 'success')
								},
								error: (e) => {
									console.error('error', e);
									this.showMessage('Error!', 'Ha ocurrido un error al editar la cuenta', 'OK', 'error', 'error')
								}
							})
						}
					}
				} else {
					this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
				}
			}
		} else {
			this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
		}
	}

	showMessage(title: string, text: string, confirm: string, origin: string,
		icon: 'success' | 'error' | 'warning', deny?: string) {
		Swal.fire({
			title,
			text,
			confirmButtonText: confirm,
			showDenyButton: deny ? true : false,
			denyButtonText: deny,
			icon,
			allowOutsideClick: icon == 'success' ? false : true,
			input: origin == 'send_again' ? 'text' : undefined,
			reverseButtons: true,
			preDeny: () => {
				if(this.id_user) this.auth.sendEmailAgain(this.id_user).subscribe({
					next: (res) => {
						console.log(res);
						this.snackbar.open('Se envió el nuevo mail!', '', {
							horizontalPosition: 'center',
							verticalPosition: 'top',
							duration: 3000
						})
					}
				})
				return false;
			}
		}).then(({ isConfirmed, value, isDenied }) => {
			console.log(value);
			/* if (isDenied && origin == 'send_again') {
				// TODO: RESEND EMAIL
				this.router.navigate(['login'])
			} */
			if(isConfirmed){
				if(origin == 'ir_a_home') {
					this.router.navigate(['home'])
				} else if(origin == 'edit') {
					this.router.navigate(['perfil'])
				} else if (origin == 'send_again') {
					if(this.id_user) this.usuarioService.confirmarCuenta(this.id_user, value).subscribe({
						next: (res) => {
							console.log(res);
							Swal.fire('Excelente!', 'Tu cuenta fue verificada, ya podes usar Ecoswap!', 'success')
							this.router.navigate(['/'])
						},
						error: (error) => {
							console.log(error);
							if(error.message.descripcion == "El código es incorrecto") {
								Swal.fire('Código incorrecto!', 'El código ingresado es incorrecto. Iniciá sesión y volvé a intentarlo.', 'error')
								this.router.navigate(['/login'])
							}
						}
					})
					else Swal.fire('Error!', 'Ocurrió un error al activar tu cuenta. Por favor intentalo más tarde', 'error')
				}
			}
		})
	}

	validarConfirmPassword(): ValidatorFn {
		return (control: AbstractControl): { [key: string]: any } | null => {
			if (control.value) {
				const password = this.mainForm.controls['password'].value;
				const confirmPassword = this.mainForm.controls['confirmPassword'].value;

				if (password && confirmPassword && password != confirmPassword) {
					return { differentPassword: true };
				} else {
					this.mainForm.controls['password'].setErrors(null);
					this.mainForm.controls['confirmPassword'].setErrors(null);
				}
			}
			return null; // Valor válido
		};
	}

	formatearTelefono() {
		this.mainForm.get('telefono')?.setValue(this.phoneNumberPipe.transform(this.mainForm.get('telefono')?.value)); // Esto fuerza el cambio del valor
		this.cdr.detectChanges(); // Detectamos los cambios manualmente
	}

	telefonoValidator(): ValidatorFn {
		const telefonoPattern = /^11\s\d{4}-\d{4}$/;
	  
		return (control: AbstractControl): { [key: string]: any } | null => {
		  if (!control.value) {
			// Si el campo está vacío, no se aplica la validación.
			return null;
		  }
	  
		  const esValido = telefonoPattern.test(control.value);
		  return esValido ? null : { formatoInvalido: true };
		};
	}

	togglePassword(){
		this.passwordType = this.passwordType === 'text' ? 'password' : 'text';
		this.passwordIcon = this.passwordIcon === 'visibility' ? 'visibility_off' : 'visibility';
	}

	refreshValidity() {
		this.mainForm.updateValueAndValidity()
	}

	formatDocument(event: any) {
		console.log(event);
		switch (event.value) {
			case 'DNI': //
				this.particularForm.controls['nroDocumento'].addValidators(Validators.pattern(/^\d{7,8}$/))
				break;
		
			default:
				this.particularForm.controls['nroDocumento'].addValidators(Validators.pattern(/^\d{7,8}$/))
				break;
		}
	}
	
}
