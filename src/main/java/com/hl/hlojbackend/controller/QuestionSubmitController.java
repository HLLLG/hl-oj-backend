package com.hl.hlojbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hl.hlojbackend.common.BaseResponse;
import com.hl.hlojbackend.common.ResultsUtils;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.model.dto.question_submit.QuestionSubmitAddRequest;
import com.hl.hlojbackend.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.hl.hlojbackend.model.entity.QuestionSubmit;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.vo.QuestionSubmitVO;
import com.hl.hlojbackend.service.QuestionSubmitService;
import com.hl.hlojbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/question_submit")
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    // ===== 已登录接口 =====

    @PostMapping("/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest request,
                                               HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        return ResultsUtils.success(questionSubmitService.doQuestionSubmit(request, loginUser.getId()));
    }

    @GetMapping("/get/vo")
    public BaseResponse<QuestionSubmitVO> getQuestionSubmitVOById(@RequestParam long id,
                                                                  HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!questionSubmit.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser),
                ErrorCode.NOT_AUTH_ERROR);
        return ResultsUtils.success(questionSubmitService.getQuestionSubmitVO(questionSubmit, loginUser));
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitVOByPage(
            @RequestBody QuestionSubmitQueryRequest request,
            HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getPageSize() > 100, ErrorCode.PARAMS_ERROR, "每页数量不能超过 100");
        User loginUser = userService.getLoginUser(httpRequest);
        return ResultsUtils.success(questionSubmitService.listQuestionSubmitVOByPage(request, loginUser));
    }
}
