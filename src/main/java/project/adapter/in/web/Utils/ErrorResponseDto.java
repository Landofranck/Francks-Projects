package project.adapter.in.web.Utils;

import java.util.List;

public class ErrorResponseDto {
    private int status;
    private Code code;
    private String message;
    private List<Link> links;

    public ErrorResponseDto() {}
    public ErrorResponseDto(String message) { this.message = message; }
    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}

