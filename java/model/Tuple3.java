package com.post.model.data;

/**
 * @author Barkley.Chang
 * @className:Tuple3
 * @description: TODO
 * @date 2020-01-25 07:19
 */
public class Tuple3<A, B, C> extends Tuple2<A, B> {
    public final C c;

    public Tuple3(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    @Override
    public String rep() {
        return super.rep() + "," + c;
    }
}