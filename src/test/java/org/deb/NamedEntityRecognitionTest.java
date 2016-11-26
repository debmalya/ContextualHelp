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

import static org.junit.Assert.*;

import java.util.List;

import org.deb.model.SentimentResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author debmalyajash
 *
 */
public class NamedEntityRecognitionTest {

	/**
	 * Test method for
	 * {@link org.deb.NamedEntityRecognition#analyzeSentiment(java.lang.String)}
	 * .
	 */
	@Test
	public final void testAnalyzeSentiment() {
		// Test for Neutral
		List<SentimentResult> result = NamedEntityRecognition
				.analyzeSentiment("I am using stanford core-nlp");
		Assert.assertTrue(!result.isEmpty());
		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getSentiment().equals("Neutral"));
		result = NamedEntityRecognition
				.analyzeSentiment("But not sure how to use it in a profitable business");
		Assert.assertTrue(!result.isEmpty());
		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getSentiment().equals("Neutral"));

		// Test for Positive
		result = NamedEntityRecognition
				.analyzeSentiment("It is exciting to use stanford core-nlp");
		Assert.assertTrue(!result.isEmpty());
		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getSentiment().equals("Positive"));

		// Test for Negative
		result = NamedEntityRecognition
				.analyzeSentiment("Bussiness people will feel frustrated, what the use of it?");
		Assert.assertTrue(!result.isEmpty());
		Assert.assertTrue(result.size() == 1);
		Assert.assertTrue(result.get(0).getSentiment().equals("Negative"));
	}

}
