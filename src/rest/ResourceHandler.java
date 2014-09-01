package rest;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
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
				} else if(m.isAnnotationPresent(POST.class)) {
					RestRequest request = new RestRequest("POST", m.getAnnotation(GET.class).path());
					paths.put(request, m);
				} else if(m.isAnnotationPresent(PUT.class)) {
					RestRequest request = new RestRequest("PUT", m.getAnnotation(GET.class).path());
					paths.put(request, m);
				} else if(m.isAnnotationPresent(DELETE.class)) {
					RestRequest request = new RestRequest("DELETE", m.getAnnotation(GET.class).path());
					paths.put(request, m);
				}
			}
		}
	}
	
	public Object applyMethod(String method, String path) throws Throwable {
		for(Iterator<Entry<RestRequest,Method>> it = paths.entrySet().iterator(); it.hasNext();) {
			Entry<RestRequest,Method> entry = it.next();
			RestRequest request = entry.getKey();
			if(request.isRequestApplicable(method, path)) {
				try {
					return entry.getValue().invoke(resource, request.getPathArguments(path));
				} catch (IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					throw e.getCause();
				}
			}
		}
		throw new FileNotFoundException(method+" "+path);
	}

}
