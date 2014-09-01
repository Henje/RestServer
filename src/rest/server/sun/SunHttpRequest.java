package rest.server.sun;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import rest.server.Request;

import com.sun.net.httpserver.HttpExchange;

public class SunHttpRequest implements Request {
	private Map<String, List<String>> header;
	private String method, path;
	private InputStream body;
	
	public SunHttpRequest(HttpExchange exchange) {
		header = exchange.getRequestHeaders();
		method = exchange.getRequestMethod();
		path = exchange.getRequestURI().getPath();
		body = exchange.getRequestBody();
	}

	@Override
	public List<String> getHeader(String name) {
		return header.get(name);
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public InputStream getBody() {
		return body;
	}

}
