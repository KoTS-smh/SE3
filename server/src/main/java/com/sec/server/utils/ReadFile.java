package com.sec.server.utils;

import java.io.*;

public class ReadFile {
    /**
     * 读取json文件，返回文件内容
     */

    public static String readFile(String path) throws IOException{
        File file = new File(path);

        if(!file.exists() || file.isDirectory()){
            throw new FileNotFoundException();
        }

        StringBuffer sb = new StringBuffer();

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineText = null;
            while((lineText = bufferedReader.readLine()) != null) {
                sb.append(lineText);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return sb.toString();
    }
}
