package com.academiaSpringBoot.demo.client;

import com.academiaSpringBoot.demo.dto.gemini.GeminiRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.GeminiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gemini-client", url = "${gemini.url}")
public interface GeminiClient {

    @PostMapping("/v1beta/models/gemini-1.5-flash-latest:generateContent")
    GeminiResponseDTO generateContent(
            @RequestParam("key") String apiKey,
            @RequestBody GeminiRequestDTO request
    );
}