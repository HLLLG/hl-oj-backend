package com.hl.hlojbackend.judge.codeSandbox.impl;

import com.hl.hlojbackend.judge.codeSandbox.CodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import org.springframework.stereotype.Service;

/**
 * 远程代码沙箱
 */
@Service
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public CodeSandboxResult executeCode(CodeSandboxRequest request) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
