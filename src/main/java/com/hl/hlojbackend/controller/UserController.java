package com.hl.hlojbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hl.hlojbackend.common.BaseResponse;
import com.hl.hlojbackend.common.DeleteRequest;
import com.hl.hlojbackend.common.ResultsUtils;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.model.dto.user.*;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.vo.LoginUserVO;
import com.hl.hlojbackend.model.vo.UserVO;
import com.hl.hlojbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // ===== 公开接口 =====

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        long userId = userService.userRegister(
                request.getUserAccount(), request.getUserPassword(), request.getCheckPassword());
        return ResultsUtils.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest request,
                                               HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        LoginUserVO vo = userService.userLogin(
                request.getUserAccount(), request.getUserPassword(), httpRequest);
        return ResultsUtils.success(vo);
    }

    // ===== 已登录接口 =====

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return ResultsUtils.success(userService.userLogout(request));
    }

    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultsUtils.success(userService.getLoginUserVO(user));
    }

    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(@RequestParam long id, HttpServletRequest request) {
        userService.getLoginUser(request);
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultsUtils.success(userService.getUserVO(user));
    }

    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest updateRequest,
                                              HttpServletRequest request) {
        ThrowUtils.throwIf(updateRequest == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        user.setId(loginUser.getId());
        user.setEditTime(LocalDateTime.now());
        return ResultsUtils.success(userService.updateById(user));
    }

    // ===== 管理员接口 =====

    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest addRequest,
                                      HttpServletRequest request) {
        ThrowUtils.throwIf(!userService.isAdmin(request), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(addRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(addRequest, user);
        user.setUserPassword(userService.getEncryptPassword(addRequest.getUserPassword()));
        user.setEditTime(LocalDateTime.now());
        ThrowUtils.throwIf(!userService.save(user), ErrorCode.SYSTEM_ERROR, "新增用户失败");
        return ResultsUtils.success(user.getId());
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest,
                                            HttpServletRequest request) {
        ThrowUtils.throwIf(!userService.isAdmin(request), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0,
                ErrorCode.PARAMS_ERROR);
        return ResultsUtils.success(userService.removeById(deleteRequest.getId()));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest updateRequest,
                                            HttpServletRequest request) {
        ThrowUtils.throwIf(!userService.isAdmin(request), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(updateRequest == null || updateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        user.setEditTime(LocalDateTime.now());
        return ResultsUtils.success(userService.updateById(user));
    }

    @GetMapping("/get")
    public BaseResponse<User> getUserById(@RequestParam long id, HttpServletRequest request) {
        ThrowUtils.throwIf(!userService.isAdmin(request), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultsUtils.success(user);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest queryRequest,
                                                       HttpServletRequest request) {
        ThrowUtils.throwIf(!userService.isAdmin(request), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(queryRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultsUtils.success(userService.listUserVOByPage(queryRequest));
    }
}
