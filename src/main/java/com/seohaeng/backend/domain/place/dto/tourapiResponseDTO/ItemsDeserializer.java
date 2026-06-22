package com.seohaeng.backend.domain.place.dto.tourapiResponseDTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ItemsDeserializer extends JsonDeserializer<AreaBasedSearchResponseDTO.Items> {

    @Override
    public AreaBasedSearchResponseDTO.Items deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return null;
        }
        return ctxt.readValue(p, AreaBasedSearchResponseDTO.Items.class);
    }
}