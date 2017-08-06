package net.seesharpsoft.spring.multipart.batch.services;

import net.seesharpsoft.spring.multipart.batch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public class DispatcherBatchRequestService implements BatchRequestService {

    @Autowired
    DispatcherServlet servlet;

    @Override
    public BatchResponse process(BatchRequest batchRequest,
                                 HttpServletRequest servletRequest,
                                 HttpServletResponse servletResponse) throws ServletException, IOException {
        BatchResponse batchResponse = new BatchResponse();
        for (BatchRequest.Entity singleRequest : batchRequest.getParts()) {
            BatchResponse.Entity singleResponse = processSingleRequest(singleRequest, servletRequest, servletResponse);
            batchResponse.addPart(singleResponse);
        }
        servletResponse.setHeader(HttpHeaders.CONTENT_TYPE, BatchMediaType.MULTIPART_BATCH_VALUE);
        return batchResponse;
    }

    /**
     * Process a single request from a batch.
     * @param singleRequest the single request
     * @param servletRequest the original request
     * @param servletResponse the original response
     * @return a single response
     * @throws ServletException
     * @throws IOException
     */
    protected BatchResponse.Entity processSingleRequest(BatchRequest.Entity singleRequest,
                                                       HttpServletRequest servletRequest,
                                                       HttpServletResponse servletResponse) throws ServletException, IOException {
        URI uri = getSingleRequestUri(singleRequest, servletRequest);

        BatchHttpServletRequest requestWrapper = new BatchHttpServletRequest(servletRequest, uri, singleRequest.getMethod(), singleRequest.getHeaders(), singleRequest.getBody(), singleRequest.getHeaders().getContentType());
        BatchHttpServletResponse responseWrapper = new BatchHttpServletResponse(servletResponse);

        servlet.service(requestWrapper, responseWrapper);

        BatchResponse.Entity responseEntry = new BatchResponse.Entity();
        responseEntry.setBody(responseWrapper.getContent());
        responseEntry.setHeaders(responseWrapper.getHeaderObject());
        responseEntry.setStatus(HttpStatus.valueOf(responseWrapper.getStatus()));
        return responseEntry;
    }

    private URI getSingleRequestUri(BatchRequest.Entity singleRequest, HttpServletRequest servletRequest) throws MalformedURLException {
        String url = singleRequest.getUrl();
        int queryIndex = url.indexOf('?');
        String path = queryIndex == -1 ? url : url.substring(0, queryIndex);
        String query = queryIndex == -1 ? "" : url.substring(queryIndex + 1);
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        String uriString = builder.scheme(servletRequest.getScheme()).host(servletRequest.getServerName()).port(servletRequest.getServerPort()).path(path).query(query).toUriString();
        return URI.create(uriString);
    }
}
