import { Component, Input } from '@angular/core';
import { Streamer } from '../../models/Streamer';
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-streamer-card',
  standalone: true,
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './streamer-card.component.html',
  styleUrls: ['./streamer-card.component.css']
})
export class StreamerCardComponent {
  @Input() streamer!: Streamer;

  constructor() {
  }
}
