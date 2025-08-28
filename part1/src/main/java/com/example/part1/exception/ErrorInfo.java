package com.example.part1.exception;

public class ErrorInfo {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int status;
    private String message;

    public ErrorInfo(int status, String message){
        this.status=status;
        this.message=message;
    }

}
