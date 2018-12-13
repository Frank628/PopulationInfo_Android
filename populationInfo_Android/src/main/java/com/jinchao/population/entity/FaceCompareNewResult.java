package com.jinchao.population.entity;

/**
 * Created by user on 2017/9/4.
 */

public class FaceCompareNewResult {
    public int code;
    public String message="";
    public FaceC data;
    public static class FaceC{
        public double similarity;
    }

}
