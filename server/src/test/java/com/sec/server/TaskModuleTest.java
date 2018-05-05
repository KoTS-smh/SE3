package com.sec.server;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.Task;
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

import java.util.jar.JarEntry;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskModuleTest extends TestCase {
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup(){
        //初始化MockMvc对象
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 测试行为：用户获取所有已经提交的任务
     * 预期结果：返回任务信息
     */
    @Test
    public void testGetAllPostTask() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setUserId(0);
        String requestJSON = JSON.toJSONString(userModel);
        mockMvc.perform(
                post("/task/getAllPost").contentType(MediaType.APPLICATION_JSON).content(requestJSON)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：用户获取所有已完成的任务
     * 预期结果：返回任务信息
     */
    @Test
    public void testGetAllFinishedTask() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setUserId(0);
        String requestJSON = JSON.toJSONString(userModel);
        mockMvc.perform(
                post("/task/getAllFinished").contentType(MediaType.APPLICATION_JSON).content(requestJSON)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：用户获取所有未完成的任务
     * 预期结果：返回任务信息
     */
    @Test
    public void testGetAllUnfinishedTask() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setUserId(0);
        String requestJSON = JSON.toJSONString(userModel);
        mockMvc.perform(
                post("/task/getAllunFinished").contentType(MediaType.APPLICATION_JSON).content(requestJSON)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：用户创建一个新的任务
     * 预期结果：返回操作成功信息
     */
    @Test
    public void testCreateNewTask() throws Exception{
        Task task = new Task();
        String request = task.toString();
        mockMvc.perform(
                post("/task/create").content(request)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：用户更新一个任务
     * 预期结果：返回操作成功信息
     */
    @Test
    public void testUpdateTask() throws Exception{
        Task task = new Task();
        String request = JSON.toJSONString(task);
        mockMvc.perform(
                post("/task/update").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：用户删除一个任务
     * 预期结果：返回操作成功信息
     */
    @Test
    public void testDeleteTask() throws Exception{
        mockMvc.perform(
                post("/task/delete").param("taskId",String.valueOf(0))
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：用户获取一个任务
     * 预期结果：返回任务信息
     */
    @Test
    public void testGetTask() throws Exception{
        Task task = new Task();
        String request = JSON.toJSONString(task);
        mockMvc.perform(
                post("/task/taskInfo").contentType(MediaType.APPLICATION_JSON).content(request)
        ).andExpect(status().isOk());
    }
}
