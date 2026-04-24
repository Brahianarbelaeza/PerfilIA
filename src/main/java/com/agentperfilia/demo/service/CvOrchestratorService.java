package com.agentperfilia.demo.service;

import com.agentperfilia.demo.exception.ResourceNotFoundException;
import com.agentperfilia.demo.integration.OpenAIClient;
import com.agentperfilia.demo.model.dto.CvRequest;
import com.agentperfilia.demo.model.dto.CvResponse;
import com.agentperfilia.demo.model.entity.Profile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CvOrchestratorService {

    private final ProfileService profileService;
    private final PromptBuilderService promptBuilderService;
    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    public CvResponse generateCv(CvRequest request) {

        Profile profile = profileService.findById(request.getProfileId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado con ID: " + request.getProfileId()));

        String prompt = promptBuilderService.buildPrompt(profile, request.getJobOffer());
        String response = openAIClient.callOpenAI(prompt);

        String cleanResponse = extractJson(response);
        return safeParseResponse(cleanResponse);
    }
    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");

        if (start != -1 && end != -1) {
            return response.substring(start, end + 1);
        }
        return response;
    }

    private CvResponse safeParseResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);

            String analysis = root.path("analysis").asText("");
            String optimizedCv = root.path("optimizedCv").asText("");

            List<String> keywords = new ArrayList<>();
            JsonNode keywordsNode = root.path("keywords");

            if (keywordsNode.isArray()) {
                keywordsNode.forEach(node -> keywords.add(node.asText()));
            }

            return CvResponse.builder()
                    .analysis(analysis)
                    .keywords(keywords)
                    .optimizedCv(optimizedCv)
                    .build();

        } catch (Exception e) {

            return CvResponse.builder()
                    .analysis("No se pudo parsear correctamente la respuesta de la IA")
                    .keywords(new ArrayList<>())
                    .optimizedCv(response) // devolvemos el texto crudo
                    .build();
        }
    }
}