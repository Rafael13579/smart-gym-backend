package com.academiaSpringBoot.demo.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SpringAiProvider implements AiProvider {

    private final ChatClient chatClient;

    @Override
    public String generate(String prompt) {
        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}
