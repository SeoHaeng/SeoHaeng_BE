package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DetailIntroTouristResponseDTO implements DetailIntroResponse {
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
        private List<TouristIntroItem> item;
    }

    @Getter
    @Setter
    public static class TouristIntroItem {
        private String contentid;
        private String contenttypeid;
        private String heritage1;
        private String heritage2;
        private String heritage3;
        private String infocenter;
        private String opendate;
        private String restdate;
        private String expguide;
        private String expagerange;
        private String accomcount;
        private String useseason;
        private String usetime;
        private String parking;
        private String chkbabycarriage;
        private String chkpet;
        private String chkcreditcard;
    }
}