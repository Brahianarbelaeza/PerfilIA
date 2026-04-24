package com.agentperfilia.demo.service;

import com.agentperfilia.demo.model.entity.Profile;
import org.springframework.stereotype.Service;

@Service
public class PromptBuilderService {

    public String buildPrompt(Profile profile, String jobOffer) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Analiza el siguiente perfil profesional y una oferta laboral. ")
                .append("Tu objetivo es generar un CV optimizado para sistemas ATS (Applicant Tracking Systems), ")
                .append("maximizando la coincidencia entre el perfil y los requisitos del puesto.\n\n");

        prompt.append("=== PERFIL DEL CANDIDATO ===\n");
        prompt.append("Nombre: ").append(nullSafe(profile.getName())).append("\n");
        prompt.append("Resumen: ").append(nullSafe(profile.getSummary())).append("\n\n");

        prompt.append("HABILIDADES:\n");
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) {
            profile.getSkills().forEach(skill ->
                    prompt.append("- ").append(skill).append("\n")
            );
        } else {
            prompt.append("- No especificadas\n");
        }

        prompt.append("\nPROYECTOS:\n");
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

        prompt.append("\nEDUCACIÓN:\n");
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

        prompt.append("\n=== OFERTA LABORAL ===\n");
        prompt.append(jobOffer).append("\n\n");

        prompt.append("=== INSTRUCCIONES ===\n");
        prompt.append("1. Analiza la oferta laboral y detecta:\n")
                .append("   - tecnologías clave\n")
                .append("   - habilidades requeridas\n")
                .append("   - tipo de rol\n\n")

                .append("2. Compara el perfil con la oferta e identifica:\n")
                .append("   - coincidencias\n")
                .append("   - fortalezas\n")
                .append("   - posibles brechas\n\n")

                .append("3. Extrae máximo 10 palabras clave relevantes para ATS.\n\n")

                .append("4. Genera un CV optimizado que:\n")
                .append("   - priorice habilidades relevantes\n")
                .append("   - use palabras clave de la oferta\n")
                .append("   - describa proyectos con impacto técnico\n")
                .append("   - use lenguaje profesional y técnico\n")
                .append("   - NO invente experiencia\n\n")

                .append("=== FORMATO DE RESPUESTA ===\n")
                .append("Responde SOLO en JSON válido. No incluyas texto adicional.\n\n")

                .append("{\n")
                .append("  \"analysis\": \"Análisis claro de compatibilidad entre perfil y oferta\",\n")
                .append("  \"keywords\": [\"palabra1\", \"palabra2\"],\n")
                .append("  \"optimizedCv\": \"CV en texto plano estructurado (Resumen, Habilidades, Proyectos, Educación)\"\n")
                .append("}\n");

        return prompt.toString();
    }

    private String nullSafe(String value) {
        return value != null ? value : "No especificado";
    }
}