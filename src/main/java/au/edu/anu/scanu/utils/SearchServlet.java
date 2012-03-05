package au.edu.anu.scanu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		sb = sb.append("type=tuples&lang=sparql&format=Sparql&limit=1000&dt=on&query=PREFIX++dc%3A++%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2F%3E%0D%0ASELECT+%3Fitem+%3Ftitle+%3Fdescription%0D%0AWHERE%0D%0A%7B%0D%0A%3Fitem+dc%3Atitle+%3Ftitle%0D%0AOPTIONAL+%7B%3Fitem+dc%3Acreator+%3Fcreator%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Adescription+%3Fdescription%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Asubject+%3Fsubject%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Apublisher+%3Fpublisher%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Acontributor+%3Fcontributor%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Adate+%3Fdate%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Atype+%3Ftype%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Aformat+%3Fformat%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Aidentifier+%3Fidentifier%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Asource+%3Fsource%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Alanguage+%3Flanguage%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Arelation+%3Frelation%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Acoverage+%3Fcoverage%7D%0D%0AOPTIONAL+%7B%3Fitem+dc%3Arights+%3Frights%7D%0D%0A%0D%0AFILTER%0D%0A%28%0D%0A%28%0D%0Aregex%28%3Ftitle%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fcreator%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fsubject%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fdescription%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fpublisher%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fcontributor%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fdate%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Ftype%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fformat%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fidentifier%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fsource%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Flanguage%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Frelation%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Fcoverage%2C+%22condition%22%2C+%22i%22%29%0D%0A%7C%7C+regex%28%3Frights%2C+%22condition%22%2C+%22i%22%29%0D%0A%29%0D%0A%29%0D%0A%7D");

		// Open connection and send POST request.
		HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/fedora/risearch").openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Length", "" + sb.length());
		
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Accept-Charset", Charset.defaultCharset().name());

		OutputStream searchUrlOutputStream = null;
		searchUrlOutputStream = connection.getOutputStream();
		searchUrlOutputStream.write(sb.toString().getBytes(Charset.defaultCharset()));
		InputStream searchUrlInputStream = connection.getInputStream();

		ServletOutputStream servletOutStream = response.getOutputStream();
		
		int bytesRead;
		byte[] bytes = new byte[10240];
		while ((bytesRead = searchUrlInputStream.read(bytes)) != -1)
		{
			servletOutStream.write(bytes, 0, bytesRead);
			log.info(bytes.toString());
		}
		
		servletOutStream.flush();
		servletOutStream.close();
		searchUrlInputStream.close();
		searchUrlOutputStream.flush();
		searchUrlOutputStream.close();
	}

}
