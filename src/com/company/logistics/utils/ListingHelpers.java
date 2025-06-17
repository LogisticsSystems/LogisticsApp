package com.company.logistics.utils;

import com.company.logistics.models.contracts.Printable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListingHelpers {

    public static <T extends Printable> String elementsToString(List<T> elements) {
        if (elements == null || elements.isEmpty()) {
            return "";
        }
        return elements.stream()
                .map(e->e.print())
                .collect(Collectors.joining("\n"));

    }

}
