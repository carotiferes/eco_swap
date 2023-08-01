import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
const properties = require('../core/properties.json')

/* const URI_ms_usuarios = properties.URI_ms_usuarios;
const URI_ms_autenticacion = properties.URI_ms_autenticacion; */

@Injectable()
export class HttpBackEnd {

	//URL: string = URI_ms_usuarios;

	constructor(private httpClient: HttpClient) { }

	get(urlName: string, endpoint: string, params?: any) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.get(URL + endpoint, { params });
	}

	post(urlName: string, endpoint: string, body: any) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.post(URL + endpoint, body);
	}

	async uploadFile(urlName: string, endpoint: string, file: any) {
		let formData = new FormData();
		formData.append('file', file);

		const response = this.post(urlName, endpoint, formData);
		const data = await lastValueFrom(response);
		return data;
	}

	put(urlName: string, endpoint: string, body: any) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.put(URL + endpoint, body,);
	}

	patch(urlName: string, endpoint: string, body: any) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.patch(URL + endpoint, body);
	}

	delete(urlName: string, endpoint: string) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.delete(URL + endpoint);
	}

	async postImg(urlName: string, endpoint: string, img: any, body?: any) {
		let formData = new FormData();
		formData.append('image', img);
		if (body) {
			if (body.dest) formData.append('dest', body.dest);
			if (body.id) formData.append('id', body.id);
		}
		const response = await this.post(urlName, endpoint, formData).toPromise();
		const data = response;
		return data;
	}

	private getUrlByName(urlName: string): string {
		console.log(properties);
		
		console.log(urlName, properties[urlName]);
		
		return properties[urlName] || 'http://localhost:8080/';
	}
}
