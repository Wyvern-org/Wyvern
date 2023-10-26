package wyvern.util.http;

import wyvern.util.jobs.IPostJob;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRequest
{
    private URL url;
    private HttpURLConnection connection;
    private StringBuilder response = new StringBuilder();

    public HttpRequest(String url)
    {
        try
        {
            this.url = new URL(url);
            this.connection = (HttpURLConnection) this.url.openConnection();
            this.connection.setDoOutput(true);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public HttpRequest setContentType(String contentType)
    {
        this.connection.setRequestProperty("Content-Type", contentType);
        return this;
    }

    public HttpRequest setRequestMethod(String method)
    {
        try
        {
            this.connection.setRequestMethod(method);
            return this;
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    public HttpRequest setRequestProperty(String key, String value)
    {
        this.connection.setRequestProperty(key, value);
        return this;
    }

    public HttpRequest writeString(String inputString)
    {
        try (OutputStream outputStream = connection.getOutputStream())
        {
            byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return this;
    }

    public HttpRequest readResponse()
    {
        try
        {
            InputStream is = this.connection.getResponseCode() == HttpURLConnection.HTTP_OK ? this.connection.getInputStream() : this.connection.getErrorStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                    response.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return this;
    }

    public HttpURLConnection getConnection()
    {
        return connection;
    }

    public StringBuilder getResponse()
    {
        return response;
    }
}
