package com.agentperfilia.demo.service;

import com.agentperfilia.demo.model.entity.Profile;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    public String buildPrompt(Profile profile, String jobOffer) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Eres un experto en reclutamiento técnico y optimización de CVs para ATS (Applicant Tracking Systems). ")
                .append("Tu tarea es analizar un perfil profesional y una oferta de empleo, ")
                .append("luego generar un CV optimizado que maximice la compatibilidad con sistemas ATS.\n\n");

        prompt.append("=== PERFIL DEL CANDIDATO ===\n");
        prompt.append("Nombre: ").append(profile.getName()).append("\n");
        prompt.append("Resumen: ").append(profile.getSummary()).append("\n\n");

        prompt.append("Habilidades:\n");
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) {
            profile.getSkills().forEach(skill -> prompt.append("- ").append(skill).append("\n"));
        } else {
            prompt.append("- No especificadas\n");
        }

        prompt.append("\nProyectos:\n");
        if (profile.getProjects() != null && !profile.getProjects().isEmpty()) {
            profile.getProjects().forEach(project -> {
                prompt.append("- ").append(project.getName()).append(": ").append(project.getDescription()).append("\n");
                if (project.getTechnologies() != null && !project.getTechnologies().isEmpty()) {
                    prompt.append("  Tecnologías: ").append(String.join(", ", project.getTechnologies())).append("\n");
                }
                if (project.getHighlights() != null && !project.getHighlights().isEmpty()) {
                    prompt.append("  Destacados: ").append(String.join(", ", project.getHighlights())).append("\n");
                }
            });
        } else {
            prompt.append("- No especificados\n");
        }

        prompt.append("\nEducación:\n");
        if (profile.getEducation() != null && !profile.getEducation().isEmpty()) {
            profile.getEducation().forEach(edu ->
                    prompt.append("- ").append(edu.getTitle()).append(" - ").append(edu.getInstitution()).append("\n")
            );
        } else {
            prompt.append("- No especificada\n");
        }

        prompt.append("\n=== OFERTA DE EMPLEO ===\n").append(jobOffer).append("\n\n");

        prompt.append("=== INSTRUCCIONES ===\n")
                .append("1. Analiza los requisitos de la oferta y compárala con el perfil del candidato.\n")
                .append("2. Extrae las palabras clave más relevantes de la oferta (máximo 10).\n")
                .append("3. Genera un CV optimizado para ATS que destaque los puntos más compatibles con la oferta.\n\n")
                .append("Responde en el siguiente formato JSON:\n")
                .append("{\n")
                .append("  \"analysis\": \"Análisis detallado de compatibilidad entre perfil y oferta\",\n")
                .append("  \"keywords\": [\"palabra1\", \"palabra2\", ...],\n")
                .append("  \"optimizedCv\": \"CV optimizado en formato texto plano\"\n")
                .append("}\n");

        return prompt.toString();
    }
}

