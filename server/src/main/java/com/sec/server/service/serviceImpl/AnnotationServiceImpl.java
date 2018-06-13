package com.sec.server.service.serviceImpl;

import com.sec.server.repository.AnnotationDao;
import com.sec.server.domain.Annotation;
import com.sec.server.domain.TaskOrder;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.service.AnnotationService;
import com.sec.server.utils.Stopwords;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;


@Service(   "annotationService")
public class AnnotationServiceImpl implements AnnotationService {
    @Autowired
    private AnnotationDao annotationDao;
    @Autowired
    private TaskOrderDao taskOrderDao;

    @Override
    @Transactional
    public void updateAnnotation(Annotation annotation) {
        try {
            if(annotation.getSentence()!=null&&!annotation.getSentence().equals(""))
                annotation.setWords(getWords(annotation.getSentence()));
            if(annotationDao.getOne(annotation.getTaskOrderId(),annotation.getPictureNum())==null){
                annotationDao.addAnnotation(annotation);
            }else {
                annotationDao.updateAnnotation(annotation);
            }
        }catch (Exception e){
            throw new ResultException("请完善标注！",132);
        }
    }

    @Override
    public Annotation getAnnotation(long taskOrderId, int pictureNum) {
        return annotationDao.getOne(taskOrderId,pictureNum);
    }

    @Override
    public List<Annotation> getAnnotations(long taskOrderId) {
        return annotationDao.getAll(taskOrderId);
    }

    @Override
    @Transactional
    public void deleteOne(long taskOrderId, int pictureNum) {
        try {
            annotationDao.deleteOneAnnotation(taskOrderId,pictureNum);
        }catch (Exception e){
            throw new ResultException("内容已被删除！",121);
        }
    }

    @Override
    @Transactional
    public void deleteAll(long taskOrderId) {
        try {
            annotationDao.deleteAllAnnotation(taskOrderId);
        }catch (Exception e){
            throw new ResultException("内容已被删除！",121);
        }
    }

    @Override
    public HashMap<String,Integer> getTags(long taskId, int pictureNum) {
        List<TaskOrder> taskOrders =taskOrderDao.getAllTaskOrderOfATask(taskId);
        List<String> sentences = new ArrayList<>();
        for(TaskOrder t:taskOrders){
            sentences.add(annotationDao.getOne(t.getTaskOrderId(),pictureNum).getWords());
        }
        if(sentences.size()==0){
            return null;
        }else {
            HashMap<String,Integer> tags = new HashMap<>();
            for(String s:sentences){
                String ss[] =  s.split(",");
                List<String> words = new ArrayList<>(Arrays.asList(ss));
                HashMap<String,Integer> tmp = getTag(words);
                if(tags.size() == 0){
                    tags = tmp;
                }else {
                    Set<String> keys = tmp.keySet();
                    for(String key:keys){
                        if(!tags.containsKey(key)){
                            tags.put(key,tags.get(key));
                        }else {
                            tags.put(key,tags.get(key)+tmp.get(key));
                        }
                    }
                }
            }
            List<Map.Entry<String,Integer>> list = new ArrayList<>(tags.entrySet());
            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            return tags;
        }
    }

    public HashMap<String,Integer> getTag(List<String> words){
        HashMap<String,Integer> result =new HashMap<>();
        if(words == null){
            return result;
        }
        for (String word : words) {
            String temp = isValue(word);
            if (temp != null) {
                if (!result.containsKey(temp)) {
                    result.put(temp, 1);
                } else {
                    result.put(temp, result.get(temp) + 1);
                }
            }
        }
        return result;
    }

    private String isValue(String word){
        if(Pattern.matches("^[a-z]+[']?[-]?[a-z]+$",word)) {
            String pattern = new Stopwords().getStopwordsRegex();
            if (Pattern.matches(pattern, word)) {
                return null;
            } else {
                return word;
            }
        }else {
            return null;
        }
    }

    //词性还原
    private List<String> stemmed(String inputStr) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        edu.stanford.nlp.pipeline.Annotation document = new edu.stanford.nlp.pipeline.Annotation(inputStr);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<String> outputStr = new ArrayList<>();
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String lema = token.get(CoreAnnotations.LemmaAnnotation.class);
                outputStr.add(lema);
            }
        }
        return outputStr;
    }

    private String getWords(String sentences){
        List<String> stemmedWords = stemmed(sentences);
        StringBuilder words = new StringBuilder();
        for(String s:stemmedWords){
            String tmp = isValue(s);
            if(tmp!=null){
                words.append(tmp).append(",");
            }
        }
        return words.deleteCharAt(words.length()-1).toString();
    }

}
