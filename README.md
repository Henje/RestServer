RestServer
==========

Very simple implementation of a REST framework. I created this because I just wanted a HTTP server without any XML configuration.

Usage
=====

To initialize the server just this is required:

```Java
try {
	RestServer server = new RestServer();
	server.bind("127.0.0.1", 1337);
	server.addResource(new TestResource());
	server.start();
} catch (Exception e) {
	e.printStackTrace();
	server.stop();
}
```

And the resource is a simple Java object with an annotation for the routing:

```Java
@Resource
public class TestResource {
	@GET(path = "/")
	public String index(Arguments args) {
		return "Hello World!";
	}
	
	@GET(path = "/test/:name")
	public String test(Arguments args) {
		return "Hello " + args.get("name") + "!";
	}
}
```
