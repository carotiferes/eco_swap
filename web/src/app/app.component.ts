import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app_eco_swap';

  pageType: 'donaciones' | 'trueques' | 'default' = 'default'; // Default page

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        // Update the currentPage based on the route
        this.pageType = this.getCurrentPage(event.urlAfterRedirects);
		console.log('current', this.pageType);
		
      }
    });
  }

  getCurrentPage(url: string) {
	console.log(url.split('/'));
	url = url.split('/')[1];
	const donacionesURLs = ['colectas', 'colecta', 'mis-colectas', 'form-colecta', 'form-donacion', 
		'donacion', 'mis-donaciones']
	const truequesURLs = ['publicaciones', 'form-publicacion', 'mis-publicaciones', 
		'form-colecta', 'form-donacion', 'publicacion',]
    // Logic to determine the current page based on the URL
    if (donacionesURLs.includes(url)) {
      return 'donaciones';
    } else if (truequesURLs.includes(url)) {
      return 'trueques';
    } else {
      return 'default'; // Set a default value or class
    }
  }

}
