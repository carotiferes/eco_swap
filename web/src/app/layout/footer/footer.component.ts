import { Component } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';

@Component({
	selector: 'app-footer',
	templateUrl: './footer.component.html',
	styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

	showFooter: boolean = false;

	constructor(private router: Router, private activatedRoute: ActivatedRoute) {
		// Subscribe to router events to monitor route changes
		this.router.events.subscribe(event => {
			if (event instanceof NavigationEnd) {
				// Determine the current route and set showFooter accordingly
				const currentRoute = this.activatedRoute.firstChild;
				if (currentRoute) {
					setTimeout(() => {
						this.showFooter = !currentRoute.snapshot.data['hideFooter'];
					}, 1500);
				}
			}
		});
	}
}
