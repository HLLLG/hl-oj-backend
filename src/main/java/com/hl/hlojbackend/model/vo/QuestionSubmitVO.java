package com.hl.hlojbackend.model.vo;

import com.hl.hlojbackend.model.judge.JudgeInfo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QuestionSubmitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String language;

    /** 用户代码（非提交者/非管理员时为 null） */
    private String code;

    /** 判题结果（反序列化后的对象） */
    private JudgeInfo judgeInfo;

    /** 判题状态：0-待判题 1-判题中 2-成功 3-失败 */
    private Integer status;

    private Long questionId;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
