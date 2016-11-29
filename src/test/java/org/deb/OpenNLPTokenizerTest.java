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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import opennlp.tools.util.InvalidFormatException;





/**
 * @author debmalyajash
 *
 */
public class OpenNLPTokenizerTest {
	
	private static final String SENTENCE = "Mr. X and Mr. Y are two good friends.";
	
	private static final String TAG_SENTENCE = "Most large cities in the US had morning and afternoon newspapers, .";

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.deb.OpenNLPTokenizer#OpenNLPTokenizer()}.
	 */
	@Test
	public void testOpenNLPTokenizer() {
		try {
			OpenNLPTokenizer openNLPTokenizer = new OpenNLPTokenizer();
			openNLPTokenizer.getNames(SENTENCE);
			openNLPTokenizer.getTags(TAG_SENTENCE);
			openNLPTokenizer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertFalse(e.getMessage(),true);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertFalse(e.getMessage(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertFalse(e.getMessage(),true);
		}
	}

}
