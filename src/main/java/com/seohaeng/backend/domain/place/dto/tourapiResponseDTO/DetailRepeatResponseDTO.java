package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
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
        @JsonDeserialize(using = ItemsDeserializer.class)
        private Items items;
        private Integer numOfRows;
        private Integer pageNo;
        private Integer totalCount;
    }

    public static class ItemsDeserializer extends JsonDeserializer<Items> {
        @Override
        public Items deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_STRING) {
                return null;
            }
            return ctxt.readValue(p, Items.class);
        }
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