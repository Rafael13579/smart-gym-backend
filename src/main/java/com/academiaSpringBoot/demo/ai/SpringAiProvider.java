package com.academiaSpringBoot.demo.ai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class SpringAiProvider implements AiProvider {

    private final ChatModel chatModel;

    public SpringAiProvider(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generate(String prompt) {
        return chatModel
                .call(new Prompt(prompt))
                .getResult()
                .getOutput()
                .getContent();
    }
}
