package com.hl.hlojbackend.model.dto.question_submit;

import com.hl.hlojbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String language;

    private Integer status;

    private Long questionId;

    private Long userId;
}
