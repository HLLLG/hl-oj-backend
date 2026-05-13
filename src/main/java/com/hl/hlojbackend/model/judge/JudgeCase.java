package com.hl.hlojbackend.model.judge;

import lombok.Data;
import java.io.Serializable;

@Data
public class JudgeCase implements Serializable {

    private static final long serialVersionUID = 1L;

    private String input;

    private String output;
}
