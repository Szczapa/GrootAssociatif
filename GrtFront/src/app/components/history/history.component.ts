import {Component, OnInit} from '@angular/core';
import {Story} from "../../models/Story";


@Component({
  selector: 'app-history',
  standalone: true,
  imports: [],
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent implements OnInit {
  stories: Story[] = [];

  ngOnInit() {
    this.stories = [
      {
        id: 1,
        title: 'Groot Rêves',
        description: '5500€',
        logo: ''
      },
      {
        id: 2,
        title: 'Groot Associatif',
        description: '11 070€',
        logo: ''
      },
      {
        id: 3,
        title: 'Rêves',
        description: '27 022€',
        logo: ''
      },
      {
        id: 4,
        title: 'GRT3 Reborn',
        description: 'En cours..',
        logo: ''
      }
    ];
  }

}
