package com.agentperfilia.demo.model.dto;

import com.agentperfilia.demo.model.entity.Education;
import com.agentperfilia.demo.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String name;
    private String fullName;
    private String email;
    private String phone;
    private String linkedin;
    private String address;
    private String summary;
    private List<String> skills;
    private List<Project> projects;
    private List<Education> education;
}

