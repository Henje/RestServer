package rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rest.server.Handler;
import rest.server.Request;
import rest.server.Response;
import rest.server.Server;
import rest.server.sun.SunHttpServer;

public class RestServer implements Handler {
	private static final Class<?>[] EXCEPTIONS = new Class<?>[] {FileNotFoundException.class, SecurityException.class};
	private static final int[] RESPONSE_CODES = new int[] {404, 403};
	private Server server;
	private List<ResourceHandler> resourceHandlers = new ArrayList<>();
	
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
	
	public void addResource(String prefix, Object resource) {
		if(resource.getClass().isAnnotationPresent(Resource.class)) {
			ResourceHandler handler = new ResourceHandler(prefix, resource);
			resourceHandlers.add(handler);
		} else {
			throw new IllegalArgumentException(resource.getClass().getCanonicalName());
		}
	}
	
	public void addResource(Object resource) {
		addResource("", resource);
	}

	@Override
	public void handle(Request request, Response response) {
		try {
			Object result = applyMethodToResources(request);
			response.setBody(result.toString());
			response.setResonseCode(200);
		} catch (Throwable e) {
			int responseCode = getResponseCodeForException(e);
			response.setResonseCode(responseCode);
		}
	}

	private Object applyMethodToResources(Request request) throws Throwable {
		for(Iterator<ResourceHandler> it = resourceHandlers.iterator(); it.hasNext();) {
			try {
				return it.next().applyMethod(request);
			} catch (FileNotFoundException e) {
			}
		}
		throw new FileNotFoundException(request.toString());
	}

	private int getResponseCodeForException(Throwable e) {
		for(int i = 0; i < EXCEPTIONS.length; i++) {
			if(EXCEPTIONS[i].isInstance(e))
				return RESPONSE_CODES[i];
		}
		return 500;
	}
}
