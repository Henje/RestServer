package rest;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ResourceHandler {
	private static final Class<?>[] methods = new Class<?>[]{GET.class, POST.class, PUT.class, DELETE.class};
	private Map<RestRequest, Method> paths = new HashMap<RestRequest, Method>();
	private Object resource;

	public ResourceHandler(Object resource) {
		this.resource = resource;
		findApplicableMethods(resource.getClass());
	}

	private void findApplicableMethods(Class<? extends Object> resourceClass) {
		for(Method m: resourceClass.getMethods()) {
			Class<?>[] parameters = m.getParameterTypes();
			for(Class<?> clazz: methods) {
				@SuppressWarnings("unchecked")
				Class<? extends Annotation> methodClass = (Class<? extends Annotation>) clazz;
				if(m.isAnnotationPresent(methodClass)) {
					if(parameters.length == 1 && parameters[0].equals(Arguments.class)) {
						RestRequest request = new RestRequest(methodClass.getSimpleName(), ((GET) m.getAnnotation(methodClass)).path());
						paths.put(request, m);
					} else {
						throw new IllegalArgumentException(m.getName());
					}
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
