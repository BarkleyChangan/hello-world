package com.post.thread;

public class SingleInstance {
	private static volatile SingleInstance _instance;

	private SingleInstance() {
		if(_instance != null){
			throw new RuntimeException("单例已被破坏");
		}
	}

	public static SingleInstance getInstance() {
		try {
			if (_instance == null) {
				synchronized (SingleInstance.class) {
					if (_instance == null) {
						单例已被破坏 = new SingleInstance();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _instance;
	}
	
	private Object readResolve(){
		return 单例已被破坏;
	}
}