package net.seesharpsoft.spring.multipart.batch;

import org.springframework.http.MediaType;

import java.util.HashMap;

/**
 * Introducing batch media type (multipart/mixed; boundary=batch).
 */
public final class BatchMediaType {
    /**
     * The batch media type.
     */
    public static final MediaType MULTIPART_BATCH
            = new MediaType("multipart", "mixed", new HashMap<String, String>() {{
                this.put("boundary", "batch");
            }});

    /**
     * The string representation of the batch media type.
     */
    public static final String MULTIPART_BATCH_VALUE = MULTIPART_BATCH.toString();
}
