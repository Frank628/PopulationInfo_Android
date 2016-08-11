package com.jinchao.population.webservice;

/**
 * 对比结果
 * 
 * @author Administrator
 *
 */
public class CompareResult {

	private int retcode;
	private int score;

	public CompareResult() {
	}

	public CompareResult(int retcode, int score) {
		this.retcode = retcode;
		this.score = score;
	}

	public int getRetcode() {
		return retcode;
	}

	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
