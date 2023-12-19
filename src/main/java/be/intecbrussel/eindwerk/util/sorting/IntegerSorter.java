package be.intecbrussel.eindwerk.util.sorting;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IntegerSorter {
    public List<Integer> sortHighToLow(List<Integer> listToSort) {
        return listToSort.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}
