package main;
import rest.Arguments;
import rest.Resource;
import rest.method.GET;


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
	
	@GET(path = "/secret")
	public String exceptionTest(Arguments args) {
		throw new SecurityException();
	}
}
