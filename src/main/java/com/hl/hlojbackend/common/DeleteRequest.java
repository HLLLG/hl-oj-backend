package com.hl.hlojbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 7811114025353969497L;

    private Long id;
}
