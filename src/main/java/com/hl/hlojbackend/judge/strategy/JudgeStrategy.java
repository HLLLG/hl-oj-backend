package com.hl.hlojbackend.judge.strategy;


import com.hl.hlojbackend.judge.codeSandbox.model.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
