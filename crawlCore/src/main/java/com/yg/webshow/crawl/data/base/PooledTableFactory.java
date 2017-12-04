package com.yg.webshow.crawl.data.base;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yg.webshow.crawl.core.SystemProperties;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.hadoop.conf.Configuration;

@Component
public class PooledTableFactory implements TableFactory {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int DEFAULT_POOL_SIZE = 256;
    public static final int DEFAULT_WORKER_QUEUE_SIZE = 1024*5;
    public static final boolean DEFAULT_PRESTART_THREAD_POOL = false;
	
    private LinkedBlockingQueue<Runnable> queue = null ;
    
	private ExecutorService executor;
    private Connection connection;
    
    @Autowired SystemProperties systemProperties ;
    
    public PooledTableFactory() {
    	;
    }
    
    @PostConstruct
    public void init() throws IOException {
    	logger.info("HTable is just initialized by postConsturctor ..");
    	    	
    	Configuration config = HBaseConfiguration.create();
		
		//LOCAL_DEV
		config.set("hbase.zookeeper.quorum", systemProperties.getHbaseQuorum());
		config.set("hbase.master", this.systemProperties.getHbaseMaster());
		
    	this.executor = this.createExecutorService(DEFAULT_POOL_SIZE, DEFAULT_WORKER_QUEUE_SIZE, DEFAULT_PRESTART_THREAD_POOL) ;
    
    	this.connection = ConnectionFactory.createConnection(config, this.executor) ;
    }
    
    public void close() {
    	;	//TODO need to implements
    }
    
    private  ExecutorService createExecutorService(int poolSize, int workQueueMaxSize, boolean prestartThreadPool) {
    	logger.info("create HConnectionThreadPoolExecutor poolSize:{}, workerQueueMaxSize:{}", poolSize, workQueueMaxSize);
    	this.queue = new LinkedBlockingQueue<Runnable>() ;
    	
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolSize, workQueueMaxSize, 5000L, TimeUnit.MILLISECONDS, this.queue);
        if (prestartThreadPool) {
            logger.info("prestartAllCoreThreads");
            threadPoolExecutor.prestartAllCoreThreads();
        }

        return threadPoolExecutor;
    }
    
    public Connection getConnection() {
    	return this.connection ;
    }
    
    public DefaultHTable getDefaultTable() {
    	DefaultHTable defaultTable = new DefaultHTable();
    	defaultTable.setTableFactory(this);
//    	defaultTable.setConfiguration(configuration);
    	
    	return defaultTable ;
    }
    
	@Override
	public Table getTable(TableName tableName) {
		Table table = null ;
		try {
			table = this.connection.getTable(tableName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return table;
	}

	@Override
	public void releaseTable(Table table) {
		try {
			if(table != null) table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
