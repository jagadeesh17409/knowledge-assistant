
package com.jagadeesh.knowledgeassistant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${groq.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String answer(String question, String relevantText) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                "Groq API key is missing. Set GROQ_API_KEY and restart."
            );
        }

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException(
                "Please enter a question."
            );
        }

        if (relevantText == null || relevantText.isBlank()) {
            throw new IllegalArgumentException(
                "Please upload a document first."
            );
        }

        String prompt =
            "You are a document knowledge assistant. Answer the question "
            + "using only the document text provided below. "
            + "If the answer is not present, say that it was not found "
            + "in the document. Do not invent information.\n\n"
            + "DOCUMENT TEXT:\n"
            + relevantText
            + "\n\nQUESTION:\n"
            + question;

        Map<String, Object> body = Map.of(
            "model", "openai/gpt-oss-20b",
            "messages", List.of(
                Map.of(
                    "role", "user",
                    "content", prompt
                )
            ),
            "temperature", 0.2,
            "max_completion_tokens", 500
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(body, headers);

        String url =
            "https://api.groq.com/openai/v1/chat/completions";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            );

            Map<?, ?> result = response.getBody();

            if (result == null) {
                throw new IllegalStateException(
                    "Groq returned an empty response."
                );
            }

            List<?> choices = (List<?>) result.get("choices");

            if (choices == null || choices.isEmpty()) {
                throw new IllegalStateException(
                    "Groq returned no answer."
                );
            }

            Map<?, ?> choice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice.get("message");

            Object content = message.get("content");

            if (content == null || content.toString().isBlank()) {
                throw new IllegalStateException(
                    "Groq returned an empty answer."
                );
            }

            return content.toString();

        } catch (HttpStatusCodeException e) {
            throw new IllegalStateException(
                "Groq API error (HTTP "
                + e.getStatusCode().value()
                + "): "
                + e.getResponseBodyAsString()
            );
        }
    }
}
