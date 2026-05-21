package com.hl.hlojbackend.judge;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import com.hl.hlojbackend.model.enums.JudgeInfoMessageInfo;
import com.hl.hlojbackend.model.judge.JudgeCase;
import com.hl.hlojbackend.model.judge.JudgeConfig;
import com.hl.hlojbackend.judge.codeSandbox.model.JudgeInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JudgeServiceImplTest {

    private final JudgeServiceImpl judgeService = new JudgeServiceImpl();



    private CodeSandboxResult buildResult(List<String> outputList, Long time, Long memory) {
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(time);
        judgeInfo.setMemory(memory);
        judgeInfo.setMessage(JudgeInfoMessageInfo.ACCEPTED.getValue());
        return CodeSandboxResult.builder()
                .outputList(outputList)
                .judgeInfo(judgeInfo)
                .build();
    }

    private JudgeCase buildJudgeCase(String input, String output) {
        JudgeCase judgeCase = new JudgeCase();
        judgeCase.setInput(input);
        judgeCase.setOutput(output);
        return judgeCase;
    }

    private JudgeConfig buildJudgeConfig(Long timeLimit, Long memoryLimit) {
        JudgeConfig judgeConfig = new JudgeConfig();
        judgeConfig.setTimeLimit(timeLimit);
        judgeConfig.setMemoryLimit(memoryLimit);
        return judgeConfig;
    }
}
