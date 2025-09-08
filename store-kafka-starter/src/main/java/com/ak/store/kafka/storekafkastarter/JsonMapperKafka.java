package com.ak.store.kafka.storekafkastarter;

import com.ak.store.kafka.storekafkastarter.util.LocalDateTimeAdapter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class JsonMapperKafka {
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    //todo <T extends KafkaEvent>
    public <T> T fromJson(String data, Class<T> clazz) {
        return gson.fromJson(data, clazz);
    }

    public <T> String toJson(T data) {
        return gson.toJson(data);
    }
}
