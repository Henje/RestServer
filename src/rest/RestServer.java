package rest;

import java.io.FileNotFoundException;
import java.io.IOException;

import rest.server.Handler;
import rest.server.Request;
import rest.server.Response;
import rest.server.Server;
import rest.server.sun.SunHttpServer;

public class RestServer implements Handler {
	private static final Class<?>[] EXCEPTIONS = new Class<?>[] {FileNotFoundException.class, SecurityException.class};
	private static final int[] RESPONSE_CODES = new int[] {404, 403};
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
			throw new NullPointerException();
		}
		if(resource.isAnnotationPresent(Resource.class)) {
			try {
				this.resource = new ResourceHandler(resource.getConstructor().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException(resource.getCanonicalName());
		}
	}

	@Override
	public void handle(Request request, Response response) {
		try {
			Object result = resource.applyMethod(request.getMethod(), request.getPath());
			response.setBody(result.toString());
			response.setResonseCode(200);
		} catch (Throwable e) {
			int responseCode = getResponseCodeForException(e);
			response.setResonseCode(responseCode);
		}
	}

	private int getResponseCodeForException(Throwable e) {
		for(int i = 0; i < EXCEPTIONS.length; i++) {
			if(EXCEPTIONS[i].isInstance(e))
				return RESPONSE_CODES[i];
		}
		return 500;
	}
}
