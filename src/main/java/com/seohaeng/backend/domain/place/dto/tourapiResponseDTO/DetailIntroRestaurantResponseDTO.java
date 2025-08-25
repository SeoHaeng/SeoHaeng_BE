package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DetailIntroRestaurantResponseDTO implements DetailIntroResponse {
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
        private List<RestaurantIntroItem> item;
    }

    @Getter
    @Setter
    public static class RestaurantIntroItem {
        private String contentid;
        private String contenttypeid;
        private String seat;
        private String kidsfacility;
        private String firstmenu;
        private String treatmenu;
        private String smoking;
        private String packing;
        private String infocenterfood;
        private String scalefood;
        private String parkingfood;
        private String opendatefood;
        private String opentimefood;
        private String restdatefood;
        private String discountinfofood;
        private String chkcreditcardfood;
        private String reservationfood;
        private String lcnsno;
    }
}