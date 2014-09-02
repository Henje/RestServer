package rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import rest.method.GET;

@Resource
public class FileResource {
	private File folder;
	
	public FileResource(File folder) {
		this.folder = folder;
	}
	
	@GET(path = "/")
	public String index(Arguments args) throws IOException {
		args.set("fileName", "index.html");
		return getFile(args);
	}
	
	@GET(path = "/:fileName")
	public String getFile(Arguments args) throws IOException {
		String fileName = args.get("fileName");
		File requestedFile = new File(folder, fileName);
		return getContents(requestedFile);
	}

	private String getContents(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuilder builder = new StringBuilder();
		while(reader.ready()) {
			builder.append(reader.readLine());
		}
		reader.close();
		return builder.toString();
	}
}
