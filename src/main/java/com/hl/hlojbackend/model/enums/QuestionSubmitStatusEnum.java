package com.hl.hlojbackend.model.enums;

import lombok.Getter;

/**
 * 提交状态枚举
 *
 * @author hl
 */
@Getter
public enum QuestionSubmitStatusEnum {

    WAITING("等待中", 0),

    RUNNING("判题中", 1),

    SUCCEED("成功", 2),

    FAILED("失败", 3);

    private final String text;

    private final Integer value;

    QuestionSubmitStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        for (QuestionSubmitStatusEnum anEnum : values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
