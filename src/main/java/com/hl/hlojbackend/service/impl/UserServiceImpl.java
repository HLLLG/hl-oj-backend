package com.hl.hlojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hl.hlojbackend.constant.UserConstant;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.mapper.UserMapper;
import com.hl.hlojbackend.model.dto.user.UserQueryRequest;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.vo.LoginUserVO;
import com.hl.hlojbackend.model.vo.UserVO;
import com.hl.hlojbackend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String SALT = "hl_oj_salt";

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList("id", "createTime", "updateTime");

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        ThrowUtils.throwIf(!StringUtils.hasText(userAccount) || !StringUtils.hasText(userPassword) || !StringUtils.hasText(checkPassword),
                ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "账号长度不能小于 4 位");
        ThrowUtils.throwIf(userPassword.length() < 8, ErrorCode.PARAMS_ERROR, "密码长度不能小于 8 位");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次输入密码不一致");

        long count = this.count(new QueryWrapper<User>().eq("userAccount", userAccount));
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "账号已存在");

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(getEncryptPassword(userPassword));
        user.setUserName(userAccount);
        user.setUserRole(UserConstant.USER_ROLE);
        user.setEditTime(LocalDateTime.now());
        ThrowUtils.throwIf(!this.save(user), ErrorCode.SYSTEM_ERROR, "注册失败");
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        ThrowUtils.throwIf(!StringUtils.hasText(userAccount) || !StringUtils.hasText(userPassword),
                ErrorCode.PARAMS_ERROR, "参数为空");
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR, "账号或密码错误");
        ThrowUtils.throwIf(userPassword.length() < 8, ErrorCode.PARAMS_ERROR, "账号或密码错误");

        User user = this.getOne(new QueryWrapper<User>()
                .eq("userAccount", userAccount)
                .eq("userPassword", getEncryptPassword(userPassword)));
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "账号或密码错误");

        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return getLoginUserVO(user);
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null,
                ErrorCode.NOT_LOGIN_ERROR);
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(sessionUser == null || sessionUser.getId() == null, ErrorCode.NOT_LOGIN_ERROR);
        User user = this.getById(sessionUser.getId());
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        return user;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }

    @Override
    public String getEncryptPassword(String password) {
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO vo = new LoginUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            return Collections.emptyList();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public Page<UserVO> listUserVOByPage(UserQueryRequest req) {
        long current = req.getCurrent();
        long pageSize = req.getPageSize();
        String sortField = req.getSortField();
        String sortOrder = req.getSortOrder();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(req.getId() != null, "id", req.getId());
        queryWrapper.eq(StringUtils.hasText(req.getUserAccount()), "userAccount", req.getUserAccount());
        queryWrapper.like(StringUtils.hasText(req.getUserName()), "userName", req.getUserName());
        queryWrapper.eq(StringUtils.hasText(req.getUserRole()), "userRole", req.getUserRole());

        boolean validSort = StringUtils.hasText(sortField) && VALID_SORT_FIELDS.contains(sortField);
        queryWrapper.orderBy(validSort, "ascend".equals(sortOrder), sortField);

        Page<User> userPage = this.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVO> voPage = new Page<>(current, pageSize, userPage.getTotal());
        voPage.setRecords(getUserVOList(userPage.getRecords()));
        return voPage;
    }
}
