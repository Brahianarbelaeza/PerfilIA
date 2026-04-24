package com.agentperfilia.demo.mapper;

import com.agentperfilia.demo.model.dto.ProfileRequest;
import com.agentperfilia.demo.model.dto.ProfileResponse;
import com.agentperfilia.demo.model.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile toEntity(ProfileRequest request) {
        return Profile.builder()
                .name(request.getName())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .linkedin(request.getLinkedin())
                .address(request.getAddress())
                .summary(request.getSummary())
                .skills(request.getSkills())
                .projects(request.getProjects())
                .education(request.getEducation())
                .build();
    }

    public ProfileResponse toResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .linkedin(profile.getLinkedin())
                .address(profile.getAddress())
                .summary(profile.getSummary())
                .skills(profile.getSkills())
                .projects(profile.getProjects())
                .education(profile.getEducation())
                .build();
    }
}

