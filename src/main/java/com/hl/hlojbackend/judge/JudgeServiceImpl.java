package com.hl.hlojbackend.judge;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hl.hlojbackend.exception.ErrorCode;
import com.hl.hlojbackend.exception.ThrowUtils;
import com.hl.hlojbackend.judge.codeSandbox.CodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.CodeSandboxFactory;
import com.hl.hlojbackend.judge.codeSandbox.CodeSandboxProxy;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import com.hl.hlojbackend.judge.strategy.JudgeContext;
import com.hl.hlojbackend.judge.strategy.JudgeManager;
import com.hl.hlojbackend.mapper.QuestionSubmitMapper;
import com.hl.hlojbackend.model.entity.Question;
import com.hl.hlojbackend.model.entity.QuestionSubmit;
import com.hl.hlojbackend.model.enums.JudgeInfoMessageInfo;
import com.hl.hlojbackend.model.enums.QuestionSubmitStatusEnum;
import com.hl.hlojbackend.model.judge.JudgeCase;
import com.hl.hlojbackend.model.judge.JudgeConfig;
import com.hl.hlojbackend.judge.codeSandbox.model.JudgeInfo;
import com.hl.hlojbackend.service.QuestionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Judge service implementation.
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @Resource
    private QuestionService questionService;

    @Resource
    private CodeSandboxFactory codeSandboxFactory;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitMapper.selectById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR, "题目提交不存在");

        Question question = questionService.getById(questionSubmit.getQuestionId());
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");

        if (!QuestionSubmitStatusEnum.WAITING.getValue().equals(questionSubmit.getStatus())) {
            return questionSubmit;
        }

        boolean running = updateSubmitStatus(questionSubmitId, QuestionSubmitStatusEnum.WAITING,
                QuestionSubmitStatusEnum.RUNNING, null);
        if (!running) {
            return questionSubmitMapper.selectById(questionSubmitId);
        }

        JudgeInfo judgeInfo;
        QuestionSubmitStatusEnum questionSubmitStatusEnum;
        try {
            List<JudgeCase> judgeCaseList = parseJudgeCase(question.getJudgeCase());
            JudgeConfig judgeConfig = parseJudgeConfig(question.getJudgeConfig());
            List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

            CodeSandboxRequest codeSandboxRequest = CodeSandboxRequest.builder()
                    .inputList(inputList)
                    .code(questionSubmit.getCode())
                    .language(questionSubmit.getLanguage())
                    .build();
            CodeSandbox codeSandbox = new CodeSandboxProxy(codeSandboxFactory.getCodeSandbox());
            CodeSandboxResult codeSandboxResult = codeSandbox.executeCode(codeSandboxRequest);

            JudgeContext judgeContext = JudgeContext.builder()
                    .codeSandboxResult(codeSandboxResult)
                    .judgeCaseList(judgeCaseList)
                    .question(question)
                    .questionSubmit(questionSubmit)
                    .judgeConfig(judgeConfig)
                    .build();
            judgeInfo = JudgeManager.doJudge(judgeContext);
            questionSubmitStatusEnum = JudgeInfoMessageInfo.ACCEPTED.getValue().equals(judgeInfo.getMessage())
                    ? QuestionSubmitStatusEnum.SUCCEED
                    : QuestionSubmitStatusEnum.FAILED;
        } catch (Exception e) {
            judgeInfo = new JudgeInfo();
            judgeInfo.setMessage(JudgeInfoMessageInfo.SYSTEM_ERROR.getValue());
            questionSubmitStatusEnum = QuestionSubmitStatusEnum.FAILED;
        }

        QuestionSubmit updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setId(questionSubmitId);
        updateQuestionSubmit.setStatus(questionSubmitStatusEnum.getValue());
        updateQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitMapper.updateById(updateQuestionSubmit);

        return questionSubmitMapper.selectById(questionSubmitId);
    }

    private boolean updateSubmitStatus(long questionSubmitId, QuestionSubmitStatusEnum oldStatus,
                                       QuestionSubmitStatusEnum newStatus, JudgeInfo judgeInfo) {
        QuestionSubmit updateQuestionSubmit = new QuestionSubmit();
        updateQuestionSubmit.setStatus(newStatus.getValue());
        if (judgeInfo != null) {
            updateQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }
        UpdateWrapper<QuestionSubmit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", questionSubmitId);
        updateWrapper.eq("status", oldStatus.getValue());
        return questionSubmitMapper.update(updateQuestionSubmit, updateWrapper) > 0;
    }

    private List<JudgeCase> parseJudgeCase(String judgeCase) {
        ThrowUtils.throwIf(!StringUtils.hasText(judgeCase), ErrorCode.SYSTEM_ERROR, "题目判题用例为空");
        return JSONUtil.toList(judgeCase, JudgeCase.class);
    }

    private JudgeConfig parseJudgeConfig(String judgeConfig) {
        if (!StringUtils.hasText(judgeConfig)) {
            return new JudgeConfig();
        }
        return JSONUtil.toBean(judgeConfig, JudgeConfig.class);
    }
}
