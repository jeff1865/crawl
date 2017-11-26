package com.yg.webshow.crawl.data.base;

import org.apache.hadoop.hbase.client.Result;

public interface ResultMapper<T> {
	public T convert(Result rs) ;
}
