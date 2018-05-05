package com.sec.server;

import com.alibaba.fastjson.JSON;
import com.sec.server.model.TaskOrderModel;
import com.sec.server.model.UserModel;
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
public class TaskOrderModuleTest extends TestCase {
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup(){
        //初始化MockMvc对象
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 测试行为：获取一个任务订单
     * 预期结果：返回任务订单信息
     */
    @Test
    public void testGetTaskOrder() throws Exception{
        mockMvc.perform(
                post("/taskOrder/orderInfo").param("taskOrderId",String.valueOf(0)).param("userId",String.valueOf(0))
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：创建一个任务订单
     * 预期结果：返回操作成功信息
     */
    @Test
    public void testCreateTaskOrder() throws Exception{
        TaskOrderModel taskOrderModel = new TaskOrderModel();
        String request = JSON.toJSONString(taskOrderModel);
        mockMvc.perform(
                post("/taskOrder/createTaskOrder").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：更新一个任务订单
     * 预期结果：返回操作成功信息
     */
    @Test
    public void testUpdateTaskOrder() throws Exception{
        TaskOrderModel taskOrderModel = new TaskOrderModel();
        String request = JSON.toJSONString(taskOrderModel);
        mockMvc.perform(
                post("/taskOrder/update").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：删除一个任务订单
     * 预期结果：返回操作成功信息
     */
    @Test
    public void testDeleteTaskOrder() throws Exception{
        mockMvc.perform(
                post("/taskOrder/delete").param("taskOrderId",String.valueOf(0)).param("userId",String.valueOf(0))
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：获取用户所有提交的任务订单
     * 预期结果：返回所有提交的任务订单
     */
    @Test
    public void testGetAllSubmitted() throws Exception{
        UserModel userModel = new UserModel();
        String request = JSON.toJSONString(userModel);
        mockMvc.perform(
                post("/taskOrder/getAllSubmited").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：获取用户所有的任务订单
     * 预期结果：返回所有的任务订单
     */
    @Test
    public void testGetAllTaskOrder() throws Exception{
        UserModel userModel = new UserModel();
        String request = JSON.toJSONString(userModel);
        mockMvc.perform(
                post("/taskOrder/getAll").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }
}
