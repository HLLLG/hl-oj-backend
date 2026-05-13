package com.hl.hlojbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hl.hlojbackend.model.dto.question_submit.QuestionSubmitAddRequest;
import com.hl.hlojbackend.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.hl.hlojbackend.model.entity.QuestionSubmit;
import com.hl.hlojbackend.model.entity.User;
import com.hl.hlojbackend.model.vo.QuestionSubmitVO;

public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddRequest request, Long userId);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    Page<QuestionSubmitVO> listQuestionSubmitVOByPage(QuestionSubmitQueryRequest request, User loginUser);
}
