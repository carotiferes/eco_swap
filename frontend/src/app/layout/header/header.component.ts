import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  @Output() sidebarEvent = new EventEmitter<any>();
  imgUser: any;
  logoUrl: string = '';

  constructor(private router: Router) {}

  @Input() userName: string = 'Administrator';

  hasPhoto: boolean = false;

  ngOnInit(): void {  }

  showSidebar() {
    this.sidebarEvent.emit();
  }

  goToHome(){
    this.router.navigateByUrl('home');
  }

  goToProfile(){
    this.router.navigateByUrl('profile')
  }
}
