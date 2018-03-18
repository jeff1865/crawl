package com.yg.webshow.crawl.nlp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class HangleProcessor {
	
	public static void main(String ... v) {
		// 1
//		System.out.println("Start System ..");
//		System.out.println(getSourceText());
//		System.out.println("End of Document");
		
		// 2
//		Komoran komoran = new Komoran("/Users/1002000/dev/myworks/crawl/crawlCore/dicdata") ;
//		
//		List<List<Pair<String,String>>> result = komoran.analyze("건설중단이 (미래의) 전기요금에 영향을 주거나 대체전력 공급에 차질이 생길 우려가 있다면 배심원단 판단에 그 문제가 가장 중요하게 작용할 것") ;
//		
//		for (List<Pair<String, String>> eojeolResult : result) {
//			for (Pair<String, String> wordMorph : eojeolResult) {
//				System.out.println(wordMorph);
//			}
//			System.out.println();
//		}
		
		// 3
		filterNN() ;
		
	}
	
	public static void filterNN() {
		Komoran komoran = new Komoran("/Users/1002000/dev/myworks/crawl/crawlCore/dicdata") ;
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader("/Users/1002000/temp_han2/economy2018.txt"));
			
			String line = null; 
			while((line = br.readLine()) != null) {
				List<List<Pair<String,String>>> result = komoran.analyze(line);
				
				for (List<Pair<String, String>> eojeolResult : result) {
					for (Pair<String, String> wordMorph : eojeolResult) {
						String snd = wordMorph.getSecond() ;
						System.out.println("snd>" + snd + ", norm>" + wordMorph.getFirst());
						if(snd != null && snd.startsWith("NN")) {
							System.out.print(wordMorph.getFirst() + " ");
						}
					}
				}
				
				System.out.println();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		// ----
//		List<List<Pair<String,String>>> result = komoran.analyze("건설중단이 (미래의) 전기요금에 영향을 주거나 대체전력 공급에 차질이 생길 우려가 있다면 배심원단 판단에 그 문제가 가장 중요하게 작용할 것") ;
//		
//		for (List<Pair<String, String>> eojeolResult : result) {
//			for (Pair<String, String> wordMorph : eojeolResult) {
//				System.out.println(wordMorph);
//			}
//			System.out.println();
//		}
	}
	
	
	public static String getSourceText() {
		StringBuffer sb = new StringBuffer(5000000) ;
		
		BufferedReader br = null ;
		
		try {
			br = new BufferedReader(new FileReader("/Users/1002000/temp_han2/economy2018.txt"));
			String line = null ;
			int z = 0;
			StringTokenizer stkz = null ;
			
			while((line = br.readLine()) != null) {
				if(line.trim().length() > 1) {
					sb.append(line + "\n") ;
				}
				
//				String[] tokens = line.split("\\.");
//				String preBuf = "";
//				for(int i=0; i<tokens.length ;i++) {
//					String token = tokens[i];
//					
//					if(i == (tokens.length - 1) && !token.trim().endsWith(".")) {
////						System.out.println(i + "\t" + token);
//						preBuf = token ;
//					} else {
//						String sentence = preBuf +  token ;
//						if(sentence.trim().length() > 0 ) {
//							System.out.println(z++ + " -> " + preBuf + "_" + token);
//							preBuf = "";
//						}
//					}
//				} 
//				
				
//				stkz = new StringTokenizer(line, ".");
//				while(stkz.hasMoreTokens()) {
//					line = stkz.nextToken();
//					if(line.length() > 1) {
//						System.out.println(i++ + " -> " + line);
//					}
//				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		return sb.toString() ;
	}
	
	
}
