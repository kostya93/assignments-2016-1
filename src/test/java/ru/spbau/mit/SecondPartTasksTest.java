package ru.spbau.mit;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() {
        final List<String> paths = Arrays.asList(
                "src/test/resources/file1",
                "src/test/resources/file2",
                "src/test/resources/file3"
        );
        final List<String> test1 = Arrays.asList(
                "зарождение барокко",
                "градостроительство оказали",
                "средневековые замки",
                "декорированными залами.",
                "затяжные войны с",
                "было связано"
        );
        assertEquals(test1, SecondPartTasks.findQuotes(paths, "за"));
    }

    @Test
    public void testPiDividedBy4() {
        assertTrue(Math.abs(Math.PI / 4 - SecondPartTasks.piDividedBy4() ) < 0.01);
    }

    @Test
    public void testFindPrinter() {
        final String a1 = "a1";
        final String a2 = "a2";
        final String a3 = "a3";
        final List<String> l1 = Arrays.asList("composition1", "composition2, composition2", "composition3, composition3, composition3");
        final List<String> l2 = Arrays.asList("composition1", "composition2, composition2");
        final List<String> l3 = Arrays.asList("composition1");
        final Map<String, List<String>> map = ImmutableMap.of(a1, l1, a2, l2, a3, l3);

        assertEquals(a1, SecondPartTasks.findPrinter(map));
    }

    @Test
    public void testCalculateGlobalOrder() {
        final Map<String, Integer> provider1 = ImmutableMap.of("product1", 10);
        final Map<String, Integer> provider2 = ImmutableMap.of("product1", 10, "product2", 20);
        final Map<String, Integer> provider3 = ImmutableMap.of("product1", 10, "product2", 20, "product3", 30);
        final Map<String, Integer> res = ImmutableMap.of("product1", 30, "product2", 40, "product3", 30);
        assertEquals(res, SecondPartTasks.calculateGlobalOrder(Arrays.asList(provider1, provider2, provider3)));
    }
}
