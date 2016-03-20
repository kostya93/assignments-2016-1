package ru.spbau.mit;

/**
 * Created by kostya on 20.03.2016.
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class PredicateTest {
    private Predicate<Object> hash2 = new Predicate<Object>() {
        @Override
        public Boolean apply(Object o) {
            return o.hashCode() % 2 == 0;
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

    private Predicate<Integer> undefined = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer integer) {
            throw new RuntimeException();
        }
    };

    @Test
    public void testSimple() {
        assertTrue(div2.apply(4));
    }

    @Test
    public void testDerive() {
        String str = "qwe";
        assertTrue(hash2.apply(str) == (str.hashCode() % 2 == 0));
    }

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicate.ALWAYS_TRUE.apply("asd"));
    }

    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicate.ALWAYS_FALSE.apply("zxc"));
    }

    @Test
    public void testNot() {
        assertTrue(Predicate.ALWAYS_FALSE.not().apply("asd"));
        assertFalse(Predicate.ALWAYS_TRUE.not().apply("qwe"));
        assertTrue(div2.not().apply(5));
        assertFalse(div3.not().apply(6));
    }

    @Test
    public void testOr() {
        assertTrue(div2.or(div3).apply(3));
        assertTrue(div2.or(div3).apply(4));
        assertTrue(div2.or(div3).apply(6));
        assertFalse(div2.or(div3).apply(7));

        //Lazy evaluation test
        assertTrue(div2.or(undefined).apply(6));
    }

    @Test
    public void testAnd() {
        assertTrue(div2.and(div3).apply(6));
        assertFalse(div2.and(div3).apply(4));
        assertFalse(div2.and(div3).apply(3));
        assertFalse(div2.and(div3).apply(7));

        //Lazy evaluation test
        assertFalse(div2.and(undefined).apply(7));
    }


}
