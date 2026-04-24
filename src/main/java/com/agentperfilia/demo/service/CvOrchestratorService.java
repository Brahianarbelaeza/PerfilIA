package com.agentperfilia.demo.service;

import com.agentperfilia.demo.exception.ResourceNotFoundException;
import com.agentperfilia.demo.integration.OpenAIClient;
import com.agentperfilia.demo.model.dto.CvRequest;
import com.agentperfilia.demo.model.dto.CvResponse;
import com.agentperfilia.demo.model.dto.CvStructured;
import com.agentperfilia.demo.model.dto.PersonalInfo;
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

            List<String> keywords = new ArrayList<>();
            JsonNode keywordsNode = root.path("keywords");
            if (keywordsNode.isArray()) {
                keywordsNode.forEach(node -> keywords.add(node.asText()));
            }

            JsonNode cvNode = root.path("optimizedCv");

            CvStructured cvStructured = objectMapper.treeToValue(
                    cvNode,
                    CvStructured.class
            );

            return CvResponse.builder()
                    .analysis(analysis)
                    .keywords(keywords)
                    .optimizedCv(cvStructured)
                    .build();

        } catch (Exception e) {

            CvStructured fallback = CvStructured.builder()
                    .personalInfo(PersonalInfo.builder().build())
                    .summary(response)
                    .skills(new ArrayList<>())
                    .projects(new ArrayList<>())
                    .education(new ArrayList<>())
                    .build();

            return CvResponse.builder()
                    .analysis("No se pudo parsear correctamente la respuesta de la IA")
                    .keywords(new ArrayList<>())
                    .optimizedCv(fallback)
                    .build();
        }
    }

    private CvStructured parseCvStructured(JsonNode cvNode) {
        PersonalInfo personalInfo = PersonalInfo.builder()
                .fullName(cvNode.path("personalInfo").path("fullName").asText(""))
                .email(cvNode.path("personalInfo").path("email").asText(""))
                .phone(cvNode.path("personalInfo").path("phone").asText(""))
                .linkedin(cvNode.path("personalInfo").path("linkedin").asText(""))
                .location(cvNode.path("personalInfo").path("location").asText(""))
                .build();

        String summary = cvNode.path("summary").asText("");

        List<String> skills = new ArrayList<>();
        JsonNode skillsNode = cvNode.path("skills");
        if (skillsNode.isArray()) {
            skillsNode.forEach(node -> skills.add(node.asText()));
        }

        return CvStructured.builder()
                .personalInfo(personalInfo)
                .summary(summary)
                .skills(skills)
                .projects(new ArrayList<>())
                .education(new ArrayList<>())
                .build();
    }
}