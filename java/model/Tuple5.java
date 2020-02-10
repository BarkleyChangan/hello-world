package com.post.model.data;

/**
 * @author Barkley.Chang
 * @className:Tuple5
 * @description: TODO
 * @date 2020-01-25 07:22
 */
public class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {
    public final E e;

    public Tuple5(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        this.e = e;
    }

    @Override
    public String rep() {
        return super.rep() + "," + e;
    }
}