package com.amanverma.hotelmanagementsystem.auth_service.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    @Autowired
    private ObjectMapper objectMapper;

    public String extractMessage(String raw) {
        try {

            // Find the start and end of the embedded JSON array
            int jsonStart = raw.indexOf("[{");
            int jsonEnd = raw.lastIndexOf("}]") + 2;

            if (jsonStart == -1 || jsonEnd == -1 || jsonEnd <= jsonStart) {
                return "Unknown error";
            }

            String json = raw.substring(jsonStart, jsonEnd);

            // Parse the JSON array
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootArray = mapper.readTree(json);

            // Extract "message" from the first object in the array
            return rootArray.path(0).path("message").asText("Unknown error");

        } catch (Exception e) {
            return "Unknown error";
        }
    }

}

