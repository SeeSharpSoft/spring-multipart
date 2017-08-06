package net.seesharpsoft.spring.multipart.batch.services;

public class BatchRequestProperties {

    public BatchRequestProperties() {};

    public BatchRequestProperties(BatchRequestProperties original) {
        if (original != null) {
            this.setIncludeOriginalHeader(original.getIncludeOriginalHeader());
        }
    }

    private boolean includeOriginalHeader = true;

    public void setIncludeOriginalHeader(boolean includeOriginalHeader) {
        this.includeOriginalHeader = includeOriginalHeader;
    }

    public boolean getIncludeOriginalHeader() {
        return this.includeOriginalHeader;
    }
}
