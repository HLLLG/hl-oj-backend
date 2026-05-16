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

    @Test
    void getJudgeInfoAccepted() {
        JudgeInfo judgeInfo = judgeService.getJudgeInfo(buildResult(List.of("3"), 100L, 128L),
                List.of(buildJudgeCase("1 2", "3")), buildJudgeConfig(1000L, 256L));

        assertEquals(JudgeInfoMessageInfo.ACCEPTED.getValue(), judgeInfo.getMessage());
    }

    @Test
    void getJudgeInfoWrongAnswerWhenOutputCountMismatch() {
        JudgeInfo judgeInfo = judgeService.getJudgeInfo(buildResult(List.of("3"), 100L, 128L),
                List.of(buildJudgeCase("1 2", "3"), buildJudgeCase("2 3", "5")), buildJudgeConfig(1000L, 256L));

        assertEquals(JudgeInfoMessageInfo.WRONG_ANSWER.getValue(), judgeInfo.getMessage());
    }

    @Test
    void getJudgeInfoWrongAnswerWhenOutputMismatch() {
        JudgeInfo judgeInfo = judgeService.getJudgeInfo(buildResult(List.of("4"), 100L, 128L),
                List.of(buildJudgeCase("1 2", "3")), buildJudgeConfig(1000L, 256L));

        assertEquals(JudgeInfoMessageInfo.WRONG_ANSWER.getValue(), judgeInfo.getMessage());
    }

    @Test
    void getJudgeInfoTimeLimitExceeded() {
        JudgeInfo judgeInfo = judgeService.getJudgeInfo(buildResult(List.of("3"), 1001L, 128L),
                List.of(buildJudgeCase("1 2", "3")), buildJudgeConfig(1000L, 256L));

        assertEquals(JudgeInfoMessageInfo.TIME_LIMIT_EXCEEDED.getValue(), judgeInfo.getMessage());
    }

    @Test
    void getJudgeInfoMemoryLimitExceeded() {
        JudgeInfo judgeInfo = judgeService.getJudgeInfo(buildResult(List.of("3"), 100L, 257L),
                List.of(buildJudgeCase("1 2", "3")), buildJudgeConfig(1000L, 256L));

        assertEquals(JudgeInfoMessageInfo.MEMORY_LIMIT_EXCEEDED.getValue(), judgeInfo.getMessage());
    }

    @Test
    void getJudgeInfoSystemErrorWhenSandboxResultNull() {
        JudgeInfo judgeInfo = judgeService.getJudgeInfo(null,
                List.of(buildJudgeCase("1 2", "3")), buildJudgeConfig(1000L, 256L));

        assertEquals(JudgeInfoMessageInfo.SYSTEM_ERROR.getValue(), judgeInfo.getMessage());
    }

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
