package com.yg.webshow.crawl.data.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

public class DefaultHTable extends HbaseControl {
	
	public DefaultHTable() {
		;
	}
	
	//TODO need to Action ( functional interface )
	public void execute() {
		
	}
	
	public void put(TableName table, List<Put> puts) {
		//TODO need to implements
		;
	}
	
	public <T> List<T> getData(TableName tableName, byte[] family, final ResultMapper<T> ob) {
		List<T> resData = new ArrayList<>();
		
		Table table = null;
		try {
			table = this.getTable(tableName);
			
			Scan scan = new Scan() ;
			ResultScanner rs = table.getScanner(scan) ;
			
			for(Result result : rs) {
				resData.add(ob.convert(result)) ;
			}
						
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(table != null) 
				this.releaseTable(table);
		}
				
		return resData ;
	}
	
	private Table getTable(TableName tableName) {
		return this.getTableFactory().getTable(tableName) ;
	}
	
	private void releaseTable(Table table) {
		this.getTableFactory().releaseTable(table);
	} 
}
