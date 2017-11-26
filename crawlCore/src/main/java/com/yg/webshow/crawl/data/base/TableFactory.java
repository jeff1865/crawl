package com.yg.webshow.crawl.data.base;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Table;

public interface TableFactory {
	public Table getTable(TableName tableName);
	public void releaseTable(final Table table);
}
