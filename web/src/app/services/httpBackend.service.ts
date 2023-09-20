import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { environment } from 'src/environments/environment';
const properties = require('../core/properties.json')

@Injectable()
export class HttpBackEnd {

	constructor(private httpClient: HttpClient) {
		console.log('ENVIRONMENT', environment);
		
	}

	get(urlName: string, endpoint: string, params?: any) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.get(URL + endpoint, { params });
	}

	getImg(urlName: string, endpoint: string, params?: any) {
		const URL = this.getUrlByName(urlName);
		return this.httpClient.get(URL + endpoint, { params, responseType: 'blob' });
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

	getUrlByName(urlName: string): string {
		//console.log('ENV URL', environment.apiUrl);
		
		return environment.apiUrl + (properties[urlName] || '8080/');
	}
}
