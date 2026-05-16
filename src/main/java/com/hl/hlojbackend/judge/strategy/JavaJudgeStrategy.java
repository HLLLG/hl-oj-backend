package com.hl.hlojbackend.judge.strategy;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import com.hl.hlojbackend.model.enums.JudgeInfoMessageInfo;
import com.hl.hlojbackend.model.judge.JudgeCase;
import com.hl.hlojbackend.model.judge.JudgeConfig;
import com.hl.hlojbackend.judge.codeSandbox.model.JudgeInfo;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Java 判题策略
 */
public class JavaJudgeStrategy implements JudgeStrategy{
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageInfo.ACCEPTED.getValue());

        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        CodeSandboxResult codeSandboxResult = judgeContext.getCodeSandboxResult();


        if (codeSandboxResult == null) {
            judgeInfo.setMessage(JudgeInfoMessageInfo.SYSTEM_ERROR.getValue());
            return judgeInfo;
        }

        JudgeInfo codeSandboxJudgeInfo = codeSandboxResult.getJudgeInfo();
        if (codeSandboxJudgeInfo != null) {
            judgeInfo.setTime(codeSandboxJudgeInfo.getTime());
            judgeInfo.setMemory(codeSandboxJudgeInfo.getMemory());
            if (StringUtils.hasText(codeSandboxJudgeInfo.getMessage())
                    && !JudgeInfoMessageInfo.ACCEPTED.getValue().equals(codeSandboxJudgeInfo.getMessage())) {
                judgeInfo.setMessage(codeSandboxJudgeInfo.getMessage());
                return judgeInfo;
            }
        }

        List<String> outputList = codeSandboxResult.getOutputList();
        List<JudgeCase> expectedJudgeCaseList = judgeCaseList == null ? Collections.emptyList() : judgeCaseList;
        if (outputList == null || outputList.size() != expectedJudgeCaseList.size()) {
            judgeInfo.setMessage(JudgeInfoMessageInfo.WRONG_ANSWER.getValue());
            return judgeInfo;
        }

        for (int i = 0; i < expectedJudgeCaseList.size(); i++) {
            String actualOutput = outputList.get(i);
            String expectedOutput = expectedJudgeCaseList.get(i).getOutput();
            if (!Objects.equals(actualOutput, expectedOutput)) {
                judgeInfo.setMessage(JudgeInfoMessageInfo.WRONG_ANSWER.getValue());
                return judgeInfo;
            }
        }

        // 判断时间、空间复杂度
        // JAVA 程序本身需要额外执行10秒钟
        long JAVA_PROGRAM_EXTRA_TIME = 10000L;
        if (judgeConfig != null) {
            Long time = judgeInfo.getTime();
            Long memory = judgeInfo.getMemory();
            if (time != null && judgeConfig.getTimeLimit() != null && (time - JAVA_PROGRAM_EXTRA_TIME) > judgeConfig.getTimeLimit()) {
                judgeInfo.setMessage(JudgeInfoMessageInfo.TIME_LIMIT_EXCEEDED.getValue());
                return judgeInfo;
            }
            if (memory != null && judgeConfig.getMemoryLimit() != null && memory > judgeConfig.getMemoryLimit()) {
                judgeInfo.setMessage(JudgeInfoMessageInfo.MEMORY_LIMIT_EXCEEDED.getValue());
                return judgeInfo;
            }
        }

        return judgeInfo;
    }
}
