package com.hl.hlojbackend.judge.codeSandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.hl.hlojbackend.judge.codeSandbox.CodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import org.springframework.stereotype.Service;

/**
 * 远程代码沙箱
 */
@Service
public class RemoteCodeSandbox implements CodeSandbox {

    private static final String SANDBOX_URL = "http://localhost:8090/executeCode";

    @Override
    public CodeSandboxResult executeCode(CodeSandboxRequest request) {
        String json = JSONUtil.toJsonStr(request);
        String response = HttpUtil.createPost(SANDBOX_URL)
                .header("Content-Type", "application/json")
                .body(json)
                .execute()
                .body();
        return JSONUtil.toBean(response, CodeSandboxResult.class);
    }
}
