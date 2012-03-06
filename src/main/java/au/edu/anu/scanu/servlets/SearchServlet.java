package au.edu.anu.scanu.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

import au.edu.anu.scanu.fedora.LocalFedoraRepository;

/**
 * Servlet implementation class ProxyServlet
 */
@WebServlet(name = "SearchServlet", urlPatterns = "/search/search.do")
public final class SearchServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		StringBuilder sb = new StringBuilder();
		InputStream searchUrlInputStream;
		OutputStream searchUrlOutputStream;

		// Create the URL query string.
		Map<String, String[]> paramMap = request.getParameterMap();
		Set<Entry<String, String[]>> paramSet = paramMap.entrySet();
		Iterator<Entry<String, String[]>> paramIterator = paramSet.iterator();
		while (paramIterator.hasNext())
		{
			Entry<String, String[]> curParam = paramIterator.next();
			sb.append(curParam.getKey().toString());
			sb.append("=");
			sb.append(curParam.getValue()[0]);
			sb.append("&");
		}
		
	
		// Open connection and send POST request.
		HttpURLConnection connection = (HttpURLConnection) new URL(LocalFedoraRepository.getFedoraUrl() + "/risearch").openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Length", "" + sb.length());
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Accept-Charset", Charset.defaultCharset().name());
		StringBuilder credentials = new StringBuilder();
		credentials.append(LocalFedoraRepository.getFedoraUsername());
		credentials.append(":");
		credentials.append(LocalFedoraRepository.getFedoraPassword());
		connection.setRequestProperty("Authorization", "Basic " + Base64.encodeBase64String(credentials.toString().getBytes()));

		// Write the query.
		searchUrlOutputStream = connection.getOutputStream();
		searchUrlOutputStream.write(sb.toString().getBytes(Charset.defaultCharset()));
		
		// Get the response stream.
		searchUrlInputStream = connection.getInputStream();
		ServletOutputStream servletOutStream = response.getOutputStream();
		
		int bytesRead;
		byte[] bytes = new byte[10240];
		while ((bytesRead = searchUrlInputStream.read(bytes)) != -1)
		{
			servletOutStream.write(bytes, 0, bytesRead);
		}
		
		servletOutStream.flush();
		servletOutStream.close();
		searchUrlInputStream.close();
		searchUrlOutputStream.flush();
		searchUrlOutputStream.close();
	}

}
