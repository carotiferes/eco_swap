import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
const properties = require('../core/properties.json')

const URL = properties.URI;

@Injectable()
export class HttpBackEnd {
  constructor(private httpClient: HttpClient) {}

  get(endpoint: string, params?: any) {
    return this.httpClient.get(URL + endpoint, { params });
  }

  post(endpoint: string, body: any) {
    return this.httpClient.post(URL + endpoint, body);
  }

  async uploadFile(endpoint: string, file: any) {
    let formData = new FormData();
    formData.append('file', file);

    const response = this.post(endpoint, formData);
    const data = await lastValueFrom(response);
    return data;
  }

  put(endpoint: string, body: any) {
    return this.httpClient.put(URL + endpoint, body);
  }

  patch(endpoint: string, body: any) {
    return this.httpClient.patch(URL + endpoint, body);
  }

  delete(endpoint: string) {
    return this.httpClient.delete(URL + endpoint);
  }

  async postImg(endpoint: string, img: any, body?: any) {
    let formData = new FormData();
    formData.append('image', img);
    if (body) {
      if (body.dest) formData.append('dest', body.dest);
      if (body.id) formData.append('id', body.id);
    }
    const response = await this.post(endpoint, formData).toPromise();
    const data = response;
    return data;
  }
}
