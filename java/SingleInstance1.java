package com.post.thread;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class SingleInstance1 implements Serializable {
	private static final long serialVersionUID = 3501693088254419824L;

	private SingleInstance1() {

	}

	private static class InnerSingleInstance1 {
		private static SingleInstance1 _instance = new SingleInstance1();
	}

	public static SingleInstance1 getInstance() {
		return InnerSingleInstance1._instance;
	}

	protected Object readResolve() throws ObjectStreamException {
		System.out.println("调用了readResolve方法!");
		return InnerSingleInstance1._instance;
	}
}