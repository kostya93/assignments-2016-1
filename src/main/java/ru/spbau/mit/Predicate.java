package ru.spbau.mit;

/**
 * Created by kostya on 20.03.2016.
 */
public abstract class Predicate<T> extends Function1<T, Boolean> {
    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object o) {
            return true;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object o) {
            return false;
        }
    };

    public Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T t) {
                return !Predicate.this.apply(t);
            }
        };
    }

    public Predicate<T> or(final Predicate<? super T> g) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T t) {
                return Predicate.this.apply(t) || g.apply(t);
            }
        };
    }

    public Predicate<T> and(final Predicate<? super T> g) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T t) {
                return Predicate.this.apply(t) && g.apply(t);
            }
        };
    }
}
