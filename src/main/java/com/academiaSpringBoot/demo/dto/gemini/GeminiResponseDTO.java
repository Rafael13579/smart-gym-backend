package com.academiaSpringBoot.demo.dto.gemini;
import java.util.List;

public record GeminiResponseDTO(List<Candidate> candidates) {
    public record Candidate(Content content) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    public String getText() {
        if (candidates == null || candidates.isEmpty()) return "";
        var parts = candidates.getFirst().content().parts();
        if (parts == null || parts.isEmpty()) return "";
        return parts.getFirst().text();
    }
}