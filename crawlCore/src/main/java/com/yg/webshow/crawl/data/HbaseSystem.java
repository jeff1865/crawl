package com.yg.webshow.crawl.data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yg.webshow.crawl.core.SystemProperties;

@Service
public class HbaseSystem {
	
	@Autowired private SystemProperties systemProperties = null ;
	
	public HbaseSystem() {
		;
	}
	
	
	public Configuration createConfig() {
		Configuration config = HBaseConfiguration.create();
		
		config.set("hbase.zookeeper.quorum", this.systemProperties.getHbaseQuorum());
		config.set("hbase.master", this.systemProperties.getHbaseMaster());
				
		return config ;
	}
	
	
}
