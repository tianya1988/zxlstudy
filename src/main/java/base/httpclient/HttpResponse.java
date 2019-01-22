package base.httpclient;

/**
 * Created by jason on 19-1-16.
 */
public class HttpResponse {

    private int statusCode;

    private String content;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
