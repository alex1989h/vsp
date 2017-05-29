package impl.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/", new MyHandler());
		server.start();
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "This is the response";
			InputStream is = t.getRequestBody();

			int ln = is.available();
			byte[] bt = new byte[ln];
			is.read(bt);
			String msg = new String(bt);
			System.out.println("Param: " + t.getRequestURI().getRawQuery());
			System.out.println("Type: " + t.getRequestMethod());
			System.out.println("Length: " + ln);
			System.out.println(msg);

			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}

		void method(String string) {
			String[] param = string.split("\\.");
			for (int i = 0; i < param.length; i++) {
				if (param[i].equals("method")) {
					switch (param[i + 1]) {
					case "gripper":
						if(param[i+2].equals("open")){
							
						}else if(param[i+2].equals("close")){
							
						}
						break;
					case "horizontal":
						
						break;
					case "vertical":

						break;
					default:
						break;
					}
				}
			}
		}
	}

}