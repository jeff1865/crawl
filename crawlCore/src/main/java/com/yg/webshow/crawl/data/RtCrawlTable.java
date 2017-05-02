package com.yg.webshow.crawl.data;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;

import com.yg.webshow.crawl.core.SysConf;

/**
 * RowKey : [site_id]_[reversed_post_no]
 * CQ : timestamp
 * 		anchor_title
 * 		contents
 * 		cont_title
 * 		crawl_status
 * 		author
 * 		doc_ts
 * 		media_n
 */
public class RtCrawlTable extends AbstractTableEx {
	private static final byte[] TABLE_NAME = Bytes.toBytes("rtCrawl"); 
	private static final byte[] CF = Bytes.toBytes("cwl");
	
	private static final byte[] CQ_SITE_ID = Bytes.toBytes("sid");
	private static final byte[] CQ_CONTENTS = Bytes.toBytes("cont");
	private static final byte[] CQ_TITLE = Bytes.toBytes("tl");
	private static final byte[] CQ_DOC_TITLTE = Bytes.toBytes("dtl");
	private static final byte[] CQ_AUTHOR = Bytes.toBytes("ath");
	private static final byte[] CQ_DOC_TS = Bytes.toBytes("dts");
	private static final byte[] CQ_STATUS = Bytes.toBytes("sts");
	
	private static final String PREFIX_MEDIA = "med_";
	
	protected RtCrawlTable(Connection conn) {
		super(conn);
	}
	
	private byte[] createRowKey(String siteId, int postNo) {
		StringBuffer sb = new StringBuffer();
		sb.append(siteId).append("_");
		sb.append(Integer.MAX_VALUE - postNo);
		
		return Bytes.toBytes(sb.toString()) ;
	}
	
	public void AddInitData(String siteid, int postNo, String author, String anchorTitle, String docTs) {
		return ;
	}
	
	
	@Override
	public byte[] getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}
	
	public static void main(String ... v) {
		System.out.println("Run ...");
		RtCrawlTable test = new RtCrawlTable(SysConf.createNewHbaseConn());
		byte[] createdRowKey = test.createRowKey("1", 1399);
	}
}
