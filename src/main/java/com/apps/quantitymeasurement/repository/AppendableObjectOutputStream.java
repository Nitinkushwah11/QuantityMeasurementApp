package com.apps.quantitymeasurement.repository;


import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Custom ObjectOutputStream that doesn't write a header when appending to an
 * existing file. This is necessary to avoid corrupting the file when multiple
 * objects are written sequentially. The header is only written if the file is
 * new or empty. If the file already exists and has content, the stream is
 * reset instead of writing a new header.
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {
    
    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // Don't write header when appending to existing file
        // Only write header if file is new/empty
        File file = new File(QuantityMeasurementCacheRepository.FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            super.writeStreamHeader();
        } else {
            reset(); // Just reset instead of writing header
        }
    }
}