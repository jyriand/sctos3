package ee.jyriand.sctos3.web;


public class SimpleResponse {

    private boolean success = true;

    public SimpleResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
