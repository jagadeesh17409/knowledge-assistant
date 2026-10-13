package com.jagadeesh.knowledgeassistant;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class KnowledgeController {
    private final DocumentService documentService;
    private final AiService aiService;

    public KnowledgeController(DocumentService documentService, AiService aiService) {
        this.documentService = documentService;
        this.aiService = aiService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String result = documentService.readAndStore(file);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Could not read this file. Please try another PDF or TXT file."));
        }
    }

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody Map<String, String> body) {
        String question = body.getOrDefault("question", "").trim();
        if (question.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please enter a question."));
        }
        if (!documentService.hasDocument()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please upload a PDF or TXT file first."));
        }
        try {
            String answer = aiService.answer(question, documentService.getRelevantText(question));
            return ResponseEntity.ok(Map.of("answer", answer));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(503).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(502).body(Map.of("error", "The AI service could not answer right now. Check your API key, internet connection, and try again."));
        }
    }
}