package com.agileengine.crawler.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class FileReaderWebPageImpl implements FileReader {
    private static Logger LOGGER = LoggerFactory.getLogger(FileReaderWebPageImpl.class);

    @Override
    public Optional<Document> getFileDocument(String path) {
        try {
            return Optional.of(Jsoup.connect(path.trim()).get());
        } catch (IOException e) {
            LOGGER.error("Error reading web page [{}]", path, e);
            return Optional.empty();
        }
    }
}
