package com.springboot.paymentapi.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper
{
    private final String payload;

    public RequestWrapper(HttpServletRequest request)
    {
        super(request);
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = request.getInputStream()) {

            if (inputStream != null) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException exception) {
            log.error("Exception while read the request body");
        }

        payload = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
        return new ServletInputStream()
        {
            @Override
            public boolean isFinished()
            {
                return false;
            }

            @Override
            public boolean isReady()
            {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                //No change
            }

            public int read() throws IOException
            {
                return byteArrayInputStream.read();
            }
        };
    }
}
