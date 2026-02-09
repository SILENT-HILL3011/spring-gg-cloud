package com.idea.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    public R(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
