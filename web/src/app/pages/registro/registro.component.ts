import { ChangeDetectorRef, Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
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
import { CuitPipe } from 'src/app/pipes/cuit.pipe';

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

	loadingSave: boolean = false;

	localidades: any[] = [];
	calles: any[] = [];

	closeSwal = true;

	constructor(private fb: FormBuilder, private dateAdapter: DateAdapter<Date>,
		private usuarioService: UsuarioService, private location: Location,
		private auth: AuthService, private router: Router, private snackbar: MatSnackBar,
		private cdr: ChangeDetectorRef, private phoneNumberPipe: PhoneNumberPipe,
		private cuitPipe: CuitPipe) {
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
			nroDocumento: [{ value: '', disabled: true }, Validators.required], // TODO: por ahora paso nro a cuil y dni
			fechaNacimiento: ['', Validators.required],
		})
		this.fundacionForm = fb.group({
			nombre: ['', Validators.required],
			cuit: ['', [Validators.required, isValidCUIT]],
		})
		this.direccionForm = fb.group({
			localidad: ['', [Validators.required, this.validateItem.bind(this)]],
			calle: [{ value: '', disabled: true }, [Validators.required, this.validateItem.bind(this)]],
			altura: [{ value: '', disabled: true }, Validators.required],
			piso: [{ value: '', disabled: true }],
			departamento: [{ value: '', disabled: true }],
			codigoPostal: [{ value: '', disabled: true }, Validators.required],
		})

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.dateAdapter.setLocale('es-AR'); // DD/mm/YYYY

		const url = location.path()
		if (url == '/reset-password') this.origin = 'resetPassword';
		else if (url == '/edit-perfil') {
			this.origin = 'editAccount';
			this.mainForm.controls['password'].disable();
			this.mainForm.controls['confirmPassword'].disable();
			this.mainForm.controls['email'].disable();
			const isUserSwapper = auth.isUserSwapper();
			if(isUserSwapper) this.isSwapper = true;
			else this.isSwapper = false;
		} else this.mainForm.controls['telefono'].addValidators(Validators.required)
	}

	ngOnInit(): void {
		if (this.origin == 'createAccount') this.selectTipoPerfil();
		else if (this.origin == 'editAccount') {
			this.loadUserInfo();
			this.loading = false;
		} else this.loading = false;
		this.getTiposDocumentos();
	}

	ngOnDestroy(): void {
		if(this.closeSwal) Swal.close();
		this.closeSwal = true;
	}

	loadUserInfo() {
		const id = this.auth.getUserID();
		if (id) {
			this.usuarioService.getUserByID(this.auth.getUserID()).subscribe({
				next: (user: any) => {
					console.log(user);
					this.mainForm.patchValue({
						email: user.email,
						telefono: user.telefono,
						password: user.password,
						confirmPassword: user.password,
					})

					if (user.particularDTO) {
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
					//console.log('USERRRR', user.particularDTO.direcciones);

					const direccion = user.particularDTO ? user.particularDTO.direcciones[0] : user.fundacionDTO.direcciones[0];
					if(direccion.localidad ) this.setAddress(direccion)
				},
				error: (error) => { console.log('error', error); }
			})
		} else Swal.fire('Error!', 'Ocurrió un error al traer tu información. Intentalo devuelta más tarde', 'error')
	}

	setAddress(direccion: any) {
		const apiUrl = 'https://apis.datos.gob.ar/georef/api/localidades?orden=id&provincia=02&exacto=true&nombre=' + encodeURIComponent(direccion.localidad)
		fetch(apiUrl).then(response => response.json()).then(data => {
			if (data.cantidad == 1) {
				this.direccionForm.controls['localidad'].setValue(data.localidades[0]);
				this.localidades = data.localidades;
				const locInput = this.direccionForm.controls['localidad'].value.departamento.id;
				const apiUrl = 'https://apis.datos.gob.ar/georef/api/calles?orden=id&provincia=02&exacto=true&departamento=' + encodeURIComponent(locInput) + '&nombre=' + encodeURIComponent(direccion.calle)
				fetch(apiUrl).then(response => response.json()).then(data => {
					if (data.cantidad == 1) this.direccionForm.controls['calle'].setValue(data.calles[0]);
					this.calles = data.calles;
					this.direccionForm.patchValue({
						altura: direccion.altura,
						piso: direccion.piso,
						departamento: direccion.dpto,
						codigoPostal: direccion.codigoPostal
					})
					this.direccionForm.enable();
				}).catch(error => console.error(error));
			}
		}).catch(error => console.error(error));
	}

	selectTipoPerfil() {
		Swal.fire({
			title: '¡Te damos la bienvenida!',
			text: `Nos alegra que te sumes a nuestra comunidad, antes de seguir, 
			necesitamos saber si te querés registrar como fundación o como Particular`,
			confirmButtonText: 'Represento a una fundación',
			confirmButtonColor: '#ae59db',
			cancelButtonText: 'No soy de una fundación',
			showCancelButton: true,
			cancelButtonColor: '#87db59',
			icon: 'question',
			allowOutsideClick: false,
			allowEscapeKey: false
		}).then(result => {
			if (result.isConfirmed) { // FUNDACION
				this.isSwapper = false;
				this.particularForm = this.fb.group({})
			} else { // SWAPPER
				this.fundacionForm = this.fb.group({})
			}
			this.loading = false
		})
	}

	getTiposDocumentos() {
		this.usuarioService.getTiposDocumentos().subscribe({
			next: (res: any) => { this.tiposDocumento = res; },
			error: (error) => { console.log('ERROR TIPOS DOC:', error); }
		})
	}

	async submit() {
		this.loadingSave = true;
		//console.log('registro', this.mainForm.value);
		if (this.mainForm.valid) {
			if (this.origin == 'resetPassword') {
				const body = {
					username: this.mainForm.controls['email'].value,
					nuevoPassword: this.mainForm.controls['password'].value,
					confirmarPassword: this.mainForm.controls['confirmPassword'].value
				}
				this.auth.resetPassword(body).subscribe({
					next: (res) => {
						//console.log(res);
						this.loadingSave = false;
					},
					error: (error) => {
						console.log('error:', error);
						this.loadingSave = false;
					}
				})
			} else {
				if (this.direccionForm.valid) {
					let user: any = {
						username: this.mainForm.controls['email'].value,
						email: this.mainForm.controls['email'].value,
						password: this.mainForm.controls['password'].value,
						telefono: this.mainForm.controls['telefono'].value,
						confirmPassword: this.mainForm.controls['confirmPassword'].value,
						direccion: {
							localidad: this.direccionForm.controls['localidad'].value.nombre,
							calle: this.direccionForm.controls['calle'].value.nombre,
							altura: this.direccionForm.controls['altura'].value,
							piso: this.direccionForm.controls['piso'].value,
							departamento: this.direccionForm.controls['departamento'].value,
							codigoPostal: this.direccionForm.controls['codigoPostal'].value,
						}
					};

					const apiUrl = `https://apis.datos.gob.ar/georef/api/direcciones?provincia=02&localidad=${encodeURIComponent(this.direccionForm.controls['localidad'].value.nombre)}&direccion=${encodeURIComponent(this.direccionForm.controls['calle'].value.nombre+this.direccionForm.controls['altura'].value)}`.replace(' ', '');
					const validAddress = (await fetch(apiUrl).then(response => response.json())).cantidad;
					if(!validAddress) {
						console.log('not valid address');
						Swal.fire('Dirección inválida', 'La dirección que ingresaste no es válida, por favor revisala y volvé a intentar.', 'error')
						this.loadingSave = false;
						return;
					}

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
							this.loadingSave = false;
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
							this.loadingSave = false;
						}
					}

					if (validDataForm) {
						if (this.origin == 'createAccount') {
							this.usuarioService.createUser(user).subscribe({
								next: (id_user: any) => {
									//console.log('next', id_user);
									this.id_user = id_user;
									this.messageWithTimer();
									this.loadingSave = false;
								},
								error: (e) => {
									console.error('error', e);
									this.loadingSave = false;
								},
							})
						} else {
							this.usuarioService.editUser(user).subscribe({
								next: (id_user: any) => {
									//console.log('next', id_user);
									this.showMessage('¡Éxito!', `Tus datos se editaron exitosamente.`, 'Genial!', 'edit', 'success')
									this.loadingSave = false;
								},
								error: (e) => {
									console.error('error', e);
									//this.showMessage('Error!', 'Ha ocurrido un error al editar la cuenta', 'OK', 'error', 'error')
									this.loadingSave = false;
								}
							})
						}
					}
				} else {
					this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
					this.loadingSave = false;
				}
			}
		} else {
			this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
			this.loadingSave = false;
		}
	}

	showMessage(title: string, text: string, confirm: string, origin: string, icon: 'success' | 'error' | 'warning') {
		Swal.fire({
			title, text, confirmButtonText: confirm, icon, allowOutsideClick: icon == 'success' ? false : true
		}).then(({ isConfirmed }) => {
			if (isConfirmed && icon != 'error') {
				if (origin == 'ir_a_home') this.router.navigate(['home'])
				else if (origin == 'edit') this.router.navigate(['mi-perfil'])
			}
		})
	}

	validarConfirmPassword(): ValidatorFn {
		return (control: AbstractControl): { [key: string]: any } | null => {
			if (control.value) {
				const password = this.mainForm.controls['password'].value;
				const confirmPassword = this.mainForm.controls['confirmPassword'].value;
				if (password && confirmPassword && password != confirmPassword) return { differentPassword: true };
				else {
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

	togglePassword() {
		this.passwordType = this.passwordType === 'text' ? 'password' : 'text';
		this.passwordIcon = this.passwordIcon === 'visibility' ? 'visibility_off' : 'visibility';
	}

	refreshValidity() {
		this.mainForm.updateValueAndValidity()
	}

	formatDocument(event: any) {
		this.particularForm.controls['nroDocumento'].enable();
		switch (event.value) {
			case 'DNI': //
				this.particularForm.controls['nroDocumento'].addValidators(Validators.pattern(/^(?!0+$)[1-9]\d{6,7}$/))
				break;
			default:
				this.particularForm.controls['nroDocumento'].addValidators(Validators.pattern(/^(?!0+$)[1-9]\d{6,7}$/))
				break;
		}
	}

	formatearCUIT() {
		this.fundacionForm.get('cuit')?.setValue(this.cuitPipe.transform(this.fundacionForm.get('cuit')?.value)); // Esto fuerza el cambio del valor
		this.cdr.detectChanges(); // Detectamos los cambios manualmente
	}

	messageWithTimer() {
		let countdownTime = 60;
		Swal.fire({
			title: '¡Gracias por registrarte!',
			text: `Te enviamos un email a la cuenta que ingresaste, con un código para verificar tu cuenta. Por favor ingresalo a continuación. Si no verificás la cuenta ahora, podrás hacerlo la próxima vez que inicies sesión.`,
			icon: 'success',
			showDenyButton: true,
			allowOutsideClick: false, allowEscapeKey: false,
			denyButtonText: 'Reenviar email en 60 segundos',
			denyButtonColor: '#ae59db', confirmButtonColor: '#87db59',
			confirmButtonText: 'Confirmar',
			input: 'text',
			didOpen: () => {
				const denyButton: any = document.querySelector('.swal2-deny');
				const countdownInterval = setInterval(() => {
					countdownTime--;
					if (denyButton) {
						denyButton.setAttribute('style', 'background-color: #a3a3a3;');
						denyButton.textContent = `Reenviar email en ${countdownTime} segundos`;
					}
					if (countdownTime <= 0 && denyButton) {
						denyButton.disabled = false;
						denyButton.setAttribute('style', 'background-color: #9c30d4;');
						denyButton.textContent = `Reenviar email`;
						clearInterval(countdownInterval);
					}
				}, 1000);
			},
			preDeny: () => {
				console.log('pre deny', countdownTime);
				if (this.id_user && countdownTime == 0) {
					this.auth.sendEmailAgain(this.id_user).subscribe({
						next: (res) => {
							console.log(res);
							this.snackbar.open('Se envió el nuevo mail!', '', {
								horizontalPosition: 'center',
								verticalPosition: 'top',
								duration: 3000
							})
						}
					})
				}
				return false;
			},
			reverseButtons: true
		}).then(({ isConfirmed, value }) => {
			if (isConfirmed && this.id_user) {
				this.usuarioService.confirmarCuenta(this.id_user, value).subscribe({
					next: (res) => {
						//console.log(res);
						Swal.fire('Excelente!', 'Tu cuenta fue verificada, ya podes usar Ecoswap!', 'success')
						this.closeSwal = false;
						this.router.navigate(['/login'])
					},
					error: (error) => {
						console.log(error);
						this.router.navigate(['/login'])
					}
				});
				//else Swal.fire('Error!', 'Ocurrió un error al activar tu cuenta. Por favor intentalo más tarde', 'error')
			}
		})
	}

	displayItem(item: any): string {
		return item ? capitalizeFirstLetter(item.nombre) : '';
	}

	validateItem(control: AbstractControl): { [key: string]: any } | null {
		const selectedValue = control.value;
		const parentFormGroup = control.parent;
		if (!parentFormGroup) return { invalidSelection: true };
		const name = Object.keys(parentFormGroup.controls).find((key) => control === parentFormGroup.get(key));

		if (selectedValue && this.localidades) {
			const isValid = (name == 'localidad' ? this.localidades : this.calles)
				.some((item) => item.nombre.toUpperCase() == (selectedValue.nombre || selectedValue).toUpperCase());
			return isValid ? null : { invalidSelection: true };
		}
		return null;
	}


	getLocalidades() {
		const locValue = this.direccionForm.controls['localidad'].value;
		if (locValue.length == 0) {
			this.direccionForm.controls['calle'].disable();
			this.direccionForm.controls['altura'].disable()
			this.direccionForm.controls['piso'].disable()
			this.direccionForm.controls['departamento'].disable()
			this.direccionForm.controls['codigoPostal'].disable()
		}
		else if (locValue.length >= 3) {
			const apiUrl = 'https://apis.datos.gob.ar/georef/api/localidades?orden=id&provincia=02&nombre=' + encodeURIComponent(locValue)
			fetch(apiUrl).then(response => response.json()).then(data => {
				this.localidades = data.localidades
			}).catch(error => console.error(error));
		}
	}

	getCalles() {
		const calleInput = this.direccionForm.controls['calle'].value;
		const locInput = this.direccionForm.controls['localidad'].value.departamento.id;

		if (calleInput.length == 0) {
			this.direccionForm.controls['altura'].disable()
			this.direccionForm.controls['piso'].disable()
			this.direccionForm.controls['departamento'].disable()
			this.direccionForm.controls['codigoPostal'].disable()
		}
		if (calleInput.length >= 3) {
			const apiUrl = 'https://apis.datos.gob.ar/georef/api/calles?orden=id&provincia=02&departamento=' + encodeURIComponent(locInput) + '&nombre=' + encodeURIComponent(calleInput)
			fetch(apiUrl).then(response => response.json()).then(data => {
				this.calles = data.calles
			}).catch(error => console.error(error));
		}
	}

	onLocSelected(event: any) {
		if (event) this.direccionForm.controls['calle'].enable()
	}

	onCalleSelected(event: any) {
		if (event) {
			this.direccionForm.controls['altura'].enable()
			this.direccionForm.controls['piso'].enable()
			this.direccionForm.controls['departamento'].enable()
			this.direccionForm.controls['codigoPostal'].enable()
		}
	}


}

function isValidCUIT(control: FormControl): { [key: string]: boolean } | null {
	const cuit = control.value;

	if (!cuit) return null; // Handle empty input

	const regex = /^\d{2}-\d{8,9}-\d{1,2}$/; // Ensure the CUIT matches the format: 11-11111111-1 or 11-11111111-11
	if (!regex.test(cuit)) return { 'invalidCUIT': true };

	const numericPart: string = cuit.replace(/-/g, ''); // Extract the numeric part for calculation
	const verifierDigit = parseInt(numericPart.charAt(numericPart.length - 1)); // Calculate the verifier digit
	const factors = [5, 4, 3, 2, 7, 6, 5, 4, 3, 2];
	let sum = 0;

	for (let i = 0; i < 10; i++) {
		sum += parseInt(numericPart.charAt(i)) * factors[i];
	}

	const remainder = sum % 11;
	const calculatedVerifierDigit = remainder === 0 ? 0 : 11 - remainder;

	if (calculatedVerifierDigit !== verifierDigit) return { 'invalidCUIT': true };
	return null; // CUIT is valid
}

function capitalizeFirstLetter(str: string) {
	if (str) return str.toLowerCase().split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
	else return str;
}