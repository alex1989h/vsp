package impl.http.server;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

public class ServerTest {

	@Test
	public void whenPostRequestUsingHttpClient_thenCorrect() 
	  throws ClientProtocolException, IOException {
	    CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost("http://127.0.0.1:8000/");
	 
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("username", "John"));
	    params.add(new BasicNameValuePair("password", "pass"));
	    httpPost.setEntity(new UrlEncodedFormEntity(params));
	 
	    CloseableHttpResponse response = client.execute(httpPost);
	    StatusLine sl = response.getStatusLine();
	    System.out.println(sl.getProtocolVersion()+" "+sl.getStatusCode()+" "+sl.getReasonPhrase());
	    InputStream is = response.getEntity().getContent();
	    int ln = is.available();
	    byte[] bt = new byte[ln];
	    is.read(bt);
	    String msg = new String(bt);
	    System.out.println(response.getEntity().getContentLength());
	    System.out.println("Content-Length: "+ln);
	    System.out.println(msg);
	    
	    client.close();
	}

}
