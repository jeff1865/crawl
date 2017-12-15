package com.yg.webshow.crawl.data.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.yg.webshow.crawl.data.base.DefaultHTable;
import com.yg.webshow.crawl.data.base.ResultMapper;

public class ExtDocTable {
	public static final TableName TABLE_NAME = TableName.valueOf("extDoc");
	
	private static final byte[] CF = Bytes.toBytes("cwl");
	
	private static final byte[] CQ_COMMENT_NO = Bytes.toBytes("cno");
	private static final byte[] CQ_USERID = Bytes.toBytes("usr");
	private static final byte[] CQ_INIT_TIME = Bytes.toBytes("its");
	private static final byte[] CQ_CONTENTS = Bytes.toBytes("cts");
//	private static final byte[] CQ_COMMENT_CNT = Bytes.toBytes("cnt");
	
	private DefaultHTable defaultHtable = null ;
	private ResultMapper<CrawlComment> resultMapper = null ;
	
	public ExtDocTable(DefaultHTable htable) {
		this.defaultHtable = htable ;
		
		this.resultMapper = new ResultMapper<CrawlComment>() {

			@Override
			public CrawlComment convert(Result rs) {
				CrawlComment crawlComment = new CrawlComment() ;
				
				String rowKey = Bytes.toString(rs.getRow());
				String[] tokens = rowKey.split("_");
				
				crawlComment.setSiteId(tokens[0]);
				crawlComment.setPostId(String.valueOf(Integer.MAX_VALUE - Integer.parseInt(tokens[1])));
				crawlComment.setCommentNo(String.valueOf(Integer.MAX_VALUE - Integer.parseInt(tokens[2])));
				
				while(rs.advance()) {
					Cell cell = rs.current();
										
					if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_USERID)) {
						crawlComment.setUser(new String(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_CONTENTS)) {
						crawlComment.setContents(new String(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_INIT_TIME)) {
						crawlComment.setInitTime(Bytes.toLong(CellUtil.cloneValue(cell)));;
					} 
				}
				
				return crawlComment;
			}
			
		};
		
	}
	
	public List<CrawlComment> getLatestComments(int topN) {
		Scan scan = new Scan();
		scan.addFamily(CF);
		FilterList fList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		fList.addFilter(new PageFilter(topN));
		scan.setFilter(fList) ;
		
		return this.defaultHtable.getData(TABLE_NAME, CF, this.resultMapper) ;
	}
	
	public void appendCommnetData(List<CrawlComment> lstCmt) {
		List<Put> puts = new ArrayList<Put>();
		
		for(CrawlComment cmt : lstCmt) {
			Put put = new Put(this.createRowKey(cmt.getSiteId(), 
					Integer.parseInt(cmt.getPostId()), 
					Integer.parseInt(cmt.getCommentNo())));
			
			if(cmt.getUser() != null) put.addColumn(CF, CQ_USERID, Bytes.toBytes(cmt.getUser())) ;
			if(cmt.getContents() != null) put.addColumn(CF, CQ_CONTENTS, Bytes.toBytes(cmt.getContents())) ;
			if(cmt.getInitTime() != 0L) put.addColumn(CF, CQ_INIT_TIME, Bytes.toBytes(cmt.getInitTime())) ;
			
			puts.add(put) ;
		}
		
		this.defaultHtable.put(TABLE_NAME, puts);
	}
	
	private byte[] createRowKey(String siteId, int postId, int cmtId) {
		StringBuffer sb = new StringBuffer() ;
		sb.append(siteId).append("_") ;
		sb.append(Integer.MAX_VALUE - postId).append("_") ;
		sb.append(Integer.MAX_VALUE - cmtId) ;
		
		return Bytes.toBytes(sb.toString()) ;
	}
}
