package ru.spbau.mit;

/**
 * Created by kostya on 20.03.2016.
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class Function2Test {
    private Function2<Integer, Integer, Integer> div = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return integer / integer2;
        }
    };

    private Function2<Integer, Integer, Integer> mult2 = new Function2<Integer, Integer, Integer>() {
        @Override
        public Integer apply(Integer integer, Integer integer2) {
            return integer * integer2;
        }
    };

    private Function1<Integer, Integer> mult1 = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer * 2;
        }
    };

    private Function2<Object, Object, Integer> hash = new Function2<Object, Object, Integer>() {
        @Override
        public Integer apply(Object o1, Object o2) {
            return o1.hashCode() + o2.hashCode();
        }
    };

    @Test
    public void testSimple() {
        assertTrue(mult2.apply(2, 5) == 10);
    }

    @Test
    public void testDerive() {
        String str1 = "asd";
        String str2 = "qwe";
        assertTrue(hash.apply(str1, str2) == str1.hashCode() + str2.hashCode());
    }

    @Test
    public void testCompose() {
        Function2<Integer, Integer, Integer> comp = mult2.compose(mult1);
        assertTrue(comp.apply(2, 5) == 20);
    }

    @Test
    public void testBind1() {
        Function1<Integer, Integer> b1 = div.bind1(10);
        assertTrue(b1.apply(2) == 5);
    }

    @Test
    public void testBind2() {
        Function1<Integer, Integer> b2 = div.bind2(2);
        assertTrue(b2.apply(10) == 5);
    }

    @Test
    public void testCurry() {
        Function1<Integer, Function1<Integer, Integer>> c1 = div.curry();
        Function1<Integer, Integer> f1 = c1.apply(10);
        assertTrue(f1.apply(2) == 5);
    }
}
