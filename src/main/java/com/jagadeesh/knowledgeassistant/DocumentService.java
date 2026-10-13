package com.jagadeesh.knowledgeassistant;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private List<String> chunks = new ArrayList<>();

    public synchronized String readAndStore(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please choose a non-empty file.");
        }
        String name = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);
        if (!(name.endsWith(".pdf") || name.endsWith(".txt"))) {
            throw new IllegalArgumentException("Unsupported file type. Please upload a PDF or TXT file.");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File is too large. Please upload a file smaller than 5 MB.");
        }

        String text;
        if (name.endsWith(".txt")) {
            text = new String(file.getBytes(), StandardCharsets.UTF_8);
        } else {
            try (PDDocument pdf = Loader.loadPDF(file.getBytes())) {
                text = new PDFTextStripper().getText(pdf);
            } catch (Exception ex) {
                throw new IllegalArgumentException("This PDF could not be read. Try a valid, text-based PDF.");
            }
        }
        text = text.replaceAll("\\s+", " ").trim();
        if (text.length() < 20) {
            throw new IllegalArgumentException("Not enough readable text was found. Try a text-based PDF or TXT file.");
        }
        chunks = splitIntoChunks(text, 900);
        return "Document uploaded successfully. Found " + chunks.size() + " text section(s). You can now ask questions.";
    }

    private List<String> splitIntoChunks(String text, int size) {
        List<String> result = new ArrayList<>();
        for (int start = 0; start < text.length(); start += size) {
            int end = Math.min(start + size, text.length());
            result.add(text.substring(start, end));
        }
        return result;
    }

    public synchronized boolean hasDocument() { return !chunks.isEmpty(); }

    public synchronized String getRelevantText(String question) {
        Set<String> words = Arrays.stream(question.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
                .filter(w -> w.length() > 2).collect(Collectors.toSet());
        if (words.isEmpty()) return String.join("\n", chunks.stream().limit(3).toList());
        return chunks.stream()
                .map(chunk -> Map.entry(chunk, score(chunk, words)))
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .limit(3)
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining("\n\n"));
    }

    private int score(String chunk, Set<String> words) {
        String lower = chunk.toLowerCase(Locale.ROOT);
        int score = 0;
        for (String word : words) {
            int at = 0;
            while ((at = lower.indexOf(word, at)) >= 0) {
                score++;
                at += word.length();
            }
        }
        return score;
    }
}