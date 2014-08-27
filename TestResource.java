import rest.Arguments;
import rest.GET;
import rest.Resource;


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
