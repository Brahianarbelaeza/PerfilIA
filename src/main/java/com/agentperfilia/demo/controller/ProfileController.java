package com.agentperfilia.demo.controller;

import com.agentperfilia.demo.mapper.ProfileMapper;
import com.agentperfilia.demo.model.dto.ProfileRequest;
import com.agentperfilia.demo.model.dto.ProfileResponse;
import com.agentperfilia.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
@CrossOrigin
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @PostMapping
    public ProfileResponse createProfile(@RequestBody ProfileRequest profileRequest) {
        var profile = profileMapper.toEntity(profileRequest);
        var savedProfile = profileService.save(profile);
        return profileMapper.toResponse(savedProfile);
    }

    @GetMapping
    public List<ProfileResponse> getAllProfiles() {
        return profileService.findAll()
                .stream()
                .map(profileMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProfileResponse getProfileById(@PathVariable String id) {
        var profile = profileService.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return profileMapper.toResponse(profile);
    }
}
