package com.yg.webshow.crawl.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SystemProperties {
	@Value("${hbase.master}")
	private String hbaseMaster ;
	@Value("${hbase.quorum}")
	private String hbaseQuorum ;
	
	public SystemProperties() {
		;
	}
	
	public String getHbaseMaster() {
		return hbaseMaster;
	}

	public void setHbaseMaster(String hbaseMaster) {
		this.hbaseMaster = hbaseMaster;
	}

	public String getHbaseQuorum() {
		return hbaseQuorum;
	}

	public void setHbaseQuorum(String hbaseQuorum) {
		this.hbaseQuorum = hbaseQuorum;
	}
	
	
	
}
