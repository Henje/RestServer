package server;


public interface Server {
	public boolean bind(String host, int port);
	public void start();
	public void stop();
	public void addHandler(Handler handler);
}
