package com.yg.webshow.crawl.nlp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.KoreanTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.ui.UiServer;
import org.nd4j.linalg.io.ClassPathResource;

//import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;

public class DocAnalyzer {
	private static Logger log = Logger.getLogger(DocAnalyzer.class) ;	
	
	public DocAnalyzer() {
		;
	}
	
	public static void process(String srcFile) throws Exception {
		log.info("Load data....");
//		ClassPathResource resource = new ClassPathResource("raw_sentences.txt");
//		SentenceIterator iter = new LineSentenceIterator(resource.getFile());
		
		// Init SentenceIterator
		SentenceIterator iter = new LineSentenceIterator(new File(srcFile));
		iter.setPreProcessor(new SentencePreProcessor() {
		    @Override
		    public String preProcess(String sentence) {
		    	return sentence.toLowerCase();
		    }
		});
				
		// Init Tokenizer
		TokenizerFactory tokenizer = new KoreanTokenizerFactory();
		tokenizer.setTokenPreProcessor(new CommonPreprocessor());
		
		// Set word2vec
		int batchSize = 1000;
		int iterations = 3;
		int layerSize = 150;

		log.info("Build model....");
		Word2Vec vec = new Word2Vec.Builder()
			.batchSize(batchSize) //# words per minibatch.
			.minWordFrequency(5) //
			.useAdaGrad(false) //
			.layerSize(layerSize) // word feature vector size
			.iterations(iterations) // # iterations to train
			.learningRate(0.025) //
			.minLearningRate(1e-3) // learning rate decays wrt # words. floor learning
			.negativeSample(100) // sample size 10 words
			.iterate(iter) //
			.tokenizerFactory(tokenizer)
			.build();
		vec.fit();
		
		// Write word vectors
		WordVectorSerializer.writeWordVectors(vec, "/Users/1002000/temp_han2/res_" + System.currentTimeMillis() + ".txt");
		
//		WordVectorSerializer.loadTxtVectors("")
		
		
//		log.info("Closest Words:");
		Collection<String> lst = vec.wordsNearest("일본", 10);
		System.out.println("\'Dragon\' sim ---> "+ lst);	
		
		// Server doesn't work
//		UiServer server = UiServer.getInstance();
//		System.out.println("Started on port " + server.getPort());
		
		double cosSim = vec.similarity("일본", "중국");
		System.out.println("cosine sim --->" + cosSim);
		
//		System.out.println("AA--");
		
		// Model Visualization
		log.info("Plot TSNE....");
		BarnesHutTsne tsne = new BarnesHutTsne.Builder()
			.setMaxIter(200)
			.stopLyingIteration(250)
			.learningRate(500)
			.useAdaGrad(false)
			.theta(0.5)
			.setMomentum(0.5)
			.normalize(true)
//			.usePca(false)
			.build();
		vec.lookupTable().plotVocab(tsne, 200, new File("/Users/1002000/temp_han2/eco2018tse2.txt"));
	}
	
	public static void dataTest(String file) throws Exception {
		WordVectors vec = WordVectorSerializer.loadTxtVectors(new File(file)) ;
		
		String qryWord = "둔화";
		Collection<String> wordsNearest = vec.wordsNearest(qryWord, 30);
		System.out.println(qryWord + "->" + wordsNearest);
		
		double cosSim = vec.similarity("일본", "중국");
		System.out.println("일본 중국 : cosine sim --->" + cosSim);
		cosSim = vec.similarity("일본", "러시아");
		System.out.println("일본 러시아 : cosine sim --->" + cosSim);
	}
	
	public static void main(String ... v) {
//		TokenizerFactory t = new DefaultTokenizerFactory();
//		TokenizerFactory kt = new KoreanTokenizerFactory();
//		kt.setTokenPreProcessor(new CommonPreprocessor());
		
		String file = "/Users/1002000/temp_han2/economy2018.txt";
		try {
//			System.out.println("Processing File ..");
//			process(file);
//			System.out.println("Processing File completed ..");
			
			
			dataTest("/Users/1002000/temp_han2/res_1519278754296.txt") ;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
