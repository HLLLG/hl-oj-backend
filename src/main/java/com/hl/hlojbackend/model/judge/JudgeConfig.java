package com.hl.hlojbackend.model.judge;

import lombok.Data;
import java.io.Serializable;

@Data
public class JudgeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 时间限制（ms） */
    private Long timeLimit;

    /** 内存限制（kb） */
    private Long memoryLimit;

    /** 栈限制（kb） */
    private Long stackLimit;
}
