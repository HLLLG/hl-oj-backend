package com.hl.hlojbackend.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hl.hlojbackend.exception.BusinessException;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.mapper.QuestionMapper;
import com.hl.hlojbackend.model.dto.question.QuestionAddRequest;
import com.hl.hlojbackend.model.dto.question.QuestionEditRequest;
import com.hl.hlojbackend.model.dto.question.QuestionQueryRequest;
import com.hl.hlojbackend.model.dto.question.QuestionUpdateRequest;
import com.hl.hlojbackend.model.entity.Question;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.judge.JudgeConfig;
import com.hl.hlojbackend.model.vo.QuestionVO;
import com.hl.hlojbackend.service.QuestionService;
import com.hl.hlojbackend.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Resource
    private UserService userService;

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList(
            "id", "createTime", "updateTime", "submitNum", "acceptedNum", "thumbNum", "favourNum");

    @Override
    public void validQuestion(Question question, boolean add) {
        String title = question.getTitle();
        String content = question.getContent();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();

        if (add) {
            ThrowUtils.throwIf(!StringUtils.hasText(title), ErrorCode.PARAMS_ERROR, "题目标题不能为空");
            ThrowUtils.throwIf(!StringUtils.hasText(content), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
            ThrowUtils.throwIf(!StringUtils.hasText(judgeCase), ErrorCode.PARAMS_ERROR, "判题用例不能为空");
            ThrowUtils.throwIf(!StringUtils.hasText(judgeConfig), ErrorCode.PARAMS_ERROR, "判题配置不能为空");
        }
        if (StringUtils.hasText(title)) {
            ThrowUtils.throwIf(title.length() > 512, ErrorCode.PARAMS_ERROR, "题目标题过长");
        }
        if (StringUtils.hasText(content)) {
            ThrowUtils.throwIf(content.length() > 8192, ErrorCode.PARAMS_ERROR, "题目内容过长");
        }
    }

    @Override
    public long addQuestion(QuestionAddRequest request, Long userId) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setTags(JSONUtil.toJsonStr(request.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(request.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(request.getJudgeConfig()));
        question.setUserId(userId);
        question.setSubmitNum(0);
        question.setAcceptedNum(0);
        question.setThumbNum(0);
        question.setFavourNum(0);
        validQuestion(question, true);
        ThrowUtils.throwIf(!this.save(question), ErrorCode.SYSTEM_ERROR, "新增题目失败");
        return question.getId();
    }

    @Override
    public boolean updateQuestion(QuestionUpdateRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setTags(JSONUtil.toJsonStr(request.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(request.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(request.getJudgeConfig()));
        validQuestion(question, false);
        return this.updateById(question);
    }

    @Override
    public boolean editQuestion(QuestionEditRequest request) {
        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setTags(JSONUtil.toJsonStr(request.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(request.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(request.getJudgeConfig()));
        validQuestion(question, false);
        return this.updateById(question);
    }

    @Override
    public QuestionVO getQuestionVO(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO vo = new QuestionVO();
        BeanUtils.copyProperties(question, vo);
        // 关联查询用户信息
        Long userId = question.getUserId();
        User user = userService.getById(userId);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        vo.setUserVO(userService.getUserVO(user));
        vo.setTags(JSONUtil.toList(question.getTags(), String.class));
        vo.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return vo;
    }

    @Override
    public List<QuestionVO> getQuestionVOList(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return Collections.emptyList();
        }
        return questions.stream().map(this::getQuestionVO).collect(Collectors.toList());
    }

    @Override
    public Page<QuestionVO> listQuestionVOByPage(QuestionQueryRequest request) {
        long current = request.getCurrent();
        long pageSize = request.getPageSize();
        Page<Question> questionPage = this.page(new Page<>(current, pageSize), buildQueryWrapper(request));
        Page<QuestionVO> voPage = new Page<>(current, pageSize, questionPage.getTotal());
        voPage.setRecords(getQuestionVOList(questionPage.getRecords()));
        return voPage;
    }

    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest request) {
        long current = request.getCurrent();
        long pageSize = request.getPageSize();
        return this.page(new Page<>(current, pageSize), buildQueryWrapper(request));
    }

    private QueryWrapper<Question> buildQueryWrapper(QuestionQueryRequest request) {
        Long id = request.getId();
        String title = request.getTitle();
        String content = request.getContent();
        List<String> tags = request.getTags();
        Long userId = request.getUserId();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();

        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.hasText(title), "title", title);
        queryWrapper.like(StringUtils.hasText(content), "content", content);
        queryWrapper.eq(userId != null, "userId", userId);
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                queryWrapper.like("tags", tag);
            }
        }
        boolean validSort = StringUtils.hasText(sortField) && VALID_SORT_FIELDS.contains(sortField);
        queryWrapper.orderBy(validSort, "ascend".equals(sortOrder), sortField);
        return queryWrapper;
    }
}
