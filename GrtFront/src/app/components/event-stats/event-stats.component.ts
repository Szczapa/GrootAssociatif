import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import { StreamService } from "../../services/stream.service";
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-event-stats',
  standalone: true,
  templateUrl: './event-stats.component.html',
  styleUrls: ['./event-stats.component.css']
})
export class EventStatsComponent implements OnInit, OnDestroy {
  lottery: number = 0;
  actual_lottery: number = 0;
  online_streamers: number = 0;
  actual_online_streamers: number = 0;
  private sseSubscription!: Subscription;

  constructor(private streamService: StreamService, private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.sseSubscription = this.streamService.getStreamData().subscribe({
      next: (sseData) => {
        const newLotteryValue = Number(sseData.charityAmount.replace(/[^0-9,]/g, '').replace(',', '.'));

        if (this.lottery !== newLotteryValue) {
          this.lottery = newLotteryValue;
          this.lottery_animation();
        }

        const onlineCount = sseData.streamers.filter((s: any) => s.online).length;
        if (this.online_streamers !== onlineCount) {
          this.online_streamers = onlineCount;
          this.online_streamers_animation();
        }
      },
      error: (error) => {
        console.error('Erreur SSE:', error);
      }
    });

    setTimeout(() => {
      this.lottery_animation();
      this.online_streamers_animation();
    }, 0);
  }

  ngOnDestroy() {
    this.sseSubscription.unsubscribe();
  }

  lottery_animation() {
    if (this.actual_lottery < this.lottery) {
      this.actual_lottery += Math.max(1, Math.floor((this.lottery - this.actual_lottery)));
      setTimeout(() => this.lottery_animation(), 20);
    }
  }

  online_streamers_animation() {
    if (this.actual_online_streamers < this.online_streamers) {
      this.actual_online_streamers += 1;
      this.cdr.detectChanges();
      setTimeout(() => this.online_streamers_animation(), 20);
    } else if (this.actual_online_streamers > this.online_streamers) {
      this.actual_online_streamers -= 1;
      this.cdr.detectChanges();
      setTimeout(() => this.online_streamers_animation(), 20);
    } else {
      this.actual_online_streamers = this.online_streamers;
    }
  }

}
