package com.sec.server.Dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    private TestDao testDao;
    @org.junit.Test
    public void t(){
        //int num=testDao.getTest(1);
        testDao.insert(9,12);
        //System.out.println(num);
    }

}
