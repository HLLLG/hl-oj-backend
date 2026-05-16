package com.hl.hlojbackend.judge.codeSandbox.impl;

import com.hl.hlojbackend.judge.codeSandbox.CodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import com.hl.hlojbackend.model.enums.JudgeInfoMessageInfo;
import com.hl.hlojbackend.model.enums.QuestionSubmitStatusEnum;
import com.hl.hlojbackend.judge.codeSandbox.model.JudgeInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 示例代码沙箱
 */
@Service
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public CodeSandboxResult executeCode(CodeSandboxRequest request) {
        List<String> inputList = request.getInputList();

        CodeSandboxResult codeSandboxResult = new CodeSandboxResult();
        codeSandboxResult.setOutputList(inputList);
        codeSandboxResult.setMessage(QuestionSubmitStatusEnum.SUCCEED.getText());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        judgeInfo.setMessage(JudgeInfoMessageInfo.ACCEPTED.getValue());
        codeSandboxResult.setJudgeInfo(judgeInfo);

        return codeSandboxResult;
    }
}
