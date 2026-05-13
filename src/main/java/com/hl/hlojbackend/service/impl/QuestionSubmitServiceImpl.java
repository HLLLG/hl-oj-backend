package com.hl.hlojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hl.hlojbackend.constant.UserConstant;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.mapper.QuestionSubmitMapper;
import com.hl.hlojbackend.model.dto.question_submit.QuestionSubmitAddRequest;
import com.hl.hlojbackend.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.hl.hlojbackend.model.entity.Question;
import com.hl.hlojbackend.model.entity.QuestionSubmit;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.judge.JudgeInfo;
import com.hl.hlojbackend.model.vo.QuestionSubmitVO;
import com.hl.hlojbackend.service.QuestionService;
import com.hl.hlojbackend.service.QuestionSubmitService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private ObjectMapper objectMapper;

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList(
            "id", "createTime", "updateTime", "status");

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest request, Long userId) {
        ThrowUtils.throwIf(!StringUtils.hasText(request.getLanguage()), ErrorCode.PARAMS_ERROR, "编程语言不能为空");
        ThrowUtils.throwIf(!StringUtils.hasText(request.getCode()), ErrorCode.PARAMS_ERROR, "代码不能为空");
        ThrowUtils.throwIf(request.getQuestionId() == null || request.getQuestionId() <= 0,
                ErrorCode.PARAMS_ERROR, "题目不合法");

        Question question = questionService.getById(request.getQuestionId());
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(request.getLanguage());
        questionSubmit.setCode(request.getCode());
        questionSubmit.setQuestionId(request.getQuestionId());
        questionSubmit.setUserId(userId);
        questionSubmit.setStatus(0);
        questionSubmit.setJudgeInfo("{}");

        ThrowUtils.throwIf(!this.save(questionSubmit), ErrorCode.SYSTEM_ERROR, "题目提交失败");
        return questionSubmit.getId();
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO vo = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, vo);
        vo.setJudgeInfo(fromJson(questionSubmit.getJudgeInfo(), new TypeReference<JudgeInfo>() {}));
        boolean isOwner = questionSubmit.getUserId().equals(loginUser.getId());
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        if (!isOwner && !isAdmin) {
            vo.setCode(null);
        }
        return vo;
    }

    @Override
    public Page<QuestionSubmitVO> listQuestionSubmitVOByPage(QuestionSubmitQueryRequest request, User loginUser) {
        long current = request.getCurrent();
        long pageSize = request.getPageSize();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();

        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());

        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(request.getId() != null, "id", request.getId());
        queryWrapper.eq(StringUtils.hasText(request.getLanguage()), "language", request.getLanguage());
        queryWrapper.eq(request.getStatus() != null, "status", request.getStatus());
        queryWrapper.eq(request.getQuestionId() != null, "questionId", request.getQuestionId());
        if (isAdmin) {
            queryWrapper.eq(request.getUserId() != null, "userId", request.getUserId());
        } else {
            queryWrapper.eq("userId", loginUser.getId());
        }

        boolean validSort = StringUtils.hasText(sortField) && VALID_SORT_FIELDS.contains(sortField);
        queryWrapper.orderBy(validSort, "ascend".equals(sortOrder), sortField);

        Page<QuestionSubmit> submitPage = this.page(new Page<>(current, pageSize), queryWrapper);
        Page<QuestionSubmitVO> voPage = new Page<>(current, pageSize, submitPage.getTotal());
        voPage.setRecords(submitPage.getRecords().stream()
                .map(qs -> getQuestionSubmitVO(qs, loginUser))
                .collect(Collectors.toList()));
        return voPage;
    }

    private <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
