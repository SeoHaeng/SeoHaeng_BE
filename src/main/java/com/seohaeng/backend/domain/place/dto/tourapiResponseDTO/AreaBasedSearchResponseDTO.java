package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class AreaBasedSearchResponseDTO {
    private Response response;

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
        private List<PlaceItem> item;
    }

    @Getter
    @Setter
    public static class PlaceItem {
        private String addr1;
        private String addr2;
        private String areacode;
        private String cat1;
        private String cat2;
        private String cat3;
        private String contentid;
        private String contenttypeid;
        private String createdtime;
        private String firstimage;
        private String firstimage2;
        private String cpyrhtDivCd;
        private String mapx;
        private String mapy;
        private String mlevel;
        private String modifiedtime;
        private String sigungucode;
        private String tel;
        private String title;
        private String zipcode;
        private String lDongRegnCd;
        private String lDongSignguCd;
        private String lclsSystm1;
        private String lclsSystm2;
        private String lclsSystm3;
    }
}