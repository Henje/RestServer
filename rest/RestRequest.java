package rest;

import java.util.ArrayList;
import java.util.List;

public class RestRequest {
	private String method;
	private List<PathPart> path = new ArrayList<PathPart>();
	
	public RestRequest(String method, String path) {
		this.method = method;
		parsePath(path);
	}

	private void parsePath(String path) {
		String[] parts = path.split("/");
		for(String part: parts) {
			PathPart pathPart = new PathPart(part, false);
			if(part.startsWith(":")) {
				pathPart = new PathPart(part.substring(1), true);
			}
			this.path.add(pathPart);
		}
	}

	public boolean isRequestApplicable(String method, String path) {
		return this.method.equalsIgnoreCase(method) && checkPath(path);
	}

	private boolean checkPath(String path) {
		String[] parts = path.split("/");
		if(parts.length != this.path.size())
			return false;
		for(int i = 0; i < parts.length; i++) {
			PathPart pathPart = this.path.get(i);
			if(!pathPart.isVariable && !pathPart.name.equals(parts[i]))
				return false;
		}
		return true;
	}
	
	
	public Arguments getPathArguments(String path) {
		Arguments args = new Arguments();
		String[] parts = path.split("/");
		for(int i = 0; i < parts.length; i++) {
			PathPart pathPart = this.path.get(i);
			if(pathPart.isVariable) {
				args.set(pathPart.name, parts[i]);
			}
		}
		return args;
	}
}
