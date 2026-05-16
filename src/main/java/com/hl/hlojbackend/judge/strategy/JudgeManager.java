package com.hl.hlojbackend.judge.strategy;

import com.hl.hlojbackend.model.entity.QuestionSubmit;
import com.hl.hlojbackend.judge.codeSandbox.model.JudgeInfo;

/**
 * 判题管理器
 */
public class JudgeManager {

    public static JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();

        switch (language) {
            case "java":
                return new JavaJudgeStrategy().doJudge(judgeContext);
            default:
                return new DefaultJudgeStrategy().doJudge(judgeContext);
        }
    }
}
