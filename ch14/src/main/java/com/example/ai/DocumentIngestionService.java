package com.example.ai;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    @Value("classpath:documents/product-faq.txt")
    private Resource faqResource;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void ingest() {
        // Stage 1 — Read: load the plain-text FAQ as a list of Documents
        TextReader reader = new TextReader(faqResource);
        reader.getCustomMetadata().put("source", "product-faq.txt");
        List<Document> documents = reader.read();

        // Stage 2 — Transform: split into 800-token chunks so each chunk
        // fits comfortably inside the embedding model's input window
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(100)
                .build();
        List<Document> chunks = splitter.apply(documents);

        // Stage 3 — Load: embed each chunk and store in the vector store.
        // Spring AI calls the configured EmbeddingModel (OpenAI by default)
        // and writes the resulting vectors to pgvector or Chroma.
        vectorStore.accept(chunks);

        System.out.println(">>> Ingested " + chunks.size() + " document chunks into the vector store.");
    }
}