package com.agileengine.crawler.reader;

import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ReaderController {
    private static final int HTML_READER = 1;
    private static final int FILE_READER = 0;
    private Map<Integer, FileReader> readers;

    {
        readers = new HashMap<>();
        readers.put(0, new FileReaderImpl());
        readers.put(1, new FileReaderWebPageImpl());
    }

    public Optional<Document> getDocument(final String path) {
        return readers.get(isURL(path) ? HTML_READER : FILE_READER).getFileDocument(path);
    }


    private boolean isURL(final String path) {
        try {
            new URL(path);
            return Boolean.TRUE;
        } catch (MalformedURLException e) {
            // I could use UrlValidator but it will required library commons-validator
        }
        return Boolean.FALSE;
    }
}
