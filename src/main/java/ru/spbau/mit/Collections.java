package ru.spbau.mit;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kostya on 20.03.2016.
 */
public final class Collections {
    private Collections() {}

    public static <T, R> Collection<R> map(Function1<? super T, R> f, Iterable<T> iter) {
        Collection<R> res = new ArrayList<R>();
        for (T i : iter) {
            res.add(f.apply(i));
        }
        return res;
    }

    public static <T> Collection<T> filter(Predicate<? super T> pred, Iterable<T> iter) {
        Collection<T> res = new ArrayList<T>();
        for (T i : iter) {
            if (pred.apply(i)) {
                res.add(i);
            }
        }
        return res;
    }

    public static <T> Collection<T> takeWhile(Predicate<? super T> pred, Iterable<T> iter) {
        Collection<T> res = new ArrayList<T>();
        for (T i : iter) {
            if (!pred.apply(i)) {
                break;
            }
            res.add(i);
        }
        return res;
    }

    public static <T> Collection<T> takeUnless(Predicate<? super T> pred, Iterable<T> iter) {
        return takeWhile(pred.not(), iter);
    }

    public static <A, B> B foldl(Function2<? super B, ? super A, B> fun2, Iterable<A> iter, B init) {
        B res = init;
        for (A i : iter) {
            res = fun2.apply(res, i);
        }
        return res;
    }

    public static <A, B> B foldr(Function2<? super A, ? super B, B> fun2, Iterable<A> iter, B init) {
        ArrayList<A> list = new ArrayList<A>();
        for (A i : iter) {
            list.add(i);
        }
        B res = init;
        java.util.Collections.reverse(list);
        for (A i : list) {
            res = fun2.apply(i, res);
        }
        return res;
    }
}
