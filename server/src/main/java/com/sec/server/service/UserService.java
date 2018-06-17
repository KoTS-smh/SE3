package com.sec.server.service;

import com.sec.server.domain.User;
import com.sec.server.model.PersonalTaskNumModel;
import com.sec.server.model.UserModel;

public interface UserService {
     User login(String username, String password);

     boolean register(UserModel userModel);

     User getUserById(long userId);

     User updateUser(User user);

     void deleteUser(long userId);

     void logout(long userId);

     void recharge(int num, long userId);

     double getBalance(long userId);

     PersonalTaskNumModel getPersonalTaskNum(long userId);

     void pointAddThree(long userId);

     void pointInc(long userId);

     void pointDrop(long userId);
}
