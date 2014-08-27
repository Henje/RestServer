package server.sun;

import java.io.IOException;

import server.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SunHttpHandler implements HttpHandler {
	private Handler handler;

	public SunHttpHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void handle(HttpExchange http) throws IOException {
		SunHttpRequest request = new SunHttpRequest(http);
		SunHttpResponse response = new SunHttpResponse(http);
		handler.handle(request, response);
		response.close();
	}

}
