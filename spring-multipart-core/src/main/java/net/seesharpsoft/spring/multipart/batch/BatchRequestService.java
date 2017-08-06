package net.seesharpsoft.spring.multipart.batch;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BatchRequestService {

    /**
     * Process a batch request.
     * @param batchRequest the batch request entity
     * @param servletRequest the original request
     * @param servletResponse the original response
     * @return a batch response
     * @throws IOException
     * @throws ServletException
     */
    BatchResponse process(BatchRequest batchRequest,
                          HttpServletRequest servletRequest,
                          HttpServletResponse servletResponse) throws ServletException, IOException;
}
