package com.sec.server.service.serviceImpl;

import com.sec.server.domain.Annotation;
import com.sec.server.domain.Task;
import com.sec.server.domain.TaskOrder;
import com.sec.server.enums.AnnotationType;
import com.sec.server.exception.ResultException;
import com.sec.server.repository.ImgUrlDao;
import com.sec.server.repository.TaskDao;
import com.sec.server.repository.TaskOrderDao;
import com.sec.server.service.AnnotationService;
import com.sec.server.service.EvaluateService;
import com.sec.server.utils.BRISQUE;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.log;
import static java.lang.Math.sqrt;
import static org.opencv.core.Core.meanStdDev;

@Service("evaluateService")
public class EvaluateServiceImpl implements EvaluateService {
    private final int minAutoEvaluateNum = 10;
    private final int bigNumStandard = 36;
    private final int smallNumTimes = 1;
    private final int bigNumTimes = 3;


    @Resource(name = "annotationService")
    private AnnotationService annotationService;
    @Autowired
    private TaskOrderDao taskOrderDao;
    @Autowired
    private ImgUrlDao imgUrlDao;
    @Autowired
    private TaskDao taskDao;

    @Override
    public void evaluateAnnotation(long taskOrderId) {
        TaskOrder taskOrder = new TaskOrder();
        long taskId = taskOrder.getTaskId();
        List<TaskOrder> taskOrders = taskOrderDao.getAllTaskOrderOfATask(taskId);
        int num = taskOrders.size();
        if(num<minAutoEvaluateNum){
            throw new ResultException("标注完后人数过少，自动评审结果不具有参考性，请手动评分！",12333);
        }else {
            int pictureNum = imgUrlDao.getImageNum(taskId);
            List<Double> points = new ArrayList<>();
            for (int i=1;i<=pictureNum;i++){
                HashMap<String,Integer> tags = annotationService.getTags(taskId,i);
                List<String> words = new ArrayList<>();
                String str[] = annotationService.getAnnotation(taskOrderId,i).getSentence().split("\\s+|\\.|,|\\(|\\)|;|:|\"|\\?|!");
                for(String ss:str){
                    if(!ss.equals("")){
                        words.add(ss);
                    }
                }
                HashMap<String,Integer> thisTag = new AnnotationServiceImpl().getTag(words);
                //todo 计算符合度,分为数据量大和数据量小两种
                if(num < bigNumStandard){
                    //按次数的多少算权重
                    tags.entrySet().removeIf(item -> item.getValue() <= smallNumTimes);
                    points.add(getFitness(tags,thisTag));
                }else if(num>=bigNumStandard){
                    tags.entrySet().removeIf(item -> item.getValue() <= bigNumTimes);
                    points.add(getFitness(tags,thisTag));
                }
            }
            //计算得到最终的分数
            double result = 0;
            for(double point:points){
                result+=point;
            }
            result/=pictureNum;
            if(num<bigNumStandard){

            }
        }
    }

    @Override
    public void evaluateAnnotation(){
        //选出人满的进行中的任务
        List<Task> tasks = taskDao.getAllOngoingTask();
        for(Task task:tasks){
            if(taskOrderDao.getAcceptNum(task.getTaskId())<task.getMaxParticipator()){
                tasks.remove(task);
            }
        }

        for(Task task:tasks){
            // todo
            List<Long> taskOrderIds = taskOrderDao.getAllTaskOrderIdOfATask(task.getTaskId());
            for(long id:taskOrderIds){
                taskOrderDao.setQuality(Math.random()*20+80,id);
            }
            //
            int num = task.getImgUrls().size();
            switch (task.getAnnotationType()){
                case option1:
                    break;
                case option2:
                    break;
                case option3:
                    break;
                case option4:
                    break;
            }
        }
    }

    private void evaluateText(long taskId,int num){
        List<TaskOrder> taskOrders = taskOrderDao.getAllTaskOrderOfATask(taskId);

        //去除系统认为是欺骗的标注信息
        for(TaskOrder taskOrder:taskOrders){
            if(cheatFindOfText(taskOrder.getTaskOrderId())){
                taskOrderDao.setQuality(10,taskOrder.getTaskOrderId());
                taskOrders.remove(taskOrder);
            }
        }

        //任务可用数据过少，只能设置一个保守的80分，无法计算质量
        if(taskOrders.size()<3){
            for(TaskOrder taskOrder:taskOrders){
                taskOrderDao.setQuality(80,taskOrder.getTaskOrderId());
            }
        }

        //按完成度由小到大排序
        taskOrders.sort(Comparator.comparingInt(TaskOrder::getFinishedPics));
        //得到最大能进行计算的图片数
        int maxNum = taskOrders.get(taskOrders.size()-1).getFinishedPics();
        if(taskOrders.get(taskOrders.size()-2).getFinishedPics()<maxNum){
            maxNum = taskOrders.get(taskOrders.size()-2).getFinishedPics();
            if(taskOrders.get(taskOrders.size()-3).getFinishedPics()<maxNum){
                maxNum = taskOrders.get(taskOrders.size()-3).getFinishedPics();
            }
        }
        HashMap<Long,List<Double>> points = new HashMap<>();
        for(int i=1;i<=maxNum;i++){
            List<Long> tmp = new ArrayList<>();
            for(int j=taskOrders.size()-1;taskOrders.get(j).getFinishedPics()>=i;i--){
                tmp.add(taskOrders.get(j).getTaskOrderId());
            }
            HashMap<Long,Double> thisPoint = getTextPoint(tmp,i);
            Set<Long> keys = thisPoint.keySet();
            for(long id:keys){
                points.get(id).add(thisPoint.get(id));;
            }
        }

        if(maxNum<taskOrders.get(taskOrders.size()-1).getFinishedPics()){
            for(int i=0;i<taskOrders.get(taskOrders.size()-1).getFinishedPics()-maxNum;i++){
                points.get(taskOrders.get(taskOrders.size()-1).getTaskOrderId()).add((double)80);
            }
        }

        if(maxNum<taskOrders.get(taskOrders.size()-2).getFinishedPics()){
            for(int i=0;i<taskOrders.get(taskOrders.size()-2).getFinishedPics()-maxNum;i++){
                points.get(taskOrders.get(taskOrders.size()-2).getTaskOrderId()).add((double)80);
            }
        }

        Set<Long> keys = points.keySet();
        for(long id:keys){
            List<Double> pts = points.get(id);
            double tmp = 0;
            for(double d:pts){
                tmp+=d;
            }
            taskOrderDao.setQuality(tmp/pts.size(),id);
        }
    }

    private void evaluateCoordinate(long taskId){

    }

    private HashMap<Long,Double> getTextPoint(List<Long> taskOrderIds,int pictureNum){
        HashMap<Long,Double> result = new HashMap<>();
        HashMap<Long,String> words = new HashMap<>();
        for(long id:taskOrderIds){
            Annotation annotation = annotationService.getAnnotation(id,pictureNum);
            words.put(id,annotation.getWords());
            result.put(id,(double)new Random().nextInt(100));
        }
        //tf-idf算法
        return result;
    }


    //粗略计算标注信息的符合程度
    private double getFitness(HashMap<String,Integer> tags,HashMap<String,Integer> thisTag){
        double totalNum = 0;
        for(int num:tags.values()){
            totalNum += num;
        }
        Set<String> keys = thisTag.keySet();
        double points = 0;
        for(Map.Entry<String,Integer> entry:tags.entrySet()){
            if(keys.contains(entry.getKey())){
                points+=(entry.getValue()/totalNum);
            }
        }
        return points*100;
    }

    private boolean cheatFindOfText(long taskOrderId){
        List<Annotation> annotations = annotationService.getAnnotations(taskOrderId);
        List<String> texts = new ArrayList<>();
        List<String> words = new ArrayList<>();
        for(Annotation annotation:annotations){
            texts.add(annotation.getSentence());
            words.add(annotation.getWords());
        }
        int difference = texts.size()-new HashSet<>(texts).size();
        if(difference>1){
            return true;
        }
        int count = 0;
        for(int i=0;i<texts.size();i++){
            if((texts.get(i).split("\\s+|\\.|,|\\(|\\)|;|:|\"|\\?|!").length-words.get(i).split(",").length)<2){
                count++;
            }
        }
        return count / texts.size() >= 0.3;
    }

    private boolean cheatFindOfClassified(long taskOrderId,int num){
        List<Annotation> annotations = annotationService.getAnnotations(taskOrderId);
        List<String> words = new ArrayList<>();
        for(Annotation annotation:annotations){
            words.add(annotation.getWords());
        }
        int count =0;
        for(String s:words){
            if(new HashSet<String>(Arrays.asList(s.split(","))).size()==1){
                count++;
            }
        }
        return count/words.size()>=0.3;
    }


    @Override
    public void evaluateTaskQuality(long taskId) {
        System.loadLibrary("opencv_java341");
        Task task = taskDao.getTask(taskId);
        List<String> urls = task.getImgUrls();
        List<Double> points = new ArrayList<>();
        //为了效率，对于图片张数过多的任务，采取抽样的方法，最多100张
        if(urls.size()<=100) {
            for (String url : urls) {
                points.add(getPictureQuality(url));
            }
        }else{
            int random[] = getRandoms(0,urls.size()-1,100);
            for(int i=0;i<100;i++){
                if (random != null) {
                    points.add(getPictureQuality(urls.get(random[i])));
                }
            }
        }
        //取分数的平均值，当图片张数少时，差的图片对分数的影响大，当图片数量大时，影响就小
        double result = 0;
        for(double point:points){
            result+=point;
        }
        result/=urls.size();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        taskDao.setTaskQuality(taskId,Double.parseDouble(df.format(result)));
    }



    //##############################################################
    private double getPictureQuality(String imgUrl){
        System.loadLibrary("opencv_java341");
        try {
            URL url = new URL(imgUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //把输入流转换成mat对象
            BufferedImage bufferedImage = ImageIO.read(inStream);
            Mat mat = img2Mat(bufferedImage);
            //估算图片质量
            return 100 - new BRISQUE().brisquescore(mat);
        } catch (IOException e) {
            return -1;
        }
    }

    //计算信息熵，用灰度图
    private double getEntropy(Mat mat){
        double[] temp = new double[256];
        // 计算每个像素的累积值
        int row = mat.rows();
        int col = mat.cols();
        for (int r = 0; r<row; r++)
        {
            for (int c = 0; c<col; c++)
            {
                int value = (int)mat.get(r,c)[0];
                if(value<0){
                    value+=256;
                }
                temp[value]++;
            }
        }
        int size = row * col;
        for (int i = 0; i<256; i++)
        {
            temp[i] = temp[i] / size;
        }
        double result = 0;
        for (int i = 0; i<256; i++)
        {
            if (temp[i] == 0.0)
                result = result;
            else
                result = result - temp[i] * (log(temp[i]) / log(2.0));
        }

        return result;
    }

    //计算平均梯度，使用灰度图
    private double getMeanGradient(Mat grayImg) {
        if (grayImg.channels() != 1) {
            System.out.print("avgGradient 参数错误，必须输入单通道图！");
            return 0.0;
        }
        // 原灰度图转换成浮点型数据类型
        Mat src = new Mat();
        grayImg.convertTo(src, CvType.CV_64FC1);

        double temp = 0.0f;
        // 由于求一阶差分的边界问题，这里行列都要-1
        int rows = src.rows() - 1;
        int cols = src.cols() - 1;

        // 根据公式计算平均梯度
        for (int r = 0; r < rows; r++)
        {
            for (int c = 0; c < cols; c++)
            {
                // 离散的delta就是相邻的离散点的差值
                double dx = src.get(r, c + 1)[0] - src.get(r, c)[0];
                double dy = src.get(r + 1, c)[0] - src.get(r, c)[0];
                double ds = sqrt((dx*dx + dy * dy) / 2);
                temp += ds;
            }
        }
        return temp / (rows*cols);
    }

    //计算灰度图的均值和方差
    private double[] getMeanStd(Mat mat){
        if (mat.channels() != 1) {
            System.out.print("mean_std 参数错误，必须输入单通道图！");
            return null;
        }
        MatOfDouble mat_mean = new MatOfDouble(), mat_stddev = new MatOfDouble();
        meanStdDev(mat, mat_mean, mat_stddev);
        double mean = mat_mean.get(0, 0)[0];
        double std = mat_stddev.get(0, 0)[0];
        double snr = mean/std;
        double result[] = new double[3];
        result[0] = mean;
        result[1] = std;
        result[2] = snr;
        return result;
    }

    private Mat img2Mat(BufferedImage in) {
        Mat out;
        byte[] data;
        int r, g, b;

        if (in.getType() == BufferedImage.TYPE_INT_RGB) {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i]) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
            }
        } else {
            out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC1);
            data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
            for (int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i]) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b));
            }
        }
        out.put(0, 0, data);
        return out;
    }

//    public static void main(String s[]){
//        System.out.println(System.getProperty("java.library.path"));
//        OpencvLibrary.loadLibraries();
//        Mat mat;
//        try {
//            //使用java2D读取图像
//            String filePath = "C:\\Users\\18333\\Desktop\\picTest\\5.bmp";
//            BufferedImage image =ImageIO.read(new File(filePath));
//            mat = new EvaluateServiceImpl().img2Mat(image);
//            System.out.println(new BRISQUE().brisquescore(mat));
//        } catch (Exception e) {
//            System.out.println("读取图像出现异常!");
//            e.printStackTrace();
//        }
//    }

    private int[] getRandoms(int min, int max, int count){
        int[] randoms = new int[count];
        List<Integer> listRandom = new ArrayList<Integer>();

        if( count > ( max - min + 1 )){
            return null;
        }
        // 将所有的可能出现的数字放进候选list
        for(int i = min; i <= max; i++){
            listRandom.add(i);
        }
        // 从候选list中取出放入数组，已经被选中的就从这个list中移除
        for(int i = 0; i < count; i++){
            int index = getRandom(0, listRandom.size()-1);
            randoms[i] = listRandom.get(index);
            listRandom.remove(index);
        }

        return randoms;
    }

    private int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt( max - min + 1 ) + min;
    }
}
