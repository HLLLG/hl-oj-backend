package com.hl.hlojbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hl.hlojbackend.model.dto.user.UserQueryRequest;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.vo.LoginUserVO;
import com.hl.hlojbackend.model.vo.UserVO;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User user);

    String getEncryptPassword(String password);

    LoginUserVO getLoginUserVO(User user);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest);
}
