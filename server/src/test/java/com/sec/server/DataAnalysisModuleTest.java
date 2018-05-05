package com.sec.server;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.User;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DataAnalysisModuleTest extends TestCase {

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before()
    public void setup(){
        //初始化MockMvc对象
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 测试行为：众包发起者获取任务信息
     * 预期结果：返回正确信息
     */
    @Test
    public void testGetTaskMessage()throws Exception{
        String responseString = mockMvc.perform(
                post("/getTaskMessage").param("taskId",String.valueOf(0))
        ).andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
    }

    /**
     * 测试行为：系统管理员获取信息
     * 预期结果：返回正确信息
     */
    @Test
    public void testGetSystemMessage()throws Exception{
        mockMvc.perform(
                post("/getSystemMessage")
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    /**
     * 测试行为：用户获取相关统计信息
     * 预期结果：返回正确用户统计信息
     */
    @Test
    public void testGetAnalysisMessage() throws Exception{
        User user = new User();
        user.setUsername("wrs000");
        user.setPassword("0");
        String requestJSON = JSON.toJSONString(user);
        mockMvc.perform(
                post("/personalData").contentType(MediaType.APPLICATION_JSON).content(requestJSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    /**
     * 测试行为：获取一个任务中的tag信息
     * 预期结果：返回正确信息
     */
    @Test
    public void testGetTagMessage() throws Exception{
        mockMvc.perform(
                post("/annotation/tags").param("taskId",String.valueOf(0))
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
}
