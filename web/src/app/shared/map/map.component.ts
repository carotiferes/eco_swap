import { Component, Inject, OnInit, } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as L from 'leaflet';

@Component({
	selector: 'app-map',
	templateUrl: './map.component.html',
	styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

	private map!: L.Map;

	constructor(@Inject(MAT_DIALOG_DATA) public data: any,) { }

	ngOnInit(): void {
		this.initMap();
	}

	private initMap(): void {
		// Initialize a map instance and set its center and zoom level
		this.map = L.map('map').setView([this.data.lat, this.data.lon], 13);
		// Create a custom icon for the marker
		const customIcon = L.icon({
			iconUrl: 'assets/pin-9-64.png',
			iconSize: [48, 48], // Set the icon size
			iconAnchor: [24, 48], // Set the anchor point
		});

		// Create a marker with the custom icon and add it to the map
		const marker = L.marker([this.data.lat, this.data.lon], { icon: customIcon }).addTo(this.map);

		// Add a tile layer (you can use different tile providers)
		L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
			attribution: '© OpenStreetMap contributors'
		}).addTo(this.map);
	}
}
