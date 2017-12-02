package com.yg.webshow.crawl.data.base;

import org.apache.hadoop.hbase.client.Table;

public interface TableFunction<T> {
	T invoke(Table table) throws Throwable ; 
}
