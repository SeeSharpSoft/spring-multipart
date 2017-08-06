package net.seesharpsoft.spring.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteArrayServletInputStream extends ServletInputStream {

    private ByteArrayInputStream buffer;

    public ByteArrayServletInputStream(byte[] content) {
        this.buffer = new ByteArrayInputStream(content);
    }

    @Override
    public int read() throws IOException {
        return buffer.read();
    }

    @Override
    public boolean isFinished() {
        return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new RuntimeException("Not implemented");
    }
}