package com.agileengine.crawler.target;

import com.agileengine.crawler.reader.ReaderController;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JsoupFindTargetElement {
    public static final int USER_VIZUAL_ATTRIBUTE_POINT = 2;
    private static Logger LOGGER = LoggerFactory.getLogger(JsoupFindTargetElement.class);
    private static ReaderController readerController;

    static {
        readerController = new ReaderController();
    }

    public static Optional<Element> getTargetElement(final String inputTargetFile,
            final Element originElement, final Integer threshold) {
        final Optional<Document> document = readerController.getDocument(inputTargetFile);
        final Optional<Elements> elements = document.map(doc -> doc.getElementsByTag(originElement.tagName()));
        if (!elements.isPresent()) {
            LOGGER.warn("Found zero elements");
            return Optional.empty();
        } else {
            LOGGER.info("Found [{}] elements", elements.get().size());
        }

        return getMostSimilar(originElement, elements.get(), threshold);
    }

    private static Optional<Element> getMostSimilar(final Element originElement, final Elements elements,
            final Integer threshold) {
        final Element sourceElementChild = originElement.children().size() > 0 ? originElement.child(0) : null;
        final List<Attribute> originElementAttrs = originElement.attributes().asList();
        final Optional<Element> targetElement = elements.stream()
                .max(Comparator.comparingInt(element -> countSameAtr(element, originElementAttrs, sourceElementChild)));

        return !targetElement.isPresent()
                || threshold > countSameAtr(targetElement.get(), originElementAttrs, sourceElementChild)  // TODO: Improve twice call
                ? Optional.empty()
                : targetElement;
    }

    private static int countSameAtr(final Element targetElement, final List<Attribute> elementAttrs,
            final Element sourceElementChild) {
        int prevSize = elementAttrs.size();
        List<Attribute> originAttrs = new ArrayList<>(elementAttrs);
        List<Attribute> targetAttrs = targetElement.attributes().asList();
        if (targetAttrs.contains(new Attribute("onclick", "javascript:window.close(); return false;"))) {
            return 0;
        }
        if (targetElement.children().size() > 0 && targetElement.child(0).equals(sourceElementChild)) {
            prevSize += USER_VIZUAL_ATTRIBUTE_POINT;
        }
        originAttrs.removeAll(targetAttrs);
        return prevSize - originAttrs.size();
    }
}
