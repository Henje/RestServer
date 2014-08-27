package rest;

public class PathPart {
	public boolean isVariable;
	public String name;
	
	public PathPart(String name, boolean isVariable) {
		this.name = name;
		this.isVariable = isVariable;
	}
}
