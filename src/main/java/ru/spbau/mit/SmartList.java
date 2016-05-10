package ru.spbau.mit;

import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {

    private int size;
    private Object data;

    public SmartList() {
        size = 0;
        data = null;
    }

    public SmartList(Collection<E> arr) {
        size = 0;
        data = null;
        for (E item : arr) {
            this.add(item);
        }
    }

    @Override
    public E get(int index) {
        if (index > size - 1) {
            throw new IndexOutOfBoundsException();
        } else if (size == 1) {
            return (E) data;
        } else if (size >= 2 && size <= 5) {
            return (E) ((Object[]) data)[index];
        } else {
            return (E) ((ArrayList<Object>) data).get(index);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E e) {
        if (size == 0) {
            data = e;
        } else if (size == 1) {
            Object old = data;
            Object[] newArr = new Object[5];
            newArr[0] = old;
            newArr[1] = e;
            data = newArr;
        } else if (size < 5) {
            ((Object[]) data)[size]  = e;
        } else if (size == 5) {
            ArrayList<Object> lst = new ArrayList<>();
            lst.addAll(Arrays.asList(((Object[]) data)).subList(0, size));
            lst.add(e);
            data = lst;
        } else {
            ((ArrayList<Object>) data).add(e);
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (size > 6) {
            size--;
            return ((ArrayList<Object>) data).remove(o);
        } else if (size == 6) {
            boolean isRemoved = ((ArrayList<Object>) data).remove(o);
            if (isRemoved) {
                Object[] newArr = new Object[5];
                for (int i = 0; i < 5; i++) {
                    newArr[i] = ((ArrayList<Object>) data).get(i);
                }
                size = 5;
                data = newArr;
            }
            return isRemoved;
        } else if (size > 2) {
            for (int i = 0; i < 5; i++) {
                if (o.equals(((Object[]) data)[i])) {
                    ((Object[]) data)[i] = null;
                    size--;
                    return true;
                }
            }
            return false;
        } else if (size == 0) {
            return false;
        }
        if (data.equals(o)) {
            data = null;
            size = 0;
            return true;
        }
        return false;
    }

    @Override
    public E set(int index, E element) {
        if (index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            E res = (E) data;
            data = element;
            return res;
        } else if (size <= 5) {
            E res = ((E[]) data)[index];
            ((E[]) data)[index] = element;
            return res;
        } else {
            return ((ArrayList<E>) data).set(index, element);
        }
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        size--;
        if (size > 5) {
            return ((ArrayList<E>) data).remove(index);
        } else if (size == 5) {
            Object[] newArr = new Object[5];
            E res = ((ArrayList<E>) data).remove(index);

            for (int i = 0; i < size; i++) {
                ((E[]) newArr)[i] = ((ArrayList<E>) data).get(i);
            }
            data = newArr;
            return res;
        } else if (size >= 2) {
            E res = ((E[]) data)[index];
            for (int i = index + 1; i < size; ++i) {
                ((E[]) data)[index - 1] = ((E[]) data)[index];
            }
            ((E[]) data)[size] = null;
            return res;
        } else if (size == 1) {
            E res = ((E[]) data)[index];
            data = ((E[]) data)[1 - index];
            return res;
        } else {
            //size == 0
            E res = (E) data;
            data = null;
            return res;
        }
    }
}
