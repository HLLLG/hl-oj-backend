package com.hl.hlojbackend.judge.codeSandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码沙箱请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeSandboxRequest {

    private List<String> inputList;

    private String code;

    private String language;
}
