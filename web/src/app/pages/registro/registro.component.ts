import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import Swal from 'sweetalert2';
import { DateAdapter } from '@angular/material/core';
import { UsuarioService } from 'src/app/services/usuario.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
	selector: 'app-registro',
	templateUrl: './registro.component.html',
	styleUrls: ['./registro.component.scss'],
	providers: [{ provide: MAT_DATE_LOCALE, useValue: 'en-GB' },]
})
export class RegistroComponent {
	mainForm: FormGroup;
	particularForm: FormGroup;
	fundacionForm: FormGroup;

	isSwapper: boolean = true;
	loading = true;

	screenWidth: number;

	resetPassword: boolean = false;

	constructor(private fb: FormBuilder, private dateAdapter: DateAdapter<Date>,
		private usuarioService: UsuarioService, private route: ActivatedRoute,
		private auth: AuthService) {
		this.mainForm = fb.group({
			//username: [''], TODO: POR AHORA USO EMAIL
			email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/)]],
			telefono: [''],
			password: ['', [Validators.required, this.validarConfirmPassword(), Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,32}$/)]],
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

		this.screenWidth = (window.innerWidth > 0) ? window.innerWidth : screen.width;
		this.dateAdapter.setLocale('es-AR'); // DD/mm/YYYY

		route.url.subscribe(a => {
			if (a[0].path == 'reset-password') {
				this.resetPassword = true;
			} else {
				this.mainForm.controls['telefono'].addValidators(Validators.required)
			}
		});

	}

	ngOnInit(): void {
		if (!this.resetPassword) this.selectTipoPerfil();
		else this.loading = false;
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

	crearCuenta() {
		console.log('registro', this.mainForm.value);
		if (this.mainForm.valid) {
			if (this.resetPassword) {
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
				let user: any = {
					username: this.mainForm.controls['email'].value,
					email: this.mainForm.controls['email'].value,
					password: this.mainForm.controls['password'].value,
					telefono: this.mainForm.controls['telefono'].value,
					confirmPassword: this.mainForm.controls['confirmPassword'].value,
					direccion: {
						altura: 'test',
						codigoPostal: 'test',
						direccion: 'test',
						departamento: 'test',
						piso: 'test'
					}
				};

				if (this.isSwapper) {
					if (this.particularForm.valid) {
						user.particular = {
							fechaNacimiento: this.particularForm.controls['fechaNacimiento'].value,
							dni: this.particularForm.controls['nroDocumento'].value, // TODO: CAMBIAR A NRO DOC
							cuil: this.particularForm.controls['nroDocumento'].value,
							nombre: this.particularForm.controls['nombre'].value,
							apellido: this.particularForm.controls['apellido'].value,
							tipoDocumento: this.particularForm.controls['tipoDocumento'].value
						}
					} else {
						this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
					}
				} else {
					if (this.fundacionForm.valid) {
						user.fundacion = {
							nombre: this.fundacionForm.controls['nombre'].value,
							cuil: this.fundacionForm.controls['cuit'].value //TODO: que en back sea cuit
						}
					} else {
						this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
					}
				}

				console.log(user);
				this.usuarioService.createUser(user).subscribe({
					next: (v: any) => {
						console.log('next', v);
						// IF OK THEN MESSAGE:
						this.showMessage('¡Gracias por registrarte!',
							`Te enviamos un email a la cuenta que ingresaste,
							para que confirmes tu cuenta.`,
							'No recibí el email', 'send_again', 'success')
					},
					error: (e) => {
						console.error('error', e);
						this.showMessage('Error!', 'Ha ocurrido un error al crear la fundación', 'OK', 'error', 'error')
					},
					complete: () => console.info('complete')
				})
			}
		} else {
			this.showMessage('¡Campos incorrectos!', 'Por favor, revisá los campos y volvé a intentar', 'OK', '', 'error')
		}
	}

	showMessage(title: string, text: string, confirm: string, origin: string,
		icon: 'success' | 'error' | 'warning') {
		Swal.fire({
			title,
			text,
			confirmButtonText: confirm,
			icon
		}).then(({ isConfirmed }) => {
			if (origin == 'send_again' && isConfirmed) {
				// TODO: RESEND EMAIL
			}
		})
	}

	validarConfirmPassword(): ValidatorFn {
		return (control: AbstractControl): { [key: string]: any } | null => {
			if (control.value) {
				const password = this.mainForm.controls['password'].value;
				const confirmPassword = this.mainForm.controls['confirmPassword'].value;

				if (password != confirmPassword) {
					return { differentPassword: true };
				} else {
					this.mainForm.controls['password'].setErrors(null);
					this.mainForm.controls['confirmPassword'].setErrors(null);
				}
			}
			return null; // Valor válido
		};
	}
}
