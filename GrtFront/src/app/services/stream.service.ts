import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators'; // Import de shareReplay
import { environment } from "../../environments/environment";

interface SSEData {
  streamers: { name: string; online: boolean }[];
  charityAmount: string;
}

@Injectable({
  providedIn: 'root'
})
export class StreamService {
  private eventSource!: EventSource;
  private apiUrl: string = environment.apiUrl;
  private reconnectDelay = 45000;
  private reconnecting = false;

  // Utilisation d'un flux partagé pour éviter des connexions multiples
  private sseObservable!: Observable<SSEData>;

  constructor(private zone: NgZone) {
    // Initialisation du flux partagé
    this.sseObservable = new Observable<SSEData>((observer) => {
      const connect = () => {
        if (this.reconnecting) return;
        this.reconnecting = true;

        this.zone.runOutsideAngular(() => {
          this.eventSource = new EventSource(this.apiUrl + '/connect');

          this.eventSource.onmessage = (event) => {
            try {
              const sseData: SSEData = JSON.parse(event.data);
              this.zone.run(() => observer.next(sseData));
            } catch (err) {
              console.error('Error parsing SSE data:', err, 'Raw data:', event.data);
            }
          };

          this.eventSource.onerror = () => {
            console.error('SSE connection error');
            this.eventSource.close();
            setTimeout(() => {
              this.reconnecting = false;
              connect();
            }, this.reconnectDelay);
          };
        });
      };

      connect();

      return () => {
        console.log('Closing SSE connection...');
        this.eventSource.close();
        this.reconnecting = false;
      };
    }).pipe(shareReplay(1)); // Partage le flux pour tous les abonnés
  }

  getStreamData(): Observable<SSEData> {
    return this.sseObservable;
  }
}
