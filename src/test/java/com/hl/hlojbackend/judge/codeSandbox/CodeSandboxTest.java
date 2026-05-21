package com.hl.hlojbackend.judge.codeSandbox;

import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxRequest;
import com.hl.hlojbackend.judge.codeSandbox.model.CodeSandboxResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSandboxTest {

    @Resource
    private CodeSandboxFactory codeSandboxFactory;

    @Test
    void executeCode() {
        CodeSandbox codeSandbox = new CodeSandboxProxy(codeSandboxFactory.getCodeSandbox());
        String code = "import java.util.Scanner;" +
                "public class Main {" +
                "    public static void main(String[] args) {" +
                "        Scanner sc = new Scanner(System.in);" +
                "        int a = sc.nextInt(), b = sc.nextInt();" +
                "        System.out.println(a + b);" +
                "    }" +
                "}";
        List<String> inputList = Arrays.asList("1 2", "10 20", "100 200");
        CodeSandboxRequest request = CodeSandboxRequest.builder()
                .inputList(inputList)
                .code(code)
                .language("java")
                .build();
        CodeSandboxResult result = codeSandbox.executeCode(request);
        System.out.println(result);
    }
}