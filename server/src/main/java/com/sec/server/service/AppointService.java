package com.sec.server.service;

import com.sec.server.enums.AnnotationType;

import java.util.List;

public interface AppointService {

    /**
     * 预约任务
     * @param taskId 任务Id
     * @param userId 工人Id
     */
    void appointTask(long taskId,long userId);

    /**
     * 取消预约
     * @param taskId 任务Id
     * @param userId 工人Id
     */
    void cancelAppoint(long taskId,long userId);

    /**
     * 预约期结束任务正式开始
     * @param taskId 任务Id
     */
    void endAppointment(long taskId);

    /**
     * 获取任务所有预约的工人
     * @param taskId 任务Id
     * @return 工人列表 list
     */
    List<Long> getAppointUser(long taskId);

    /**
     * 获取工人预约的所有任务
     * @param userId 工人Id
     * @return 任务列表 list
     */
    List<Long> getAppointTask(long userId);

    /**
     * 工人荣誉度排序（高到低）
     * @param list 工人列表
     * @param annotationType 荣誉类型
     * @return 排序后工人列表 list
     */
    List<Long> sortListByHonorMessage(List<Long> list,AnnotationType annotationType);
}
