package com.example.nhommobile.model;

import java.util.List;

public class LoaiXeModel {
    boolean success;
    String message;
    List<LoaiXe> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoaiXe> getResult() {
        return result;
    }

    public void setResult(List<LoaiXe> result) {
        this.result = result;
    }
}
