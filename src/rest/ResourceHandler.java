package rest;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import rest.method.DELETE;
import rest.method.GET;
import rest.method.POST;
import rest.method.PUT;
import rest.server.Request;

public class ResourceHandler {
	private static final Class<?>[] methods = new Class<?>[]{GET.class, POST.class, PUT.class, DELETE.class};
	private Map<RestRequest, Method> paths = new HashMap<RestRequest, Method>();
	private Object resource;
	private String prefix;

	public ResourceHandler(String prefix, Object resource) {
		this.prefix = prefix;
		this.resource = resource;
		findApplicableMethods();
	}

	private void findApplicableMethods() {
		Class<?> resourceClass = resource.getClass();
		for(Method m: resourceClass.getMethods()) {
			for(Class<?> clazz: methods) {
				@SuppressWarnings("unchecked")
				Class<? extends Annotation> httpMethodClass = (Class<? extends Annotation>) clazz;
				if(isApplicableMethod(m, httpMethodClass)) {
					addPath(m, httpMethodClass);
				}
			}
		}
	}

	private boolean isApplicableMethod(Method m, Class<? extends Annotation> httpMethodClass) {
		Class<?>[] parameters = m.getParameterTypes();
		if(!m.isAnnotationPresent(httpMethodClass))
			return false;
		if(!(parameters.length == 1 && parameters[0].equals(Arguments.class)))
			throw new IllegalArgumentException("Wrong method signature: "+resource.getClass().getSimpleName()+"."+m.getName());
		return true;
	}

	private void addPath(Method m, Class<? extends Annotation> httpMethodClass) {
		String httpMethod = httpMethodClass.getSimpleName();
		String path = prefix + getPathForMethod(m, httpMethodClass);
		RestRequest request = new RestRequest(httpMethod, path);
		paths.put(request, m);
	}

	private String getPathForMethod(Method m, Class<? extends Annotation> httpMethodClass) {
		Annotation annotation = m.getAnnotation(httpMethodClass);
		try {
			return (String) httpMethodClass.getMethod("path").invoke(annotation);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
		}
		return "";
	}
	
	public Object applyMethod(Request request) throws Throwable {
		for(Iterator<Entry<RestRequest,Method>> it = paths.entrySet().iterator(); it.hasNext();) {
			Entry<RestRequest,Method> entry = it.next();
			RestRequest restRequest = entry.getKey();
			Method resourceMethod = entry.getValue();
			if(restRequest.isRequestApplicable(request)) {
				return tryToInvoke(request, resourceMethod, restRequest);
			}
		}
		throw new FileNotFoundException(request.toString());
	}

	private Object tryToInvoke(Request request, Method resourceMethod, RestRequest restRequest) throws Throwable {
		try {
			Arguments args = restRequest.getPathArguments(request);
			return resourceMethod.invoke(resource, args);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new FileNotFoundException(request.toString());
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

}
