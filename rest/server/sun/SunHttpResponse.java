package rest.server.sun;

import java.io.IOException;
import java.util.List;

import rest.server.Response;

import com.sun.net.httpserver.HttpExchange;

public class SunHttpResponse implements Response {
	private HttpExchange http;
	private String body;
	private int responseCode;

	public SunHttpResponse(HttpExchange http) {
		this.http = http;
	}

	@Override
	public void setHeader(String name, List<String> value) {
		http.getResponseHeaders().put(name, value);
	}

	@Override
	public void setResonseCode(int code) {
		responseCode = code;
	}

	@Override
	public void setBody(String body) {
		this.body = body;
	}

	public void close() {
		try {
			if(body != null && responseCode != 0) {
				byte[] buffer = body.getBytes();
				http.sendResponseHeaders(responseCode, buffer.length);
				http.getResponseBody().write(buffer);
			} else {
				http.sendResponseHeaders(503, 0);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		http.close();
	}

}
