package com.paellasoft.CRUD.repository.custom;

import org.springframework.core.io.InputStreamSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputSource  implements InputStreamSource {

    private final byte[] content;

    public ByteArrayInputSource(byte[] content) {
        this.content = content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }
}
