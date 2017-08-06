package net.seesharpsoft.spring.multipart.test.util;

import net.seesharpsoft.spring.util.ByteArrayServletInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class HttpInputMessageDummy implements HttpInputMessage {

    private HttpHeaders headers;

    private byte[] body;

    public HttpInputMessageDummy(HttpHeaders headers, InputStream inputStream) throws IOException {
        this(headers, IOUtils.readFully(inputStream, -1, true));
    }

    public HttpInputMessageDummy(HttpHeaders headers, byte[] body) {
        this.headers = HttpHeaders.readOnlyHttpHeaders(headers);
        this.body = body == null ? null : body.clone();
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayServletInputStream(this.body);
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
