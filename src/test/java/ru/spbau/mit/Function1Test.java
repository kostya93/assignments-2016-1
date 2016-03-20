package ru.spbau.mit;

/**
 * Created by kostya on 20.03.2016.
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class Function1Test {
    private Function1<Integer, Integer> mult = new Function1<Integer, Integer>() {
        @Override
        public Integer apply(Integer integer) {
            return integer * 2;
        }
    };
    private Function1<Object, Integer> hash = new Function1<Object, Integer>() {
        @Override
        public Integer apply(Object integer) {
            return integer.hashCode();
        }
    };

    @Test
    public void testSimple() {
        assertTrue(mult.apply(5) == 10);
    }

    @Test
    public void testDerive() {

        String str = "asd";
        assertTrue(str.hashCode() == hash.apply(str));
    }

    @Test
    public void testCompose() {
        assertTrue(mult.compose(mult).apply(10) == 40);
    }
}
