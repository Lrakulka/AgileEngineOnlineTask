package com.agileengine;

import com.agileengine.crawler.origin.JsoupFindOriginElement;
import com.agileengine.crawler.target.JsoupFindTargetElement;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FindElementOnPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindElementOnPage.class);

    private static final String DEFAULT_ORIGIN_ELEMENT_ID = "make-everything-ok-button";
    private static final Integer DEFAULT_THRESHOLD = 1;
    private static final int ERROR_CODE = 1;

    public static void main(String[] args) {
        String inputOriginFile = args[0];
        String inputTargetFile = args[1];
        String originElementId = DEFAULT_ORIGIN_ELEMENT_ID;
        int threshold = DEFAULT_THRESHOLD;
        if (args.length > 2) {
            originElementId = args[2];
        }
        if (args.length > 3) {
            threshold = Integer.valueOf(args[3]);
        }

        final Optional<Element> originElement = JsoupFindOriginElement
                .getOriginElement(inputOriginFile, originElementId);
        validateElement(originElement, "Origin element not found");

        Optional<Element> targetElement = JsoupFindTargetElement
                .getTargetElement(inputTargetFile, originElement.get(), threshold);
        validateElement(targetElement, "Target element not found");

        System.out.println(getElementPath(targetElement.get()));
    }

    private static String getElementPath(Element element) {
        final StringBuilder builder = new StringBuilder();
        while (element.hasParent()) {
            builder.insert(0, String.format("%s[%s] > ", element.tagName(), element.id()));
            element = element.parent();
        }
        builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }

    private static void validateElement(final Optional<Element> checkElement, final String msg) {
        if (!checkElement.isPresent()) {
            System.out.println(msg);
            System.exit(ERROR_CODE);
        }
    }
}
