package com.post.model.data;

/**
 * @author Barkley.Chang
 * @className:Tuple2
 * @description: TODO
 * @date 2020-01-25 07:16
 */
public class Tuple2<A, B> {
    public final A a;
    public final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public String rep() {
        return a + "," + b;
    }

    @Override
    public String toString() {
        return "(" + rep() + ")";
    }
}