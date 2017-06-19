package com.yg;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;

public class AsyncHttpClient {

	public static void main(String... v) throws Exception {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		try {
			httpclient.start();
			Future<Boolean> future = httpclient.execute(HttpAsyncMethods.createGet("http://httpbin.org/"),
					new MyResponseConsumer(), null);
			Boolean result = future.get();
			if (result != null && result.booleanValue()) {
				System.out.println("Request successfully executed");
			} else {
				System.out.println("Request failed");
			}
			System.out.println("Shutting down");
		} finally {
			httpclient.close();
		}
		System.out.println("Done");
		;
	}

	static class MyResponseConsumer extends AsyncCharConsumer<Boolean> {

		@Override
		protected void onResponseReceived(final HttpResponse response) {
			System.out.println("onResponseReceived Event .. ");
		}

		@Override
		protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
			System.out.println("RCV Event .. " + buf);
			while (buf.hasRemaining()) {
				System.out.print(buf.get());
			}
		}

		@Override
		protected void releaseResources() {
			System.out.println("releaseResources Event .. ");
		}

		@Override
		protected Boolean buildResult(final HttpContext context) {
			System.out.println("buildResult Event .. ");
			return Boolean.TRUE;
		}

	}
}
