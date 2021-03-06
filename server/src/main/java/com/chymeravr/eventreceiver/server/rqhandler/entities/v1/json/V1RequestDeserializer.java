package com.chymeravr.eventreceiver.server.rqhandler.entities.v1.json;

import com.chymeravr.eventreceiver.server.rqhandler.iface.RequestDeserializer;
import com.chymeravr.schemas.eventreceiver.EventPing;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by rubbal on 19/1/17.
 */
public class V1RequestDeserializer implements RequestDeserializer {

    @Override
    public EventPing deserializeRequest(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(data, EventPing.class);
    }
}
