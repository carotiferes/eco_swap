import { Component } from '@angular/core';
import { TruequeComponent } from 'src/app/pages/trueque/trueque.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

	searchText: string = '';
	tabs: {label: string, content: any}[] = [
		{label: 'TRUEQUES', content: TruequeComponent},
		{label: 'DONACIONES', content: TruequeComponent},
		{label: 'MI PERFIL', content: TruequeComponent},
	]

}
