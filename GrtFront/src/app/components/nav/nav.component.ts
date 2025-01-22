import {Component, ElementRef, signal, ViewChild} from '@angular/core';
import {NgClass, NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgClass
  ],
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent {
  @ViewChild('burger') burger!: ElementRef;
  @ViewChild('navLinks') navLinks!: ElementRef;
  @ViewChild('navBar') navBar!: ElementRef;

  isNavActive = signal(false);

  activeBurger() {
    this.isNavActive.set(!this.isNavActive());
  }
}
