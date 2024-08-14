package cz.diamo.vratnice_public.exception;

import org.springframework.http.HttpStatus;

public class ReCaptchaException extends Exception {

private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;

    public ReCaptchaException(String message) {
        super(message);
        this.httpStatus = HttpStatus.FORBIDDEN;
    }

    public ReCaptchaException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
