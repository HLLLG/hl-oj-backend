package com.hl.hlojbackend.judge.codeSandbox;

import com.hl.hlojbackend.judge.codeSandbox.impl.ExampleCodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.impl.RemoteCodeSandbox;
import com.hl.hlojbackend.judge.codeSandbox.impl.ThirdPartyCodeSandbox;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码沙箱工厂
 */
@Component
public class CodeSandboxFactory {

    @Resource
    private RemoteCodeSandbox remoteCodeSandbox;

    @Resource
    private ExampleCodeSandbox exampleCodeSandbox;

    @Resource
    private ThirdPartyCodeSandbox thirdPartyCodeSandbox;

    @Value("${code-sandbox.type:example}")
    private String type;

    public CodeSandbox getCodeSandbox() {
        return switch (type) {
            case "remote" -> remoteCodeSandbox;
            case "thirdParty" -> thirdPartyCodeSandbox;
            default -> exampleCodeSandbox;
        };
    }
}
