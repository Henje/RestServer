package rest;

import java.util.HashMap;
import java.util.Map;

public class Arguments {
	private Map<String,String> arguments = new HashMap<String,String>();
	
	public void set(String name, String value) {
		arguments.put(name, value);
	}
	
	public String get(String name) {
		return arguments.get(name);
	}
}
