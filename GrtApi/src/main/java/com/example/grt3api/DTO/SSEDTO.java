package com.example.grt3api.DTO;

import java.util.List;

public class SSEDTO {
    private List<StreamerDTO> streamers;
    private String charityAmount;

    public SSEDTO(List<StreamerDTO> streamers, String charityAmount) {
        this.streamers = streamers;
        this.charityAmount = charityAmount;
    }

    public List<StreamerDTO> getStreamers() {
        return streamers;
    }

    public String getCharityAmount() {
        return charityAmount;
    }
}
