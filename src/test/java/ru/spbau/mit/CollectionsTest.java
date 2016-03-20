package ru.spbau.mit;

/**
 * Created by kostya on 20.03.2016.
 */

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionsTest {
    private List<Integer> list = Arrays.asList(2, 4, 8, 6, 7, 10, 15);

    private Function1<Integer, Integer> mult1 = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer * 2;
        }
    };

    private Predicate<Integer> div2 = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer % 2 == 0;
        }
    };

    private Predicate<Integer> div3 = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            return integer % 3 == 0;
        }
    };

    private Function2<Integer, Integer, Integer> fun2 = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return (integer + 2) * integer2;
        }
    };

    @Test
    public void testMap() {
        List<Integer> list2 = Arrays.asList(4, 8, 16, 12, 14, 20, 30);
        Collection<Integer> res = Collections.map(mult1, list);
        assertEquals(list2, res);
    }

    @Test
    public void testFilter() {
        List<Integer> listDiv2 = Arrays.asList(2, 4, 8, 6, 10);
        Collection<Integer> res = Collections.filter(div2, list);
        assertEquals(listDiv2, res);

        res = Collections.filter(Predicate.ALWAYS_TRUE, list);
        assertEquals(res, list);

        res = Collections.filter(Predicate.ALWAYS_FALSE, list);
        assertTrue(res.size() == 0);
    }

    @Test
    public void testTakeWhile() {
        List<Integer> listTW = Arrays.asList(2, 4, 8, 6);
        Collection<Integer> res = Collections.takeWhile(div2, list);
        assertEquals(listTW, res);
    }

    @Test
    public void testTakeUnless() {
        List<Integer> listTU = Arrays.asList(2, 4, 8);
        Collection<Integer> res = Collections.takeUnless(div3, list);
        assertEquals(listTU, res);
    }

    @Test
    public void testFoldl() {
        Integer res = Collections.foldl(fun2, list, 2);
        assertTrue(res == 2131830);
    }

    @Test
    public void testFoldr() {
        Integer res = Collections.foldr(fun2, list, 2);
        assertTrue(res == 7050240);
    }
}
