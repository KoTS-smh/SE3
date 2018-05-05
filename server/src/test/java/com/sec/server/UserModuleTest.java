package com.sec.server;

import com.alibaba.fastjson.JSON;
import com.sec.server.domain.User;
import com.sec.server.domain.UserInfo;
import com.sec.server.enums.Education;
import com.sec.server.enums.Sex;
import com.sec.server.enums.UserLevel;
import junit.framework.TestCase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserModuleTest extends TestCase {

    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Before()
    public void setup(){
        //初始化MockMvc对象
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * 测试行为：用户登录
     * 预期结果：返回正确信息并更改存储信息
     */
    @Test
    public void testCreateCorrectUser() throws Exception{
        //设置对象参数
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("wrs000");
        userInfo.setPassword("0");
        //转成json数据
        String requestjson = com.alibaba.fastjson.JSONObject.toJSONString(userInfo);
        String responseString = mockMvc.perform(
                //请求url
                post("/user/login")
                        //参数类型
                        .contentType(MediaType.APPLICATION_JSON)
                        //参数
                        .content(requestjson)
        ).andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        System.out.print(responseString);
    }

    /**
     * 测试行为：用户注册
     * 预期结果：返回正确信息并更改存储信息
     */
    @Test
    public void testRegister() throws Exception {
        //设置对象参数
        User userInfo = new User();
        userInfo.setUsername("wrs333");
        userInfo.setPassword("0");
        userInfo.setTel_phone("110");
        //转成json数据
        String requestJson = com.alibaba.fastjson.JSONObject.toJSONString(userInfo);
        String responseString = mockMvc.perform(
                //请求url
                post("/user/register")
                        //参数类型
                        .contentType(MediaType.APPLICATION_JSON)
                        //参数
                        .content(requestJson)
        ).andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.print(responseString);
    }

    /**
     * 测试行为：更新用户信息
     * 预期结果：返回正确信息并更改存储信息
     */
    @Test
    public void testUpdataUser()throws Exception{
        //设置对象参数
        User user = new User();
        user.setUserId(3);
        user.setTel_phone("111");
        user.setEmail("www");
        user.setUsername("wrs333");
        user.setPassword("0");
        user.setSex(Sex.Woman);
        user.setEducation(Education.undergraduate);
        user.setUserLevel(UserLevel.LEVEL1);
        //转成json数据
        String requestJson = JSON.toJSONString(user);
        String requestString = mockMvc.perform(
                //请求url
                post("/user/update")
                        //参数类型
                        .contentType(MediaType.APPLICATION_JSON)
                        //参数
                        .content(requestJson)
        ).andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.print(requestJson);
    }

    /**
     * 测试行为：删除用户信息
     * 预期结果：返回正确信息并更改存储信息
     */
    @Test
    public void testDeleteUser()throws Exception{
        long userId = 3;
        mockMvc.perform(
                //请求url
                post("/user/delete").param("userId",String.valueOf(3))
        ).andExpect(status().isOk());
    }

    /**
     * 测试行为：获取一个用户的信息
     * 预期结果：返回正确信息
     */
    @Test
    public void testGetUser()throws Exception{
        String responseString = mockMvc.perform(
                //请求url
                post("/user/getUser").param("userId",String.valueOf(2))
        ).andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.print(responseString);
    }

}
