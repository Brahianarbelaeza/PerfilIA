package com.agentperfilia.demo.integration;

import com.agentperfilia.demo.exception.OpenAIException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String callOpenAI(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = buildRequestBody(prompt);

            HttpEntity<String> request = new HttpEntity<>(
                    objectMapper.writeValueAsString(requestBody),
                    headers
            );

            String response = restTemplate.postForObject(apiUrl, request, String.class);

            return parseResponse(response);

        } catch (OpenAIException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenAIException("Error al comunicarse con la API de OpenAI: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> body = new HashMap<>();

        body.put("model", "gpt-4.1-mini");
        body.put("temperature", 0.3);

        List<Map<String, Object>> input = new ArrayList<>();

        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Eres un experto en reclutamiento técnico y optimización de CVs para sistemas ATS. Responde siempre en JSON válido, sin texto adicional.");
        input.add(systemMessage);

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        input.add(userMessage);

        body.put("input", input);

        return body;
    }

    private String parseResponse(String response) throws Exception {
        try {
            JsonNode root = objectMapper.readTree(response);

            JsonNode output = root.path("output");

            if (!output.isArray() || output.size() == 0) {
                throw new OpenAIException("Respuesta vacía recibida de OpenAI");
            }

            JsonNode contentArray = output.get(0).path("content");

            if (!contentArray.isArray() || contentArray.size() == 0) {
                throw new OpenAIException("Contenido vacío en la respuesta de OpenAI");
            }

            JsonNode textNode = contentArray.get(0).path("text");

            if (textNode.isMissingNode()) {
                throw new OpenAIException("No se encontró texto en la respuesta de OpenAI");
            }

            return textNode.asText();
        } catch (OpenAIException e) {
            throw e;
        } catch (Exception e) {
            throw new OpenAIException("Error al parsear la respuesta de OpenAI: " + e.getMessage(), e);
        }
    }
}

