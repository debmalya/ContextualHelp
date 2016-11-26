/**
 * Copyright 2015-2016 Debmalya Jash
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deb;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.deb.model.SentimentResult;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author debmalyajash
 *
 */
public class NamedEntityRecognition {

	private static final String serializedClassifier = "./src/classifiers/english.muc.7class.distsim.crf.ser.gz";
	// private static final String serializedClassifier =
	// "./src/classifiers/english.all.3class.distsim.crf.ser.gz";

	private static final AbstractSequenceClassifier classifier = CRFClassifier
			.getClassifierNoExceptions(serializedClassifier);

	/**
	 * 
	 */
	public NamedEntityRecognition() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try (Scanner in = new Scanner(System.in)) {

			System.out.println("Enter customer feedback :");
			String customerFeedback = in.nextLine();
			if (customerFeedback != null && customerFeedback.trim().length() > 0) {
				// Recognize the feedback.
				// Display the feedback.
				// Want to try more ?
				classify(customerFeedback);
				analyzeSentiment(customerFeedback);

			}

		}
	}

	/**
	 * @param customerFeedback
	 */
	public static void classify(String customerFeedback) {
		int count = 0;
		List<List<CoreLabel>> out = classifier.classify(customerFeedback);
		for (List<CoreLabel> sentence : out) {
			count = 0;
			for (CoreLabel word : sentence) {
				String customerWord = word.word();
				String customerWordAnnotation = word.get(AnswerAnnotation.class);
				if (!customerWordAnnotation.equals("O")) {
					System.out.print(customerWord + '/' + customerWordAnnotation + ' ');
					count++;
				}
			}
			
			if (count > 0) {
				System.out.println();
			}
			
		}
		
		
	}

	/**
	 * 
	 * @param customerFeedback
	 */
	public static List<SentimentResult>  analyzeSentiment(String customerFeedback) {
		// This part is for sentiment analysis
		List<SentimentResult> sentimentResultList = new ArrayList<>();
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(customerFeedback);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		  String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
		  System.out.println(sentiment + "\t" + sentence);
		  
		  // Instantiate new SentimentResult object and set its properties
		  SentimentResult sr = new SentimentResult(sentiment,sentence.toString());
		  
		  
		  sentimentResultList.add(sr);
		  
		}
		
		return sentimentResultList;
	}

}
