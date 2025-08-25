package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DetailRepeatResponseDTO {
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
        private List<RepeatItem> item;
    }

    @Getter
    @Setter
    public static class RepeatItem {
        private String contentid;
        private String contenttypeid;
        private String serialnum;
        private String infoname;
        private String infotext;
    }
}