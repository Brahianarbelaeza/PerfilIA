package com.agentperfilia.demo.model.dto;

import com.agentperfilia.demo.model.entity.Project;
import com.agentperfilia.demo.model.entity.Education;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvStructured {
    private PersonalInfo personalInfo;
    private String summary;
    private List<String> skills;
    private List<Project> projects;
    private List<Education> education;
}

