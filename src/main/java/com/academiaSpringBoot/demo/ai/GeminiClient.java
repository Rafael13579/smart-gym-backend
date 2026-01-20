package com.academiaSpringBoot.demo.ai;

import com.academiaSpringBoot.demo.dto.gemini.GeminiRequestDTO;
import com.academiaSpringBoot.demo.dto.gemini.GeminiResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gemini-client", url = "${gemini.api.url}")
public interface GeminiClient {

    @PostMapping("/v1beta/models/gemini-2.5-flash:generateContent")
    GeminiResponseDTO generate(@RequestParam("key") String apiKey, @RequestBody GeminiRequestDTO request);
}
