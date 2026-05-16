package com.hl.hlojbackend.judge.codeSandbox;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Code sandbox proxy.
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public CodeSandboxResult executeCode(CodeSandboxRequest request) {
        log.info("Code sandbox request: {}", request);
        CodeSandboxResult codeSandboxResult = codeSandbox.executeCode(request);
        log.info("Code sandbox response: {}", codeSandboxResult);
        return codeSandboxResult;
    }
}
