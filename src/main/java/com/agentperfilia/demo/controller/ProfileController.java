package com.agentperfilia.demo.controller;

import com.agentperfilia.demo.model.entity.Profile;
import com.agentperfilia.demo.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/profiles")
@CrossOrigin
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.save(profile);
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable String id) {
        return profileService.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}
