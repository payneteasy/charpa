package com.googlecode.charpa.progress.service.impl;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author rpuch
 */
public class LimitedCapacityDroppingList<E> extends AbstractList<E> {
    private final int maxCapacity;

    private LinkedList<E> deque = new LinkedList<E>();

    public LimitedCapacityDroppingList(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("maxCapacity must be positive");
        }
        this.maxCapacity = maxCapacity;
    }

    @Override
    public E get(int index) {
        return deque.get(index);
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public synchronized boolean add(E e) {
        while (deque.size() >= maxCapacity) {
            deque.remove();
        }
        deque.add(e);
        return true;
    }

    @Override
    public E remove(int index) {
        return deque.remove(index);
    }

    @Override
    public Iterator<E> iterator() {
        return deque.iterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return deque.listIterator(index);
    }
}
