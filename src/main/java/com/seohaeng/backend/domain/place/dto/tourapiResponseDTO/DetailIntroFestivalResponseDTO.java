package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DetailIntroFestivalResponseDTO implements DetailIntroResponse {
    private Response response;
    
    @Override
    public String getContentId() {
        return response != null && response.body != null && response.body.items != null && 
               !response.body.items.item.isEmpty() ? response.body.items.item.get(0).contentid : null;
    }
    
    @Override
    public String getContentTypeId() {
        return response != null && response.body != null && response.body.items != null && 
               !response.body.items.item.isEmpty() ? response.body.items.item.get(0).contenttypeid : null;
    }

    @Getter
    @Setter
    public static class Response {
        private Header header;
        private Body body;
    }

    @Getter
    @Setter
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Getter
    @Setter
    public static class Body {
        private Items items;
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    @Getter
    @Setter
    public static class Items {
        private List<FestivalIntroItem> item;
    }

    @Getter
    @Setter
    public static class FestivalIntroItem {
        private String contentid;
        private String contenttypeid;
        private String sponsor1;
        private String sponsor1tel;
        private String sponsor2;
        private String sponsor2tel;
        private String eventenddate;
        private String playtime;
        private String eventplace;
        private String eventhomepage;
        private String agelimit;
        private String bookingplace;
        private String placeinfo;
        private String subevent;
        private String program;
        private String eventstartdate;
        private String usetimefestival;
        private String discountinfofestival;
        private String spendtimefestival;
        private String festivalgrade;
        private String progresstype;
        private String festivaltype;
    }
}