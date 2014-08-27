package rest;

import java.io.IOException;

import server.Handler;
import server.Request;
import server.Response;
import server.Server;
import server.sun.SunHttpServer;
//import javax.annotation.Resource;

public class RestServer implements Handler {
	private Server server;
	private ResourceHandler resource;
	
	public RestServer() throws IOException {
		this(new SunHttpServer());
	}
	
	public RestServer(Server server) {
		this.server = server;
		server.addHandler(this);
	}
	
	public void bind(String host, int port) {
		server.bind(host, port);
	}
	
	public void start() {
		server.start();
	}
	
	public void stop() {
		server.stop();
	}
	
	public void addResource(Class<?> resource) {
		if(resource == null) {
			throw new IllegalStateException();
		}
		if(resource.isAnnotationPresent(Resource.class)) {
			try {
				this.resource = new ResourceHandler(resource.getConstructor().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handle(Request request, Response response) {
		try {
			Object result = resource.applyMethod(request.getMethod(), request.getPath());
			response.setBody(result.toString());
			response.setResonseCode(200);
		} catch(IllegalArgumentException e) {
			response.setResonseCode(404);
		}
	}
}
