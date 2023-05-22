import { Component, EventEmitter, OnInit, Output } from '@angular/core';
const menuData = require('../../data/menu.json')

@Component({
	selector: 'app-sidebar',
	templateUrl: './sidebar.component.html',
	styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent implements OnInit {
	@Output() sidebarEvent = new EventEmitter<any>();

	menus = menuData;

	constructor() { }

	ngOnInit(): void { }

	showSidebar() {
		this.sidebarEvent.emit();
	}
}
