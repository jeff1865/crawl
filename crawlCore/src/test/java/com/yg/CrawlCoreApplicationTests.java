package com.yg;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yg.webshow.crawl.core.CrawlCoreApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CrawlCoreApplication.class)
@WebAppConfiguration
public class CrawlCoreApplicationTests {

	@Test
	public void contextLoads() {
	}

}
