package com.hl.hlojbackend.model.enums;

import lombok.Getter;

/**
 * 判题信息枚举
 */
@Getter
public enum JudgeInfoMessageInfo {

    ACCEPTED("成功", "Accepted"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    COMPILE_ERROR("编译错误", "Compile Error"),
    TIME_LIMIT_EXCEEDED("时间超限", "Time Limit Exceeded"),
    MEMORY_LIMIT_EXCEEDED("内存超限", "Memory Limit Exceeded"),
    RUNTIME_ERROR("运行错误", "Runtime Error"),
    SYSTEM_ERROR("系统错误", "System Error"),
    WAITING("等待中", "Waiting"),
    OUTPUT_LIMIT_EXCEEDED("输出超限", "Output Limit Exceeded"),
    DANGEROUS_OPERATION("危险操作", "Dangerous Operation");


    private final String text;

    private final String value;

    JudgeInfoMessageInfo(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageInfo getEnumByValue(String value) {
        for (JudgeInfoMessageInfo valueEnum : values()) {
            if (valueEnum.value.equals(value)) {
                return valueEnum;
            }
        }
        return null;
    }
}
