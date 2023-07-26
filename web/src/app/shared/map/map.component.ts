import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
//import { Map, Marker, NavigationControl } from 'maplibre-gl';

@Component({
	selector: 'app-map',
	templateUrl: './map.component.html',
	styleUrls: ['./map.component.scss']
})
export class MapComponent /* implements AfterViewInit */ {

	/* map: Map | undefined;
	@ViewChild('map')
	private mapContainer!: ElementRef<HTMLElement>;

	ngAfterViewInit() {
		const initialState = { lng: 139.753, lat: 35.6844, zoom: 14 };

		this.map = new Map({
			container: this.mapContainer.nativeElement,
			style: `https://api.maptiler.com/maps/streets-v2/style.json?key=PVhSIsIisRAZzZjw4a6d`,
			center: [ -58.44,-34.57],
  			zoom: 14,
		});

		this.map.addControl(new NavigationControl(), 'top-right');
		new Marker({ color: "#FF0000" }).setLngLat([ -58.44,-34.57]).addTo(this.map);

	} */
}
