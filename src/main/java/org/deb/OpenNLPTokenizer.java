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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

/**
 * @author debmalyajash
 *
 */
public class OpenNLPTokenizer {

	private static InputStream modelIn;

	private static InputStream modelInPOS;

	private static TokenNameFinderModel model;

	private static NameFinderME nameFinder;

	private static POSModel posModel;

	private static POSTaggerME tagger;

	/**
	 * Default constructor.
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException
	 * 
	 */
	public OpenNLPTokenizer() throws InvalidFormatException, IOException {
		if (modelIn == null) {
			modelIn = new FileInputStream("./src/main/resources/en-ner-person.bin");
		}

		if (model == null) {
			model = new TokenNameFinderModel(modelIn);
		}

		if (nameFinder == null) {
			nameFinder = new NameFinderME(model);
		}

		if (modelInPOS == null) {
			modelInPOS = new FileInputStream("./src/main/resources/en-pos-maxent.bin");
		}

		if (posModel == null) {
			posModel = new POSModel(modelInPOS);
		}

		if (tagger == null) {
			tagger = new POSTaggerME(posModel);
		}
	}

	/**
	 * The NameFinderME class is not thread safe, it must only be called from
	 * one thread. After every document clearAdaptiveData must be called to
	 * clear the adaptive data in the feature generators. Not calling
	 * clearAdaptiveData can lead to a sharp drop in the detection rate after a
	 * few documents.
	 * 
	 * @param sentences
	 *            to be analyzed for names.
	 */
	public synchronized void getNames(final String sentences) {
		Span nameSpans[] = null;
		String[] allSentences = sentences.split(".");
		nameSpans = nameFinder.find(allSentences);
		System.out.println(nameSpans.length);

		nameFinder.clearAdaptiveData();
	}
	
	public synchronized void getTags(final String sentences){
		String[] allSentences = sentences.split(".");
		String tags[] = tagger.tag(allSentences);
	}

	public void close() {
		closeInputStream(modelIn);
		closeInputStream(modelInPOS);
	}

	/**
	 * 
	 */
	public void closeInputStream(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException ignore) {
			}
		}
	}

}
