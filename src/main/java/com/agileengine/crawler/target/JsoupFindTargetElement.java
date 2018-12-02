package com.agileengine.crawler.target;

import com.agileengine.crawler.reader.ReaderController;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
        final Node sourceElementChild = originElement.childNodes().size() > 0
                ? originElement.childNodes().get(0)
                : null;
        final List<Attribute> originElementAttrs = originElement.attributes().asList();
        final Optional<Element> targetElement = elements.stream()
                .max(Comparator.comparingInt(element -> countSameAtr(element, originElementAttrs, sourceElementChild)));

        return !targetElement.isPresent()
                || threshold > countSameAtr(targetElement.get(), originElementAttrs, sourceElementChild)  // TODO: Improve twice call
                ? Optional.empty()
                : targetElement;
    }

    private static int countSameAtr(final Element targetElement, final List<Attribute> elementAttrs,
            final Node sourceElementChild) {
        int prevSize = elementAttrs.size();
        List<Attribute> originAttrs = new ArrayList<>(elementAttrs);
        List<Attribute> targetAttrs = targetElement.attributes().asList();
        if (targetAttrs.contains(new Attribute("onclick", "javascript:window.close(); return false;"))) {
            return 0;
        }
        if (targetElement.childNodes().size() > 0 && targetElement.childNodes().get(0).hasSameValue(sourceElementChild)) {
            prevSize += USER_VIZUAL_ATTRIBUTE_POINT;
        }
        originAttrs.removeAll(targetAttrs);
        return prevSize - originAttrs.size();
    }
}
