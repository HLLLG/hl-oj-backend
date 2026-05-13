package com.hl.hlojbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hl.hlojbackend.common.BaseResponse;
import com.hl.hlojbackend.common.DeleteRequest;
import com.hl.hlojbackend.common.ResultsUtils;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.model.dto.question.*;
import com.hl.hlojbackend.model.entity.Question;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.vo.QuestionVO;
import com.hl.hlojbackend.service.QuestionService;
import com.hl.hlojbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    // ===== 管理员接口 =====

    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest request,
                                          HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(httpRequest), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        return ResultsUtils.success(questionService.addQuestion(request, loginUser.getId()));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest,
                                                HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0,
                ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        Question question = questionService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!question.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser),
                ErrorCode.NOT_AUTH_ERROR);
        return ResultsUtils.success(questionService.removeById(deleteRequest.getId()));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest request,
                                                HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(httpRequest), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(request == null || request.getId() == null, ErrorCode.PARAMS_ERROR);
        return ResultsUtils.success(questionService.updateQuestion(request));
    }

    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(@RequestParam long id,
                                                  HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(httpRequest), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultsUtils.success(question);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest request,
                                                           HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(httpRequest), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        return ResultsUtils.success(questionService.listQuestionByPage(request));
    }

    @PostMapping("/export")
    public BaseResponse<List<Question>> exportQuestions(@RequestBody QuestionQueryRequest request,
                                                        HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(httpRequest), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        return ResultsUtils.success(questionService.listQuestionByPage(request).getRecords());
    }

    @PostMapping("/import")
    public BaseResponse<Integer> importQuestions(@RequestBody List<QuestionAddRequest> requests,
                                                 HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(!userService.isAdmin(httpRequest), ErrorCode.NOT_AUTH_ERROR);
        ThrowUtils.throwIf(requests == null || requests.isEmpty(), ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        int count = 0;
        for (QuestionAddRequest request : requests) {
            questionService.addQuestion(request, loginUser.getId());
            count++;
        }
        return ResultsUtils.success(count);
    }

    // ===== 已登录接口 =====

    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest request,
                                              HttpServletRequest httpRequest) {
        ThrowUtils.throwIf(request == null || request.getId() == null, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(httpRequest);
        Question oldQuestion = questionService.getById(request.getId());
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser),
                ErrorCode.NOT_AUTH_ERROR);
        return ResultsUtils.success(questionService.editQuestion(request));
    }

    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(@RequestParam long id,
                                                      HttpServletRequest httpRequest) {
        userService.getLoginUser(httpRequest);
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultsUtils.success(questionService.getQuestionVO(question));
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest request,
                                                               HttpServletRequest httpRequest) {
        userService.getLoginUser(httpRequest);
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request.getPageSize() > 100, ErrorCode.PARAMS_ERROR, "每页数量不能超过 100");
        return ResultsUtils.success(questionService.listQuestionVOByPage(request));
    }
}
