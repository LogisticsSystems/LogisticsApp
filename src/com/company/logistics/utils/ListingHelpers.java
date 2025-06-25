package com.company.logistics.utils;

import com.company.logistics.models.contracts.Printable;

import java.util.List;
import java.util.stream.Collectors;

public class ListingHelpers {

    public static <T extends Printable> String elementsToString(List<T> elements) {
        if (elements == null || elements.isEmpty()) return "";

        String newLine = System.lineSeparator();
        return elements.stream()
                .map(Printable::print)
                .collect(Collectors.joining(newLine)) + PrintConstants.LINE_BREAK;
    }

}
