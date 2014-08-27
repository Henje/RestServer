package rest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceHandler {
	private Map<RestRequest, Method> paths = new HashMap<RestRequest, Method>();
	private Object resource;

	public ResourceHandler(Object resource) {
		this.resource = resource;
		findApplicableMethods(resource.getClass());
	}

	private void findApplicableMethods(Class<? extends Object> resourceClass) {
		for(Method m: resourceClass.getMethods()) {
			Class<?>[] parameters = m.getParameterTypes();
			if(parameters.length == 1 && parameters[0].equals(Arguments.class)) {
				if(m.isAnnotationPresent(GET.class)) {
					RestRequest request = new RestRequest("GET", m.getAnnotation(GET.class).path());
					paths.put(request, m);
				}
			}
		}
	}
	
	public Object applyMethod(String method, String path) {
		for(Iterator<Entry<RestRequest,Method>> it = paths.entrySet().iterator(); it.hasNext();) {
			Entry<RestRequest,Method> entry = it.next();
			RestRequest request = entry.getKey();
			if(request.isRequestApplicable(method, path)) {
				try {
					return entry.getValue().invoke(resource, request.getPathArguments(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		throw new IllegalArgumentException(method+" "+path);
	}

}
