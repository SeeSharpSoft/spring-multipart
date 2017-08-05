package net.seesharpsoft.spring.multipart.batch;

import net.seesharpsoft.spring.multipart.MultipartEntity;
import net.seesharpsoft.spring.multipart.MultipartMessage;
import net.seesharpsoft.spring.multipart.MultipartRfc2046MessageConverter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class BatchMessageConverter extends MultipartRfc2046MessageConverter {

    @Override
    public boolean canRead(Class clazz, MediaType mediaType) {
        return BatchRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canWrite(Class clazz, MediaType mediaType) {
        return BatchResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected MultipartMessage createMultipartMessage() {
        return new BatchRequest();
    }

    @Override
    protected MultipartEntity createMultipartEntity() {
        return new BatchRequest.Entity();
    }

    @Override
    protected void applyEntityBody(MultipartEntity entity, String part) {
        int headerContentSplitIndex = part.indexOf(CRLF + CRLF);
        boolean hasBody = headerContentSplitIndex != -1;
        String headerPart = !hasBody ? part : part.substring(0, headerContentSplitIndex);
        String bodyPart = !hasBody ? "" : part.substring(headerContentSplitIndex + (CRLF + CRLF).length(), part.length());

        applyBodyHeader((BatchRequest.Entity)entity, headerPart);
        super.applyEntityBody(entity, bodyPart);
    }

    protected void applyBodyHeader(BatchRequest.Entity entity, String content) {
        int headerContentSplitIndex = content.indexOf(CRLF);
        String urlPart = content.substring(0, headerContentSplitIndex);
        String[] targetUrlParts = urlPart.split(" ");
        entity.setMethod(HttpMethod.resolve(targetUrlParts[0]));
        entity.setUrl(targetUrlParts[1]);

        applyEntityHeaders(entity, content.substring(headerContentSplitIndex, content.length()));
    }

    /******************************** RESPONSE ***************************/

    @Override
    protected void writePartHeader(OutputStreamWriter writer, MultipartEntity entry) throws IOException {
        writer.write(HttpHeaders.CONTENT_TYPE);
        writer.write(": ");
        writer.write("application/http");
        writer.write(CRLF);
        writer.write("Content-Transfer-Encoding");
        writer.write(": ");
        writer.write("binary");
        writer.write(CRLF);
    }

    @Override
    protected void writePartContent(OutputStreamWriter writer, MultipartEntity entry) throws IOException {
        writeResponseStatus(writer, (BatchResponse.Entity)entry);
        byte[] body = entry.getBody();
        if (body != null) {
            writePartContentHeader(writer, entry.getHeaders(), body.length);
            writer.write(CRLF);
            writer.write(new String(body, DEFAULT_CHARSET));
            writer.write(CRLF);
        }
    }

    private void writeResponseStatus(OutputStreamWriter writer, BatchResponse.Entity entry) throws IOException {
        writer.write("HTTP/1.1 ");
        writer.write(entry.getStatus().value() + "");
        writer.write(" ");
        writer.write(entry.getStatus().getReasonPhrase());
        writer.write(CRLF);
    }

    private void writePartContentHeader(OutputStreamWriter writer, HttpHeaders headers, int contentLength) throws IOException {
        writer.write(HttpHeaders.CONTENT_TYPE);
        writer.write(": ");
        writer.write(headers.getContentType().toString());
        writer.write(CRLF);
        writer.write(HttpHeaders.CONTENT_LENGTH);
        writer.write(": ");
        writer.write(contentLength + "");
        writer.write(CRLF);
    }
}
