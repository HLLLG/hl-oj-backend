package com.hl.hlojbackend.judge.codeSandbox.impl;

import com.hl.hlojbackend.judge.codeSandbox.CodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import org.springframework.stereotype.Service;

/**
 * 第三方代码沙箱
 */
@Service
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public CodeSandboxResult executeCode(CodeSandboxRequest request) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
