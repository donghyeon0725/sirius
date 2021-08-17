package shop.sirius.domain.infra.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {

    private String domain;
    private HttpMethod method;
    private Map<String, String> header;
    private Map<String, String> body;

    public HttpRequest(String domain, HttpMethod method, Map<String, String> header, Map<String, String> body) {
        this.domain = domain;
        this.method = method;
        this.header = header;
        this.body = body;
    }

    private RequestResult request(String domain, HttpMethod method, Map<String, String> headers, Map<String, String> body) {

        int responseCode = 0;

        StringBuilder sb = new StringBuilder();
        try {
            URL u = new URL(domain);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            if (headers != null)
                for (String key : headers.keySet())
                    conn.setRequestProperty(key, headers.get(key));

            conn.setRequestMethod(method.name());
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(15000);

            if (body != null) {
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                String query = queryString(body);
                wr.write(query);
                wr.flush();
                wr.close();
            }

            responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while (true) {
                    line = reader.readLine();
                    if (line == null)
                        break;
                    sb.append(line);
                }
                reader.close();
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                sb.append(response.toString());
            }

        } catch (MalformedURLException e) {
            log.error(domain + " is not a URL I understand");
        } catch (IOException e) {
            log.error("IOException" + e.getMessage());
        }

        return new RequestResult(responseCode, sb.toString());
    }

    private String queryString(Map<String, String> body) {
        StringBuffer sbParams = new StringBuffer();

        boolean isAnd = false;

        for (String key : body.keySet()) {
            if (isAnd)
                sbParams.append("&");
            sbParams.append(key).append("=").append(body.get(key));
            if (!isAnd)
                if (body.size() >= 2)
                    isAnd = true;
        }

        return sbParams.toString();
    }

    public static class Builder {
        private String domain;
        private HttpMethod method;
        private Map<String, String> header = new HashMap<>();
        private Map<String, String> body = new HashMap<>();

        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder header(String key, String value) {
            this.header.put(key, value);
            return this;
        }

        public Builder body(String key, String value) {
            this.body.put(key, value);
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(domain, method, header, body);
        }

    }


    public RequestResult fetch() {
        return request(domain, method, header, body);
    }

}
