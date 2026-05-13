package com.hl.hlojbackend.model.vo;

import com.hl.hlojbackend.model.judge.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String content;

    private List<String> tags;

    private Integer submitNum;

    private Integer acceptedNum;

    private Integer thumbNum;

    private Integer favourNum;

    private JudgeConfig judgeConfig;

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private UserVO userVO;
}
