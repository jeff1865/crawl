package com.yg.webshow.crawl.data.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;

public class DefaultHTable extends HbaseControl {
	
	public DefaultHTable() {
		;
	}
		
	public void put(TableName tableName, List<Put> puts) {
		this.invoke(tableName, new TableFunction<Integer>() {
			@Override
			public Integer invoke(Table table) throws Throwable {
				table.put(puts);
				return 0;
			}
			
		}) ;
	}
	
	public <T> List<T> getData(TableName tableName, byte[] family, final ResultMapper<T> ob) {
		List<T> resData = new ArrayList<>();
		
		return this.invoke(tableName, new TableFunction<List<T>>() {
			@Override
			public List<T> invoke(Table table) throws Throwable {
				
				Scan scan = new Scan() ;
				ResultScanner rs = table.getScanner(scan) ;
				
				for(Result result : rs) {
					resData.add(ob.convert(result)) ;
				}
				
				return resData;
			}
		});
	}
	
	public <T> List<T> getData(TableName tableName, byte[] family, final ResultMapper<T> ob, final Scan scan, 
			int startIndex, int endIndex) {
		List<T> resData = new ArrayList<>();
		
		return this.invoke(tableName, new TableFunction<List<T>>() {
			@Override
			public List<T> invoke(Table table) throws Throwable {
				
//				Scan scan = new Scan() ;
				ResultScanner rs = table.getScanner(scan) ;
				
				int i = 0;
				for(Result result : rs) {
					if(startIndex <= i && i <= endIndex)
						resData.add(ob.convert(result)) ;
					else if(i > endIndex) {
						break ;
					}
					i ++ ;
				}
				
				return resData;
			}
		});
	}
	
	
	public <T> List<T> getData(TableName tableName, byte[] family, final ResultMapper<T> ob, final Scan scan) {
		List<T> resData = new ArrayList<>();
		
		return this.invoke(tableName, new TableFunction<List<T>>() {
			@Override
			public List<T> invoke(Table table) throws Throwable {
				
//				Scan scan = new Scan() ;
				ResultScanner rs = table.getScanner(scan) ;
				
				for(Result result : rs) {
					resData.add(ob.convert(result)) ;
				}
				
				return resData;
			}
		});
	}
	
	public <T> T getData(TableName tableName, byte[] family, final ResultMapper<T> ob, byte[] rowKey) {
		return this.invoke(tableName, new TableFunction<T>() {
			@Override
			public T invoke(Table table) throws Throwable {
				
				Get get = new Get(rowKey);
				Result rs = table.get(get) ;
				
				return ob.convert(rs);
			}
		});
		
	}
	
	public <T> T invoke(TableName tableName, TableFunction<T> func) {
		
		Table table = this.getTable(tableName);
		
		try {
			return func.invoke(table) ;
		} catch (Throwable e) {
			e.printStackTrace();
			throw new HbaseException(e.getMessage(), e);
		} finally {
			this.releaseTable(table);
		}
	
	}
	
	private Table getTable(TableName tableName) {
		return this.getTableFactory().getTable(tableName) ;
	}
	
	private void releaseTable(Table table) {
		this.getTableFactory().releaseTable(table);
	} 
}
