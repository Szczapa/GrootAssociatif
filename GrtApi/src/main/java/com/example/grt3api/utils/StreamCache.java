package com.example.grt3api.utils;

import com.example.grt3api.DTO.SSEDTO;
import com.example.grt3api.DTO.StreamerDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class StreamCache {
    private static final Logger logger = LoggerFactory.getLogger(StreamCache.class);

    private final AtomicReference<List<StreamerDTO>> streamerList = new AtomicReference<>(Collections.emptyList());
    private final AtomicReference<String> charityAmount = new AtomicReference<>("0.00 $");

    private FluxSink<SSEDTO> fluxSink;
    private final Flux<SSEDTO> sseFlux;

    public StreamCache() {
        this.sseFlux = Flux.<SSEDTO>create(sink -> this.fluxSink = sink).share();
    }

    public void updateStreamers(List<StreamerDTO> newStreamers, String newCharityAmount) {

        if (newStreamers == null) {
            logger.warn("`newStreamers` est null, utilisation d'une liste vide par défaut.");
            newStreamers = Collections.emptyList();
        }
        if (newCharityAmount == null) {
            logger.warn("`newCharityAmount` est null, utilisation de la valeur par défaut : '0.00 $'.");
            newCharityAmount = "0.00 $";
        }

        boolean hasChanged = false;

        List<StreamerDTO> currentList = streamerList.get();
        if (!currentList.equals(newStreamers)) {
            logger.info("🔄 Différence détectée dans la liste des streamers !");
            streamerList.set(newStreamers);
            hasChanged = true;
        }


        if (!charityAmount.get().equals(newCharityAmount)) {
            logger.info("💰 Le montant de charité a changé (Avant: {}, Après: {}).",
                    charityAmount.get(), newCharityAmount);
            charityAmount.set(newCharityAmount);
            hasChanged = true;
        }

        if (hasChanged) {
            if (fluxSink != null) {
                logger.info("📡 Envoi d'un nouvel événement SSE.");
                fluxSink.next(new SSEDTO(newStreamers, newCharityAmount));
            } else {
                logger.warn("fluxSink n'est pas initialisé. Aucun événement SSE ne sera envoyé.");
            }
        }
    }

    public Flux<SSEDTO> getSSEFlux() {
        return Flux.defer(() -> {
            List<StreamerDTO> currentStreamers = streamerList.get();
            String currentCharityAmount = charityAmount.get();

            return (currentStreamers != null)
                    ? Flux.just(new SSEDTO(currentStreamers, currentCharityAmount)).concatWith(sseFlux)
                    : sseFlux;
        });
    }
}
