package com.agileengine.crawler.origin;

import com.agileengine.crawler.reader.ReaderController;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class JsoupFindOriginElement {
    private static Logger LOGGER = LoggerFactory.getLogger(JsoupFindOriginElement.class);
    private static ReaderController readerController;

    static {
        readerController = new ReaderController();
    }

    public static Optional<Element> getOriginElement(final String resourcePath, final String targetElementId) {
        final Optional<Document> document = readerController.getDocument(resourcePath);
        final Optional<Element> element = document.map(doc -> doc.getElementById(targetElementId));
        if (!element.isPresent()) {
            LOGGER.warn("Target element not found");
        } else {
            LOGGER.info("Target element : [{}]", element.get());
        }
        return element;
    }

}
