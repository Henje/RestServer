import rest.RestServer;

public class Test {
	
	public static void main(String...args) {
		try {
			RestServer server = new RestServer();
			server.bind("127.0.0.1", 1337);
			server.addResource(TestResource.class);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
