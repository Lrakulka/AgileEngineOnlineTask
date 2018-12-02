package com.agileengine.crawler.reader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FileReaderImpl implements FileReader {
    private static Logger LOGGER = LoggerFactory.getLogger(FileReaderImpl.class);

    private static String CHARSET_NAME = "utf8";

    @Override
    public Optional<Document> getFileDocument(String path) {
        final File htmlFile = new File(path.trim());
        try {
            return Optional.of(Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath()));
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }
}
