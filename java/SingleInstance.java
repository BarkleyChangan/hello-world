package com.post.thread;

public class SingleInstance {
	private static volatile SingleInstance _instance;

	private SingleInstance() {

	}

	public static SingleInstance getInstance() {
		try {
			if (_instance == null) {
				synchronized (SingleInstance.class) {
					if (_instance == null) {
						_instance = new SingleInstance();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _instance;
	}
}