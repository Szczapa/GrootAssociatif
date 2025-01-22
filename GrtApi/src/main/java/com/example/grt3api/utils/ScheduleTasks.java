package com.example.grt3api.utils;

import com.example.grt3api.DTO.StreamerDTO;
import com.example.grt3api.service.StreamlabsService;
import com.example.grt3api.service.TwitchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleTasks {
    private final StreamCache streamCache;
    private final TwitchService twitchService;
    private final StreamlabsService streamlabsService;

    public ScheduleTasks(TwitchService twitchService, StreamCache streamCache, StreamlabsService streamlabsService) {
        this.twitchService = twitchService;
        this.streamCache = streamCache;
        this.streamlabsService = streamlabsService;
    }

    @Scheduled(fixedRate = 45000)
    public void updateStreamersAndCharity() throws Exception {
        List<StreamerDTO> streamerList = twitchService.getStreamerOnline();
        String donation = streamlabsService.getCharityAmount();
        streamCache.updateStreamers(streamerList, donation);
    }
}
