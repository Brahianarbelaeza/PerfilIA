package com.agentperfilia.demo.controller;

import com.agentperfilia.demo.model.dto.CvRequest;
import com.agentperfilia.demo.model.dto.CvResponse;
import com.agentperfilia.demo.service.CvOrchestratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/generate-cv")
@CrossOrigin
@RequiredArgsConstructor
public class CvController {
    private final CvOrchestratorService cvOrchestratorService;

    @PostMapping
    public ResponseEntity<CvResponse> generateCv(@Valid @RequestBody CvRequest request) {
        log.info("Solicitud de generación de CV recibida - ProfileID: {}", request.getProfileId());
        
        var response = cvOrchestratorService.generateCv(request);
        
        log.info("CV generado exitosamente para ProfileID: {}", request.getProfileId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
