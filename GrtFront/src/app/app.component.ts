import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NavComponent} from "./components/nav/nav.component";
import {HearderComponent} from "./components/hearder/hearder.component";
import {AssociationComponent} from "./components/association/association.component";
import {HistoryComponent} from "./components/history/history.component";
import {StreamerComponent} from "./components/streamer/streamer.component";
import {EventStatsComponent} from "./components/event-stats/event-stats.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavComponent, HearderComponent, AssociationComponent, HistoryComponent, StreamerComponent, EventStatsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'FrontEnd';
}
