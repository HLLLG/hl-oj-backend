package com.hl.hlojbackend.judge.strategy;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import com.hl.hlojbackend.model.entity.Question;
import com.hl.hlojbackend.model.entity.QuestionSubmit;
import com.hl.hlojbackend.model.judge.JudgeCase;
import com.hl.hlojbackend.model.judge.JudgeConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 策略上下文
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {

    private CodeSandboxResult codeSandboxResult;

    private JudgeConfig judgeConfig;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
