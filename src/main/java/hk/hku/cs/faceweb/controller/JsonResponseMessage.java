package hk.hku.cs.faceweb.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class JsonResponseMessage<T> {

    @ApiModelProperty(notes = "The return object request by user")
    @JsonProperty("Content")
    private T content;
    @ApiModelProperty(notes = "The return code")
    @JsonProperty("ReturnCode")
    private  int returnCode;
    @ApiModelProperty(notes = "The message of return")
    @JsonProperty("Message")
    private String message;

    protected JsonResponseMessage() { }

    public JsonResponseMessage(T content, int returnCode, String message) {
        this.content = content;
        this.returnCode = returnCode;
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "JsonResponseMessage{" +
                "Content=" + content +
                ", ReturnCode=" + returnCode +
                ", Message='" + message + '\'' +
                '}';
    }
}
