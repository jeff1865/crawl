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
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;

import com.yg.webshow.crawl.data.base.DefaultHTable;
import com.yg.webshow.crawl.data.base.ResultMapper;

/**
 * This class is a wrapper for Hbase table
 *
 */
public class CrawlTable {
	
	public static final TableName TABLE_NAME = TableName.valueOf("rtCrawl");
	
	private static final byte[] CF = Bytes.toBytes("cwl");
	
	private static final byte[] CQ_SITE_ID = Bytes.toBytes("sid");
	private static final byte[] CQ_CONTENTS = Bytes.toBytes("cont");
	private static final byte[] CQ_HTML_CONTENTS = Bytes.toBytes("hcnt");
	private static final byte[] CQ_ANCHOR_TITLE = Bytes.toBytes("alt");
	private static final byte[] CQ_DOC_TITLE = Bytes.toBytes("dtl");
	private static final byte[] CQ_CONT_TITLE = Bytes.toBytes("ctl");
	private static final byte[] CQ_AUTHOR = Bytes.toBytes("ath");
	private static final byte[] CQ_DOC_TS = Bytes.toBytes("dts");
	private static final byte[] CQ_STATUS = Bytes.toBytes("sts");
	private static final byte[] CQ_URL = Bytes.toBytes("url");
	private static final byte[] CQ_MEDIA_COUNT = Bytes.toBytes("mdc");
	
	private static final byte[] CQ_LATEST_UPD_TS = Bytes.toBytes("uts") ;
	private static final byte[] CQ_UPD_COUNT = Bytes.toBytes("upt") ;
	
	private static final String PREFIX_MEDIA = "med_";
	private static final String PREFIX_COMMENT = "cmt_";
	public static final String VAL_STATUS_INIT = "INIT";
	public static final String VAL_STATUS_EXTDATA = "MERGED";
	
	private DefaultHTable defaultHtable ;
	private ResultMapper<CrawlRow> resultMapper = null ;
	
	public CrawlTable(DefaultHTable htable) {
		this.defaultHtable = htable;
		
		this.resultMapper = new ResultMapper<CrawlRow>() {

			@Override
			public CrawlRow convert(Result rs) {
				CrawlRow cdb  = new CrawlRow() ; 
				String rowKey = Bytes.toString(rs.getRow());
				System.out.println("rowKey -> " + rowKey);
				if(rowKey != null) { 
					cdb.setPostId(String.valueOf(Integer.MAX_VALUE - Integer.parseInt(rowKey.split("_")[1])));
					cdb.setSiteId(rowKey.split("_")[0]);
				}
				
				while(rs.advance()) {
					Cell cell = rs.current();
					
//					if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_SITE_ID)) {
//						cdb.setSiteId(new String(CellUtil.cloneValue(cell)));
//					} else 
					if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_CONTENTS)) {
						cdb.setFilteredContents(new String(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_ANCHOR_TITLE)) {
						cdb.setAnchorTitle(new String(CellUtil.cloneValue(cell)));;
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_DOC_TITLE)) {
						cdb.setDocTitle(new String(CellUtil.cloneValue(cell)));;
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_AUTHOR)) {
						cdb.setAuthor(new String(CellUtil.cloneValue(cell)));;
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_DOC_TS)) {
						cdb.setDocTs(Bytes.toLong(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_STATUS)) {
						cdb.setStatus(new String(CellUtil.cloneValue(cell)));;
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_URL)) {
						cdb.setUrl(new String(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_HTML_CONTENTS)) {
						cdb.setHtmlContents(new String(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_LATEST_UPD_TS)) {
						cdb.setUpdateDate(Bytes.toLong(CellUtil.cloneValue(cell)));
					} else if(Arrays.equals(CellUtil.cloneQualifier(cell), CQ_UPD_COUNT)) {
						cdb.setUpdateCount(Bytes.toInt(CellUtil.cloneValue(cell)));
					}
				}
				
				return cdb;
			}
			
		} ;
		
	}
		
	public void updateContents(String siteId, String postId, String contents, String htmlContents, int count) {
		List<Put> puts = new ArrayList<Put>();
		Put put = new Put(this.createRowKey(siteId, postId));
		put.addColumn(CF, CQ_CONTENTS, Bytes.toBytes(contents));
		put.addColumn(CF, CQ_HTML_CONTENTS, Bytes.toBytes(htmlContents));
		put.addColumn(CF, CQ_STATUS, Bytes.toBytes(VAL_STATUS_EXTDATA));
		
		put.addColumn(CF, CQ_LATEST_UPD_TS, Bytes.toBytes(System.currentTimeMillis()));
		put.addColumn(CF, CQ_UPD_COUNT, Bytes.toBytes(count));
		puts.add(put) ;
		this.defaultHtable.put(TABLE_NAME, puts);;
	}
	
	public void updateStatus(String siteId, String postId, String status) {
		List<Put> puts = new ArrayList<Put>();
		Put put = new Put(this.createRowKey(siteId, postId));
		put.addColumn(CF, CQ_STATUS, Bytes.toBytes(status));
		
		puts.add(put) ;
		this.defaultHtable.put(TABLE_NAME, puts);;
	}
	
	//TODO need to consider Reflection!!
	public void insertData(CrawlRow crawlRow) {
		List<CrawlRow> data = new ArrayList<CrawlRow>(1);
		data.add(crawlRow);
		
		insertData(data);
	}
	
	public void insertData(List<CrawlRow> crawlRows) {
		List<Put> puts = new ArrayList<Put>();
		
		for(CrawlRow crawlRow : crawlRows) {
			Put put = new Put(this.createRowKey(crawlRow.getSiteId(), crawlRow.getPostId()));
			put.addColumn(CF, CQ_AUTHOR, Bytes.toBytes(crawlRow.getAuthor()));
			put.addColumn(CF, CQ_ANCHOR_TITLE, Bytes.toBytes(crawlRow.getAnchorTitle()));
			put.addColumn(CF, CQ_URL, Bytes.toBytes(crawlRow.getUrl()));
			put.addColumn(CF, CQ_DOC_TS, Bytes.toBytes(crawlRow.getDocTs()));
			put.addColumn(CF, CQ_STATUS, Bytes.toBytes(VAL_STATUS_INIT));
			
			put.addColumn(CF, CQ_DOC_TS, Bytes.toBytes(crawlRow.getDocTs()));
			
			put.addColumn(CF, CQ_LATEST_UPD_TS, Bytes.toBytes(System.currentTimeMillis())) ;
			put.addColumn(CF, CQ_UPD_COUNT, Bytes.toBytes(0)) ;
			
			puts.add(put) ;
		}
		
		this.defaultHtable.put(TABLE_NAME, puts);;
	}
	
	private byte[] createRowKey(String siteId, String postId) {
		StringBuffer sb = new StringBuffer();
		sb.append(siteId).append("_");
		sb.append(Integer.MAX_VALUE - Integer.parseInt(postId));
		
		return Bytes.toBytes(sb.toString()) ;
	}
	
	private byte[] createRowKey(String siteId, int postNo) {
		StringBuffer sb = new StringBuffer();
		sb.append(siteId).append("_");
		sb.append(Integer.MAX_VALUE - postNo);
		
		return Bytes.toBytes(sb.toString()) ;
	}
	
	public CrawlRow getData(String siteId, String postId) {
		return this.defaultHtable.getData(TABLE_NAME, CF, resultMapper, this.createRowKey(siteId, postId));
	}
	
	
	public List<CrawlRow> getLatestData(int topN, String status) {
		Scan scan = new Scan();
		scan.addFamily(CF);
		FilterList fList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		if(status != null) {
			fList.addFilter(new SingleColumnValueFilter(CF,
					CQ_STATUS, CompareOp.EQUAL, Bytes.toBytes(status)));
		}
		fList.addFilter(new PageFilter(topN));
		scan.setFilter(fList) ;
		
		return this.defaultHtable.getData(TABLE_NAME, CF, this.resultMapper, scan) ;
	}
	
	public List<CrawlRow> getLatestData(int pageNo, int pageCnt, String status) {
		Scan scan = new Scan();
		scan.addFamily(CF);
		FilterList fList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		if(status != null) {
			fList.addFilter(new SingleColumnValueFilter(CF,
					CQ_STATUS, CompareOp.EQUAL, Bytes.toBytes(status)));
		}
		
//		scan.setFilter(fList) ;
		
//		return this.defaultHtable.getData(TABLE_NAME, CF, this.resultMapper, scan) ;
		
		int startIndex = pageNo * pageCnt ;
		int endIndex = startIndex + pageCnt ;
		
		return this.defaultHtable.getData(TABLE_NAME, CF, this.resultMapper, scan, startIndex, endIndex) ;
		
	}
	
	public List<CrawlRow> getAllData() {
		return this.defaultHtable.getData(TABLE_NAME, CF, this.resultMapper) ;
	}
	
}
