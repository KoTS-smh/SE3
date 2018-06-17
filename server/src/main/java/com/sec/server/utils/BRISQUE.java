package com.sec.server.utils;

import org.apache.commons.io.FileUtils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import static org.opencv.core.Core.*;

public class BRISQUE {
    public double brisquescore(Mat image){
        if(image.empty()){
            System.out.print("image read file!");
            return -1;
        }
        Mat gray=new Mat();
        if(image.channels()==3){
            Imgproc.cvtColor(image,gray,Imgproc.COLOR_RGB2GRAY);
        }else{
            gray = image.clone();
        }
        gray.convertTo(gray,CvType.CV_64FC1);//char2double\
        Vector<Double> feat=new Vector<>();
        feat = brisque_feature(gray,feat);
        //cout<<"feat computed"<<endl;
        /*---------------------------------------------------------------------*/
        //Quality Score Computation
        /*---------------------------------------------------------------------*/

	    String datafile="server/src/main/java/com/sec/server/utils/svmData";
	    String resultFile = "server/src/main/java/com/sec/server/utils/svmResult";
	    String allmodel = "server/src/main/java/com/sec/server/utils/allmodel";
	    String allrange = "server/src/main/java/com/sec/server/utils/allrange";
	    String svmScaleData = "server/src/main/java/com/sec/server/utils/svmScaleData";
        double score=0;
        try {
            File file = new File(datafile);
            FileWriter fr = new FileWriter(file);
            StringBuilder stringBuilder =new StringBuilder();
            stringBuilder.append("1 ");
            for(int i=0;i<feat.size();i++){
                stringBuilder.append(i+1).append(":").append(feat.get(i)).append(" ");
            }
            fr.write(stringBuilder.toString());
            fr.flush();
            fr.close();
            String scaleData = svm_scale.main(new String[]{"-r",allrange,datafile});
            fr = new FileWriter(new File(svmScaleData));
            fr.write(scaleData);
            fr.flush();
            fr.close();
            String[] result_files = {"-b","1",svmScaleData,allmodel,resultFile};
            svm_predict.main(result_files);
            score = Double.parseDouble(FileUtils.readFileToString(new File(resultFile)));
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return score;
    }

    private Vector<Double> brisque_feature(Mat imdist, Vector<Double> feat)
    {
        if (imdist.empty())
        {
            System.out.print("image read file!");
        }
        //------------------------------------------------
        //Feature Computation
        //-------------------------------------------------

        int scalenum = 2;
        feat.clear();
        Mat imdisthalf=Mat.zeros(imdist.rows()/2,imdist.cols()/2,imdist.type());
        if (2==scalenum)
        {
            feat = brisque_process(imdist,feat);
            Imgproc.resize(imdist,imdisthalf,new Size(imdist.cols()/2,imdist.rows()/2));
            //cout<<imdisthalf.at<double>(0,0)<<endl;
            //for (unsigned int i=0;i<feat.size();i++)
            //{
            //	cout<<feat[i]<<endl;
            //}
            feat = brisque_process(imdisthalf,feat);
        }
        return feat;
    }

    private Vector<Double> brisque_process(Mat imdist, Vector<Double> feat)
    {
        if (imdist.empty())
        {
            System.out.print("image not read.");
        }
        Mat mu = new Mat(),mu_sq=new Mat();
        Mat sigma=Mat.zeros(imdist.rows(),imdist.cols(),imdist.type());
        Mat imgdouble=new Mat();
        Mat imdist_mu=new Mat();
        Mat avoidzero;
        double alpha=0,overallstd=0;
        Mat structdis = new Mat();

        //Ptr<FilterEngine> f= createGaussianFilter( CV_64FC1, Size(3,3), 1, 1);
        //Mat tmp1=getGaussianKernel(3,1);
        //Ptr<FilterEngine> f= createSeparableLinearFilter(CV_64FC1,CV_64FC1, tmp1, tmp1,Point(-1,-1), 0);
        //Mat tmp2=Mat::ones(3,3,CV_64FC1);
        //f->apply(tmp2,tmp2);
        //cout<<tmp2<<endl;
        Imgproc.GaussianBlur(imdist,mu, new Size(7,7),7.f/6,7.f/6,0);
        //BORDER_CONSTANT=0
        //cout<<imdist.at<double>(0,0)<<endl;
        //cout<<mu.at<double>(0,0)<<endl;
        //cout<<mu.at<double>(0,1)<<endl;
        multiply(mu,mu,mu_sq);
        //cout<<setprecision(10)<<mu_sq.at<double>(0,0)<<endl;
        //Mat imdist1=imdist.clone();
        //cout<<imdist1.at<double>(0,0)<<endl;
        //Mat mu1=mu.clone();
        //Mat imdist_mu1;
        //   subtract(imdist1,mu1,imdist_mu1);
        //imdist_mu1.mul(imdist_mu1);
        //GaussianBlur(imdist_mu1,imdist_mu1,Size(7,7),7.f/6,7.f/6,0);
        //cout<<imdist_mu1.at<double>(0,0)<<endl;
        multiply(imdist,imdist,imgdouble);
        Imgproc.GaussianBlur(imgdouble,imgdouble,new Size(7,7),7.f/6,7.f/6,0);
        //cout<<setprecision(10)<<imgdouble.at<double>(0,0)<<endl;

        for (int i=0;i<imgdouble.rows();i++)
        {
            for (int j=0;j<imgdouble.cols();j++)
            {
                double data1 = imgdouble.get(i,j)[0];
                double data2 = mu_sq.get(i,j)[0];
                sigma.put(i,j,Math.sqrt(Math.abs(data1-data2)));
                //sigma.at<double>(i,j)=sqrt(abs(imgdouble.at<double>(i,j)-mu_sq.at<double>(i,j)));
            }
        }
        subtract(imdist,mu,imdist_mu);
        avoidzero=Mat.ones(sigma.rows(),sigma.cols(),sigma.type());
        add(sigma,avoidzero,sigma);
        //avoidzero=Mat::ones(imdist_mu1.rows,imdist_mu1.cols,imdist_mu1.type());
        //add(imdist_mu1,avoidzero,imdist_mu1);
        //divide(imdist_mu,imdist_mu1,structdis);

        divide(imdist_mu,sigma,structdis);//.......................................equation 1
        //imshow("str",structdis);
        //waitKey(1000);
        //cout<<setprecision(10)<<structdis.at<double>(0,0)<<endl;
        double d[] = estimateggdparam(structdis,alpha,overallstd);
        feat.add(d[0]);
        feat.add(d[1]*d[1]);
        Mat shifted_structdis;
        Mat pair;
        double constvalue,meanparam,leftstd=0,rightstd=0;
        int[][] shifts={{0,1},{1,0},{1,1},{-1,1}};

        for(int  itr_shift =0;itr_shift <4;itr_shift ++)
        {

            shifted_structdis=circshift(structdis,shifts[itr_shift][0],shifts[itr_shift][1]);
            //cout<<setprecision(10)<<shifted_structdis.at<double>(0,0)<<endl;
            pair=structdis.mul(shifted_structdis);//.............................................................................equation 7,8,9,10
            //cout<<pair.at<double>(0,0)<<endl;
            double dd[] = estimateaggdparam(pair,
                    alpha,
                    leftstd,//left sigma
                    rightstd//right sigma
            );
            alpha = dd[0];leftstd=dd[1];rightstd=dd[2];
            constvalue=(Math.sqrt(Gamma(1/alpha))/Math.sqrt(Gamma(3/alpha)));
            meanparam=(rightstd-leftstd)*(Gamma(2/alpha)/Gamma(1/alpha))*constvalue;//.................equation 15
            feat.add(alpha);
            feat.add(meanparam);
            feat.add(leftstd*leftstd);
            feat.add(rightstd*rightstd);
        }
        return feat;
    }

    private double[] estimateggdparam(Mat vec, double gamparam, double sigma)
    {

        Mat vec2=vec.clone();
        //cout<<vec2.at<double>(0,0)<<endl;
        Scalar sigma_sq=mean(vec2.mul(vec2));
        sigma=Math.sqrt(sigma_sq.val[0]);
        Mat tmp =new Mat();
        absdiff(vec,Scalar.all(0),tmp);
        Scalar E=mean(tmp);
        double rho=sigma_sq.val[0]/(E.val[0]*E.val[0]);
        Vector<Double> gam = new Vector<>();
        Vector<Double> r_gam=new Vector<>();
        Vector<Double> rho_r_gam=new Vector<>();
        int number=(int)((10-0.2f)/0.001f)+1;
        gam.clear();
        r_gam.clear();
        rho_r_gam.clear();
        gam.setSize(number);
        r_gam.setSize(number);
        rho_r_gam.setSize(number);
        for(int i=0;i<number;i++)
        {
            if (0==i)
            {
                //gam[i]=0.2;
                gam.set(i,0.2);
            }else
            {
                gam.set(i,gam.get(i-1)+0.001f);
            }
            r_gam.set (i,Gamma(1.f/gam.get(i))*Gamma(3.f/gam.get(i))/(Gamma(2./gam.get(i))*Gamma(2./gam.get(i))));
            rho_r_gam.set(i,Math.abs(rho-r_gam.get(i)));
        }
        //find min and pos
        //min_element(dv.begin(),dv.end()) return vector<double>::iterator, as location of point
        int pos = rho_r_gam.indexOf(Collections.min(rho_r_gam));
        //gamma
        gamparam=gam.get(pos);
        double[] result = new double[2];
        result[0]=gamparam;
        result[1]=sigma;
        return result;
    }

    private double[] estimateaggdparam(Mat vec, double alpha, double leftstd, double rightstd)
    {
        Vector<Double> left=new Vector<>();
        Vector<Double> right=new Vector<>();
        left.clear();
        right.clear();
        for (int i=0;i<vec.rows();i++)
        {
            for (int j=0;j<vec.cols();j++)
            {
                double data1 = vec.get(i,j)[0];
                if (/*vec.at<double>(i,j)<0*/data1<0)
                {
                    left.add(/*vec.at<double>(i,j)*/data1);
                }
                else if (/*vec.at<double>(i,j)>0*/data1>0)
                {
                    right.add(/*vec.at<double>(i,j)*/data1);
                }
            }
        }
        for (int i=0;i<left.size();i++)
        {
            left.set(i,left.get(i)*left.get(i));
        }
        for (int i=0;i<right.size();i++)
        {
            right.set(i,right.get(i)*right.get(i));
        }
        double leftsum=0.f;
        for (Double aLeft : left) {
            leftsum += aLeft;
        }
        double rightsum=0.f;
        for (Double aRight : right) {
            rightsum += aRight;
        }
        leftstd=Math.sqrt(leftsum/left.size());//mean
        rightstd =Math.sqrt(rightsum/right.size());//mean
        double gammahat = leftstd/rightstd;
        Mat vec2=new Mat();
        multiply(vec,vec,vec2);
        Mat tmp = new Mat();
        absdiff(vec,Scalar.all(0),tmp);
        Scalar tmp1=mean(tmp);
        Scalar tmp2=mean(vec2);
        double rhat=tmp1.val[0]*tmp1.val[0]/tmp2.val[0];

        double rhatnorm=(rhat*(gammahat*gammahat*gammahat +1)*(gammahat+1))/((gammahat*gammahat +1)*(gammahat*gammahat +1));
        Vector<Double> gam=new Vector<>();
        Vector<Double> r_gam=new Vector<>();
        Vector<Double> r_gam_rha=new Vector<>();
        int number=(int)((10-0.2f)/0.001f)+1;
        gam.setSize(number);
        r_gam.setSize(number);
        r_gam_rha.setSize(number);

        for(int i=0;i<number;i++)
        {
            if (0==i)
            {
                gam.set(i,0.2);
            }
            else
            {
                gam.set(i,gam.get(i-1)+0.001f);
            }

            r_gam.set(i,(Gamma(2.f/gam.get(i))*Gamma(2.f/gam.get(i)))/(Gamma(1./gam.get(i))*Gamma(3./gam.get(i))));
            r_gam_rha.set(i,(r_gam.get(i)-rhatnorm)*(r_gam.get(i)-rhatnorm));
        }
        //find min and pos
        int pos = r_gam_rha.indexOf(Collections.min(r_gam_rha));
        alpha = gam.get(pos);

        double[] result = new double[3];
        result[0]=alpha;
        result[1]=leftstd;
        result[2]=rightstd;
        return result;
    }

    private Mat circshift(Mat structdis, int a, int b)
    {
	/*
	A = [ 1 2 3;
	         4 5 6;
			 7 8 9];
	B=circshift(A,[0,1])
		B =
		3     1     2
		6     4     5
		9     7     8
		K>> B=circshift(A,[1,0])
		B =
		7     8     9
		1     2     3
		4     5     6
		K>> B=circshift(A,[-1,0])
		B =
		4     5     6
		7     8     9
		1     2     3
	*/

        Mat shiftx=Mat.zeros(structdis.rows(),structdis.cols(),structdis.type());
        if (0==a)
        {//unchanged
            shiftx=structdis.clone();
        }
        else if(1==a)
        {//
            for (int i=0;i<structdis.rows()-1;i++)
            {
                for (int j=0;j<structdis.cols();j++)
                {
                    shiftx.put(i+1,j,structdis.get(i,j));
                }
            }
            for (int j=0;j<structdis.cols();j++){
                shiftx.put(0,j,structdis.get(structdis.rows()-1,j));
            }
        }
        else if (-1==a)
        {
            for (int i=0;i<structdis.rows()-1;i++)
            {
                for (int j=0;j<structdis.cols();j++)
                {
                    shiftx.put(i,j,structdis.get(i+1,j));
                }
            }
            for (int j=0;j<structdis.cols();j++){
                shiftx.put(structdis.rows()-1,j,structdis.get(0,j));
            }
        }
	/*
	K>>  A = [ 1 2 3;4 5 6; 7 8 9];
	K>>  B=circshift(A,[0,1])
		B =
		3     1     2
		6     4     5
		9     7     8
		*/
        Mat shifty=Mat.zeros(shiftx.rows(),shiftx.cols(),shiftx.type());
        if (0==b)
        {
            shifty=shiftx.clone();
        }
        else if (1==b)
        {
            for (int i=0;i<shiftx.rows();i++)
            {
                for (int j=0;j<shiftx.cols()-1;j++)
                {
                    shifty.put(i,j+1,shiftx.get(i,j));
                }
            }
            for (int i=0;i<shiftx.rows();i++){
                shifty.put(i,0,shiftx.get(i,shiftx.cols()-1));
            }
        }
        return shifty;
    }


    private double Gamma(double x)
    {//x>0
        if( x > 2 && x<= 3 )
        {
             double c0 =  0.0000677106;
             double c1 = -0.0003442342;
             double c2 =  0.0015397681;
             double c3 = -0.0024467480;
             double c4 =  0.0109736958;
             double c5 = -0.0002109075;
             double c6 =  0.0742379071;
             double c7 =  0.0815782188;
             double c8 =  0.4118402518;
             double c9 =  0.4227843370;
             double c10 = 1.0000000000;
            double temp = 0;
            temp = temp + c0*Math.pow( x-2.0, 10.0) + c1* Math.pow( x-2.0, 9.0);
            temp = temp + c2*Math.pow( x-2.0, 8.0) + c3*Math.pow( x-2.0 , 7.0);
            temp = temp + c4*Math.pow( x-2.0, 6.0) + c5*Math.pow( x-2.0, 5.0 );
            temp = temp + c6*Math.pow( x-2.0, 4.0 ) + c7*Math.pow( x-2.0, 3.0 );
            temp = temp + c8*Math.pow( x-2.0, 2.0 ) + c9*( x-2.0) + c10;
            return temp;
        }
        else if( x>0 && x<=1 )
        {
            return Gamma( x+2 )/(x*(x+1) );
        }
        else if( x > 1 && x<=2 )
        {
            return Gamma( x+1 )/x;
        }
        else if( x > 3 )
        {
            int i = 1;
            double temp = 1;
            while(!((x - i) > 2 && (x - i) <= 3))
            {
                temp = (x-i) * temp;
                i++;
            }
            temp = temp*(x-i);
            return temp*Gamma( x-i);
        }
        else
        {
            return 0;
        }
    }

}
