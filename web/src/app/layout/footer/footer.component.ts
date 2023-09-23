import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

	showFooter: boolean = false;

	constructor() {
		setTimeout(() => {
			this.showFooter = true;
		}, 2000);
	}
}
