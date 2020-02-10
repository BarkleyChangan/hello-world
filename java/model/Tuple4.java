package com.post.model.data;

/**
 * @author Barkley.Chang
 * @className:Tuple4
 * @description: TODO
 * @date 2020-01-25 07:21
 */
public class Tuple4<A, B, C, D> extends Tuple3 {
    public final D d;

    public Tuple4(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }

    @Override
    public String rep() {
        return super.rep() + "," + d;
    }
}