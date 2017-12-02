package com.yg.webshow.crawl.data.base;

public class HbaseException extends RuntimeException {
	private static final long serialVersionUID = -33362480511889550L;
		
	public HbaseException(String msg, Throwable e) {
		super(msg, e);
	}
}
