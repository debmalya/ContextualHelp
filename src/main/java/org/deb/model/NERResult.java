/**
 * Copyright 2015-2016 Debmalya Jash
 * 
 */
package org.deb.model;

import java.util.List;

/**
 * @author debmalyajash
 *
 */
public class NERResult {
	
	private List<String> nerValue;
	
	private String sentence;

	/**
	 * 
	 */
	public NERResult(List<String> nerList, String sentence) {
		this.nerValue = nerList;
		this.sentence = sentence;
	}

	public List<String> getNerValue() {
		return nerValue;
	}

	public void setNerValue(List<String> nerValue) {
		this.nerValue = nerValue;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	

}
