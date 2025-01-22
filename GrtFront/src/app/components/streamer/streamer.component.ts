import {ChangeDetectorRef, Component, OnInit,} from '@angular/core';
import {StreamerCardComponent} from "../streamer-card/streamer-card.component";
import {Streamer} from "../../models/Streamer";
import { StreamService } from "../../services/stream.service";
import { Subscription } from 'rxjs';

@Component({

  selector: 'app-streamer',
  standalone: true,
  templateUrl: './streamer.component.html',
  styleUrls: ['./streamer.component.css'],

  imports: [
    StreamerCardComponent
  ]
})
export class StreamerComponent implements OnInit {
  StreamerData: Streamer[] = [
    {
      key: 'aeldras',
      name: 'Aeldras',
      status: false,
      description: `Nagano Prefecture, set within the majestic Japan Alps, is a cultural treasure trove
                with its historic shrines and temples, particularly the famous Zenkō-ji. The region is also
                a hotspot for skiing and snowboarding, offering some of the country's best powder.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'arbrawl',
      name: 'Arbrawl',
      status: false,
      description: `Default description for Arbrawl.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'atrandos',
      name: 'Atrandos',
      status: false,
      description: `Tucked away in the Switzerland Alps, Saint Antönien offers an idyllic retreat
                for those seeking tranquility and adventure alike. It's a hidden gem for backcountry skiing in
                winter and boasts lush trails for hiking and mountain biking during the warmer months.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'captn_universe',
      name: 'Captn_universe',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'ds_freyja',
      name: 'DS Freyja',
      status: false,
      description: `Default description for DS Freyja.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'eindall',
      name: 'Eindall',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'feli_matamune',
      name: 'Feli Matamune',
      status: false,
      description: `Nagano Prefecture, set within the majestic Japan Alps, is a cultural treasure trove
                with its historic shrines and temples, particularly the famous Zenkō-ji. The region is also
                a hotspot for skiing and snowboarding, offering some of the country's best powder.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'frenchi',
      name: 'Frenchi',
      status: false,
      description: `Nagano Prefecture, set within the majestic Japan Alps, is a cultural treasure trove
                with its historic shrines and temples, particularly the famous Zenkō-ji. The region is also
                a hotspot for skiing and snowboarding, offering some of the country's best powder.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'foxy9376',
      name: 'Foxy9376',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'hapygames',
      name: 'Hapygames',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'imunic0rn',
      name: 'Imunic0rn',
      status: false,
      description: `Default description for Imunic0rn.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'kimabruti',
      name: 'Kimabruti',
      status: false,
      description: `Default description for Kimabruti.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'kikifred',
      name: 'KikiFred',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'lougafr',
      name: 'Lougafr',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'lywen__',
      name: 'Lywen',
      status: false,
      description: `Default description for Lywen.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'murphan',
      name: 'Murphan',
      status: false,
      description: `Default description for Lywen.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'naelyeee',
      name: 'Naelyeee',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'patou689',
      name: 'Patou689',
      status: false,
      description: `Nagano Prefecture, set within the majestic Japan Alps, is a cultural treasure trove
                with its historic shrines and temples, particularly the famous Zenkō-ji. The region is also
                a hotspot for skiing and snowboarding, offering some of the country's best powder.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'rekriot',
      name: 'Rekriot',
      status: false,
      description: `Default description for Rekriot.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'sungshiro',
      name: 'Sungshiro',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'violineheartilly',
      name: 'Violine',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'warvextw',
      name: 'Warvextw',
      status: false,
      description: `Default description for Naelyeee.`,
      image: 'assets/images/offline.jpg'
    },
    {
      key: 'white50',
      name: 'White50',
      status: false,
      description: `Default description for White50.`,
      image: 'assets/images/offline.jpg'
    }
  ];

  private sseSubscription!: Subscription;

  constructor(private streamService: StreamService, private cdr: ChangeDetectorRef) {
    console.log('StreamerComponent initialized');
  }

  ngOnInit() {
    this.sseSubscription = this.streamService.getStreamData().subscribe({
      next: (streamers) => {

         this.updateStreamerStatus(streamers);
      },
      error: (error) => {
        console.error('Sse Error:', error);
      }
    });
  }

  ngOnDestroy() {
    this.sseSubscription.unsubscribe();
  }

  updateStreamerStatus(sseData: any) {
    console.log("updateStreamerStatus", sseData);
    if (!sseData || !Array.isArray(sseData.streamers)) {
      console.error('SSE Format Error');
      return;
    }

    this.StreamerData = this.StreamerData.map(streamer => {
      const updatedStreamer = sseData.streamers.find((s: any) => s.name.toLowerCase() === streamer.key.toLowerCase());
      return updatedStreamer
        ? {
          ...streamer,
          status: updatedStreamer.online,
          image: updatedStreamer.url && updatedStreamer.url !== "" ? updatedStreamer.url : streamer.image
        }
        : streamer;
    });

    this.cdr.detectChanges();
  }
}

