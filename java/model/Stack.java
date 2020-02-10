package com.post.model.data;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Barkley.Chang
 * @className:Stack
 * @description: TODO
 * @date 2020-01-22 10:45
 */
public class Stack<T> {
    private Deque<T> storage = new ArrayDeque<>();

    public void push(T t) {
        storage.push(t);
    }

    public T peek() {
        return storage.peek();
    }

    public T pop() {
        return storage.pop();
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}