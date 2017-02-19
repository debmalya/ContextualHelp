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

import org.deb.model.NERResult;
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

			System.out.println("Enter your text :");
			String customerFeedback = in.nextLine();
			if (customerFeedback != null && customerFeedback.trim().length() > 0) {
				// Recognize the feedback.
				// Display the feedback.
				// Want to try more ?
				System.out.println("=== Stanford Classification ===");
				classify(customerFeedback);
				System.out.println("=== Stanford sentiment analysis ===");
				analyzeSentiment(customerFeedback);
				tagPOS(customerFeedback);

			} else {
				System.err.println("ERR: please enter something to evaluate.");
			}

		}
	}

	/**
	 * @param customerFeedback
	 */
	public static List<NERResult> classify(String customerFeedback) {
		int count = 0;
		List<List<CoreLabel>> out = classifier.classify(customerFeedback);
		List<NERResult> nerResult = new ArrayList<>();
		for (List<CoreLabel> sentence : out) {
			count = 0;
			List<String> eachNerResult = new ArrayList<>();
			for (CoreLabel word : sentence) {
				String customerWord = word.word();
				String customerWordAnnotation = word.get(AnswerAnnotation.class);
				if (!customerWordAnnotation.equals("O")) {
					System.out.print(customerWord + '/' + customerWordAnnotation + ' ');
					count++;
					eachNerResult.add(customerWord + '/' + customerWordAnnotation);
				}
			}

			if (count > 0) {
				System.out.println();
				NERResult result = new NERResult(eachNerResult, sentence.toString());
				nerResult.add(result);
			}

		}

		return nerResult;

	}

	/**
	 * 
	 * @param customerFeedback
	 */
	public static List<SentimentResult> analyzeSentiment(String customerFeedback) {
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
			SentimentResult sr = new SentimentResult(sentiment, sentence.toString());

			sentimentResultList.add(sr);

		}

		return sentimentResultList;
	}

	public static List<String> tagPOS(String text) {
		List<String> posList = new ArrayList<>();

		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(CoreAnnotations.TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				posList.add(pos);
				String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

				System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
			}
			
		}
		return posList;
	}
}
