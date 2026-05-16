package com.hl.hlojbackend.judge.codeSandbox;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;

/**
 * 代码沙箱接口
 */
public interface CodeSandbox {

    CodeSandboxResult executeCode(CodeSandboxRequest request);
}
