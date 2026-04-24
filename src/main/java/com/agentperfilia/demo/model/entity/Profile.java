package com.agentperfilia.demo.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Profile {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String summary;
    private List<String> skills;
    private List<Project> projects;
    private List<Education> education;
    private String fullName;
    private String email;
    private String phone;
    private String linkedin;
    private String address;
}
