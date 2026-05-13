package com.hl.hlojbackend.model.dto.question_submit;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSubmitAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String language;

    private String code;

    private Long questionId;
}
