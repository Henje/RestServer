package main;
import rest.RestServer;

public class Test {
	
	public static void main(String...args) {
		RestServer server = null;
		try {
			server = new RestServer();
			server.bind("127.0.0.1", 1337);
			server.addResource(new TestResource());
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			server.stop();
		}
	}

}
