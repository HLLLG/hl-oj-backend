package com.hl.hlojbackend.judge;

import com.hl.hlojbackend.model.entity.QuestionSubmit;

/**
 * Judge service.
 */
public interface JudgeService {

    /**
     * Judge a question submit.
     *
     * @param questionSubmitId question submit id
     * @return latest question submit
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
