package com.example.grt3api.controller;

import com.example.grt3api.DTO.SSEDTO;
import com.example.grt3api.utils.StreamCache;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class SSEController {
    private final StreamCache streamCache;

    public SSEController(StreamCache streamCache) {
        this.streamCache = streamCache;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SSEDTO> connect() {
        System.out.println("Nouvelle connexion SSE");
        return streamCache.getSSEFlux()
                .doOnCancel(() -> System.out.println("Le client a fermé la connexion."))
                .doFinally(signalType -> System.out.println("Fin du flux : " + signalType));
    }
}
