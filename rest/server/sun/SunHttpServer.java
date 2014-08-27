package rest.server.sun;

import java.io.IOException;
import java.net.InetSocketAddress;

import rest.server.Handler;
import rest.server.Server;
import sun.net.httpserver.HttpServerImpl;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;


public class SunHttpServer implements Server {
	private HttpServer server;
	
	public SunHttpServer() throws IOException {
		server = HttpServerImpl.create();
	}

	@Override
	public boolean bind(String host, int port) {
		try {
			server.bind(new InetSocketAddress(host, port), 10);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void start() {
		server.start();
	}

	@Override
	public void stop() {
		server.stop(0);
	}

	@Override
	public void addHandler(Handler handler) {
		HttpContext context = server.createContext("/");
		context.setHandler(new SunHttpHandler(handler));
	}

}
