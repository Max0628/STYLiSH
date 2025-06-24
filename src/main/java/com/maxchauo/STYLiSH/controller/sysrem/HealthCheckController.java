package com.maxchauo.STYLiSH.controller.sysrem;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Log4j2
@RequestMapping("/api")
@RestController
public class HealthCheckController {

    @GetMapping("/v1/healthCheck")
    public ResponseEntity<String> healthCheck() {
        try {
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Health check failed", e);
            return ResponseEntity.status(503).body("health check failed");
        }
    }
}
