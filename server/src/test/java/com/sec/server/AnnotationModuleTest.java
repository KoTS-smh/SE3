package com.sec.server;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.AnnotationInfo;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnnotationModuleTest extends TestCase {
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup(){
        //初始化MockMvc对象
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 测试行为：获得一张图片所有的标注信息
     * 预期结果：返回所有的标注信息
     */
    @Test
    public void testGetAllAnnotation() throws Exception{
        mockMvc.perform(
                post("/annotation/getAll").param("annotationId",String.valueOf(0))
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：更改一个图片的标注信息
     * 预期结果:返回操作成功信息
     */
    @Test
    public void testUpdateAnnotation() throws Exception{
        AnnotationInfo annotationInfo = new AnnotationInfo();
        String request = JSON.toJSONString(annotationInfo);
        mockMvc.perform(
                post("/annotation/update").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }
}
