package com.github.shsmysore.fcmjava.client.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shsmysore.fcmjava.client.exceptions.SerializationException;

public class JsonSerializer implements IJsonSerializer {

    private final ObjectMapper objectMapper;

    public JsonSerializer() {
        this(new ObjectMapper());
    }

    public JsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <TModel> String serialize(TModel model) {
        try {
            return objectMapper.writeValueAsString(model);
        } catch(Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <TModel> TModel deserialize(String content, Class<TModel> type) {
        try {
            return objectMapper.readValue(content, type);
        } catch(Exception e) {
            throw new SerializationException(e);
        }
    }
}
