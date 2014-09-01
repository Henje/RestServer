package rest.server;

import java.io.InputStream;
import java.util.List;

public interface Request {
	public List<String> getHeader(String name);
	public String getMethod();
	public String getPath();
	public InputStream getBody();
}
