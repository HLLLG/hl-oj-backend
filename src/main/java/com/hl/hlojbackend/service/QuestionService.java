package com.hl.hlojbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hl.hlojbackend.model.dto.question.QuestionAddRequest;
import com.hl.hlojbackend.model.dto.question.QuestionEditRequest;
import com.hl.hlojbackend.model.dto.question.QuestionQueryRequest;
import com.hl.hlojbackend.model.dto.question.QuestionUpdateRequest;
import com.hl.hlojbackend.model.entity.Question;
import com.hl.hlojbackend.model.vo.QuestionVO;

import java.util.List;

public interface QuestionService extends IService<Question> {

    void validQuestion(Question question, boolean add);

    long addQuestion(QuestionAddRequest request, Long userId);

    boolean updateQuestion(QuestionUpdateRequest request);

    boolean editQuestion(QuestionEditRequest request);

    QuestionVO getQuestionVO(Question question);

    List<QuestionVO> getQuestionVOList(List<Question> questions);

    Page<QuestionVO> listQuestionVOByPage(QuestionQueryRequest request);

    Page<Question> listQuestionByPage(QuestionQueryRequest request);
}
