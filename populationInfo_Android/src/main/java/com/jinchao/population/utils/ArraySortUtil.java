package com.jinchao.population.utils;
/**
 * 联动排序 冒泡
 * @author FRANK
 *
 */
public class ArraySortUtil {
	
	public static String[] arraySort(String[] str){
		
		 try {
			for(int i=0;i<str.length-1;i++){
				         for(int j=i+1;j<str.length;j++){
				          if (Integer.parseInt(str[i].substring(0, str[i].length()-1))>Integer.parseInt(str[j].substring(0, str[j].length()-1))){
				            String temp=str[i];
				            str[i]=str[j];
				            str[j]=temp;
				          }
				       }
				}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return str;
	}
	

}
