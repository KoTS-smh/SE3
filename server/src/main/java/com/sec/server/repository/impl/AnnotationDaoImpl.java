package com.sec.server.repository.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sec.server.domain.Annotation;
import com.sec.server.domain.AnnotationInfo;
import com.sec.server.enums.ResultCode;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.AnnotationDao;
import com.sec.server.utils.Path;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("annotationDao")
public class AnnotationDaoImpl implements AnnotationDao {
    @Override
    public void updateAnnotation(String annotationInfo) {
        JSONObject jsonObject = JSON.parseObject(annotationInfo);
        long annotationId = jsonObject.getLong("annotationId");
        try {
            File file = new File(Path.annotationPath +annotationId+".txt");
            FileWriter fr = new FileWriter(file);
            fr.write(annotationInfo);
            fr.flush();
            fr.close();
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    @Override
    public String getAnnotation(long annotationId) {
        File file = new File(Path.annotationPath+annotationId+".txt");
        String s="";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br =new BufferedReader(fr);
            s=br.readLine();
            return s;
        } catch (IOException e) {
            throw new ResultException(ResultCode.UNKNOWN_ERROR);
        }
    }

    static long createAnnotationInfo(int imgNum){
        File file = new File(Path.annotationPath);
        if(!file.exists()){
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if(files !=null&&files.length!=0){
            file = files[files.length-1];
            String filename = file.getName();
            long thisId = Long.parseLong(filename.split("\\.")[0])+1;
            AnnotationInfo annotationInfo=new AnnotationInfo();
            annotationInfo.setAnnotationId(thisId);
            HashMap<Integer,List<Annotation>> hashMap=new HashMap<>();
            for(int i=1;i<=imgNum;i++){
                hashMap.put(i,new ArrayList<>());
            }
            annotationInfo.setAnnotationMap(hashMap);
            try {
                file = new File(Path.annotationPath+"/"+thisId+".txt");
                file.createNewFile();
                FileWriter fr = new FileWriter(file);
                fr.write(JSON.toJSONString(annotationInfo));
                fr.flush();
                fr.close();
                return thisId;
            } catch (IOException e) {
                throw new ResultException(ResultCode.UNKNOWN_ERROR);
            }
        }else {
            long thisId = 1;
            AnnotationInfo annotationInfo=new AnnotationInfo();
            annotationInfo.setAnnotationId(thisId);
            annotationInfo.setAnnotationMap(new HashMap<>());
            try {
                file = new File(Path.annotationPath+"/"+thisId+".txt");
                file.createNewFile();
                FileWriter fr = new FileWriter(file);
                fr.write(JSON.toJSONString(annotationInfo));
                fr.flush();
                fr.close();
                return thisId;
            } catch (IOException e) {
                throw new ResultException(ResultCode.UNKNOWN_ERROR);
            }
        }
    }
}
