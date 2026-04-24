package com.agentperfilia.demo.service;

import com.agentperfilia.demo.model.entity.Profile;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class PromptBuilderService {

    public String buildPrompt(Profile profile, String jobOffer) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Eres un experto en reclutamiento técnico y optimización de CVs para sistemas ATS.\n\n");

        prompt.append("Tu tarea es analizar un perfil profesional y una oferta laboral, y generar un CV optimizado que maximice la compatibilidad con la oferta.\n\n");

        // ========================
        // INFORMACIÓN PERSONAL
        // ========================
        prompt.append("=== INFORMACIÓN PERSONAL ===\n");
        prompt.append("Nombre Completo: ").append(nullSafe(profile.getFullName())).append("\n");
        prompt.append("Email: ").append(nullSafe(profile.getEmail())).append("\n");
        prompt.append("Teléfono: ").append(nullSafe(profile.getPhone())).append("\n");
        prompt.append("LinkedIn: ").append(nullSafe(profile.getLinkedin())).append("\n");
        prompt.append("Ubicación: ").append(nullSafe(profile.getAddress())).append("\n\n");

        // ========================
        // RESUMEN
        // ========================
        prompt.append("=== RESUMEN PROFESIONAL ===\n");
        prompt.append(nullSafe(profile.getSummary())).append("\n\n");

        // ========================
        // HABILIDADES
        // ========================
        prompt.append("=== HABILIDADES ===\n");
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) {
            profile.getSkills().forEach(skill ->
                    prompt.append("- ").append(skill).append("\n")
            );
        } else {
            prompt.append("- No especificadas\n");
        }

        // ========================
        // PROYECTOS
        // ========================
        prompt.append("\n=== PROYECTOS ===\n");
        if (profile.getProjects() != null && !profile.getProjects().isEmpty()) {
            profile.getProjects().forEach(project -> {
                prompt.append("- ").append(nullSafe(project.getName()))
                        .append(": ").append(nullSafe(project.getDescription())).append("\n");

                if (project.getTechnologies() != null && !project.getTechnologies().isEmpty()) {
                    prompt.append("  Tecnologías: ")
                            .append(String.join(", ", project.getTechnologies()))
                            .append("\n");
                }

                if (project.getHighlights() != null && !project.getHighlights().isEmpty()) {
                    prompt.append("  Logros: ")
                            .append(String.join(", ", project.getHighlights()))
                            .append("\n");
                }
            });
        } else {
            prompt.append("- No especificados\n");
        }

        // ========================
        // EDUCACIÓN
        // ========================
        prompt.append("\n=== EDUCACIÓN ===\n");
        if (profile.getEducation() != null && !profile.getEducation().isEmpty()) {
            profile.getEducation().forEach(edu ->
                    prompt.append("- ")
                            .append(nullSafe(edu.getTitle()))
                            .append(" - ")
                            .append(nullSafe(edu.getInstitution()))
                            .append("\n")
            );
        } else {
            prompt.append("- No especificada\n");
        }

        // ========================
        // OFERTA LABORAL
        // ========================
        prompt.append("\n=== OFERTA LABORAL ===\n");
        prompt.append(jobOffer).append("\n\n");

        prompt.append("\n=== PERFIL EN FORMATO JSON ===\n");
        prompt.append(convertProfileToJson(profile)).append("\n\n");
        // ========================
        // INSTRUCCIONES
        // ========================
        prompt.append("=== INSTRUCCIONES ===\n");

        prompt.append("1. Analiza la oferta laboral e identifica:\n")
                .append("- tecnologías clave\n")
                .append("- habilidades requeridas\n")
                .append("- tipo de rol\n\n");

        prompt.append("2. Compara el perfil con la oferta e identifica:\n")
                .append("- coincidencias\n")
                .append("- fortalezas\n")
                .append("- posibles brechas\n\n");

        prompt.append("3. Extrae máximo 10 palabras clave técnicas relevantes para ATS. Evita términos genéricos.\n\n");

        prompt.append("4. Genera un CV optimizado que:\n")
                .append("- priorice habilidades relevantes\n")
                .append("- use palabras clave de la oferta\n")
                .append("- describa proyectos con impacto técnico\n")
                .append("- use lenguaje profesional y técnico\n")
                .append("- NO invente experiencia\n\n");

        prompt.append("5. REGLAS OBLIGATORIAS:\n")
                .append("- No dejes campos vacíos\n")
                .append("- Si hay información en el perfil, debes usarla\n")
                .append("- Projects y education deben completarse siempre\n")
                .append("- No dupliques habilidades o tecnologías\n\n");

        prompt.append("6. OBLIGATORIO:\n")
                .append("- Convierte los proyectos del perfil en la sección 'projects'\n")
                .append("- Convierte la educación del perfil en la sección 'education'\n")
                .append("- NO puedes dejar estas secciones vacías bajo ninguna circunstancia\n")
                .append("- Si existe al menos un proyecto en el perfil, debes incluirlo\n")
                .append("- Si existe educación en el perfil, debes incluirla\n\n");

        // ========================
        // FORMATO DE RESPUESTA
        // ========================
        prompt.append("=== FORMATO DE RESPUESTA ===\n");
        prompt.append("Responde SOLO en JSON válido. No incluyas texto adicional.\n");
        prompt.append("optimizedCv DEBE ser un objeto JSON, no texto.\n\n");

        prompt.append("{\n")
                .append("  \"analysis\": \"Análisis claro de compatibilidad entre perfil y oferta\",\n")
                .append("  \"keywords\": [\"palabra1\", \"palabra2\"],\n")
                .append("  \"optimizedCv\": {\n")
                .append("    \"personalInfo\": {\n")
                .append("      \"fullName\": \"\",\n")
                .append("      \"email\": \"\",\n")
                .append("      \"phone\": \"\",\n")
                .append("      \"linkedin\": \"\",\n")
                .append("      \"location\": \"\"\n")
                .append("    },\n")
                .append("    \"summary\": \"\",\n")
                .append("    \"skills\": [],\n")
                .append("    \"projects\": [\n")
                .append("      {\n")
                .append("        \"name\": \"\",\n")
                .append("        \"description\": \"\",\n")
                .append("        \"technologies\": [],\n")
                .append("        \"highlights\": []\n")
                .append("      }\n")
                .append("    ],\n")
                .append("    \"education\": [\n")
                .append("      {\n")
                .append("        \"title\": \"\",\n")
                .append("        \"institution\": \"\"\n")
                .append("      }\n")
                .append("    ]\n")
                .append("  }\n")
                .append("}\n");

        return prompt.toString();
    }
    private String nullSafe(String value) {
        return value != null ? value : "No especificado";
    }
    private String convertProfileToJson(Profile profile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(profile);
        } catch (Exception e) {
            return "{}";
        }
    }

}