package com.agileengine.crawler.reader;

import org.jsoup.nodes.Document;

import java.util.Optional;

public interface FileReader {
    Optional<Document> getFileDocument(String path);
}
