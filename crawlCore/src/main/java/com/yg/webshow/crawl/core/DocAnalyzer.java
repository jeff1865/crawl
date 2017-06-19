package com.yg.webshow.crawl.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
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
			.negativeSample(10) // sample size 10 words
			.iterate(iter) //
			.tokenizerFactory(tokenizer)
			.build();
		vec.fit();
		
		// Write word vectors
		WordVectorSerializer.writeWordVectors(vec, "/Users/1002000/dev/temp_doc/res_" + System.currentTimeMillis() + ".txt");

		log.info("Closest Words:");
		Collection<String> lst = vec.wordsNearest("일본", 10);
		System.out.println(lst);	
		
		// Server doesn't work
//		UiServer server = UiServer.getInstance();
//		System.out.println("Started on port " + server.getPort());
		
		double cosSim = vec.similarity("선생", "일본");
		System.out.println(cosSim);
				
		// Model Visualization
		log.info("Plot TSNE....");
		BarnesHutTsne tsne = new BarnesHutTsne.Builder()
			.setMaxIter(1000)
			.stopLyingIteration(250)
			.learningRate(500)
			.useAdaGrad(false)
			.theta(0.5)
			.setMomentum(0.5)
			.normalize(true)
//			.usePca(false)
			.build();
		vec.lookupTable().plotVocab(tsne, 20, new File("/Users/1002000/dev/temp_doc/flotvoca1.txt"));
	}
	
	public static void main(String ... v) {
//		TokenizerFactory t = new DefaultTokenizerFactory();
//		TokenizerFactory kt = new KoreanTokenizerFactory();
//		kt.setTokenPreProcessor(new CommonPreprocessor());
		
		String file = "/Users/1002000/dev/temp_doc/aaa.txt";
		try {
			System.out.println("Processing File ..");
			process(file);
			System.out.println("Processing File completed ..");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
