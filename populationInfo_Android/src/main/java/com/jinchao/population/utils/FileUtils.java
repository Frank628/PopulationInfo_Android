package com.jinchao.population.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class FileUtils {
	public static String getAssetsTxt(Context context,String filename){
		String txt="";
		try {  
			 InputStream is = context.getAssets().open(filename);  
			 int size = is.available();  
			 byte[] buffer = new byte[size];  
			 is.read(buffer);  
			 is.close();  
			 txt = new String(buffer);  
			 } catch (IOException e) {  
			   throw new RuntimeException(e);  
			 }  
		return txt;
	}
	public static String getStringFromFile(File file) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }
	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean   flag = false;
		File  file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}
}
