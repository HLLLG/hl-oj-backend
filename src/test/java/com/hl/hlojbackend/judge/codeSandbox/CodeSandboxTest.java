package com.hl.hlojbackend.judge.codeSandbox;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSandboxTest {

    @Resource
    private CodeSandboxFactory codeSandboxFactory;

    @Test
    void executeCode() {
        CodeSandbox codeSandbox = new CodeSandboxProxy(codeSandboxFactory.getCodeSandbox());
        CodeSandboxRequest request = CodeSandboxRequest.builder().inputList(null).code("public class Main { public " +
                "static void main(String[] args) { System.out.println(\"Hello " + "World\"); } }").language("java").build();
        CodeSandboxResult result = codeSandbox.executeCode(request);
    }
}