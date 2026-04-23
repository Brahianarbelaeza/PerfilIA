package com.agentperfilia.demo.controller;

import com.agentperfilia.demo.model.dto.CvRequest;
import com.agentperfilia.demo.model.dto.CvResponse;
import com.agentperfilia.demo.service.CvOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/generate-cv")
@CrossOrigin
@RequiredArgsConstructor
public class CvController {
    private final CvOrchestratorService cvOrchestratorService;

    @PostMapping
    public CvResponse generateCv(@RequestBody CvRequest request) {
        return cvOrchestratorService.generateCv(request);
    }
}
