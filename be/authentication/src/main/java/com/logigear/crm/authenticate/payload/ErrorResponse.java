package com.logigear.crm.authenticate.payload;

public class ErrorResponse {
    private long status;
    private String message;

    public ErrorResponse(long status, String message) {
        this.status = status;
        this.message = message;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
