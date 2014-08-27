package rest.server;

import java.util.List;

public interface Response {
	public void setHeader(String name, List<String> value);
	public void setResonseCode(int code);
	public void setBody(String body);
}
