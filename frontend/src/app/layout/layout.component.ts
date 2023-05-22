import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
})
export class LayoutComponent implements OnInit {
  @ViewChild('drawer') drawer: any;

  constructor() {
  }

  ngOnInit(): void {
  }

  sidebarOn() {
    this.drawer.toggle();
  }
}
