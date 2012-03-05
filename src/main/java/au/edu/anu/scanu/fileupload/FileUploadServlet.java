package au.edu.anu.scanu.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class FileUploadServlet
 */
public class FileUploadServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private final Logger log = Logger.getLogger(this.getClass().getName());

	private FileUploadListener listener;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		FileUploadListener listener = null;
		StringBuffer xml = new StringBuffer();
		long bytesRead = 0L;
		long contentLength = 0L;

		if (session == null)
		{
			return;
		}
		else if (session != null)
		{
			log.info("Session ID in Get: " + session.getId());
			listener = (FileUploadListener) session.getAttribute("LISTENER");

			if (listener == null)
			{
				return;
			}
			else
			{
				bytesRead = listener.getBytesRead();
				contentLength = listener.getContentLength();
			}
		}

		response.setContentType("text/xml");

		xml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		xml.append("<response>\n");
		xml.append("\t<bytes_read>" + bytesRead + "</bytes_read>\n");
		xml.append("\t<content_length>" + contentLength + "</content_length>\n");

		if (bytesRead == contentLength)
		{
			xml.append("\t<finished />\n");
			session.setAttribute("LISTENER", null);
		}
		else
		{
			long percentComplete = ((100 * bytesRead) / contentLength);
			xml.append("\t<percent_complete>" + String.valueOf(percentComplete) + "</percent_complete>\n");
		}

		xml.append("</response>\n");

		out.println(xml.toString());
		out.flush();
		out.close();
	}

	/**
	 * Called when a file is uploaded using form post method.
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Properties
		Properties uploadProps = new Properties();
		uploadProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("FileUpload.properties"));

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Set factory constraints
		factory.setSizeThreshold(1024 * 1024);	// 1 MB.
		factory.setRepository(new File(uploadProps.getProperty("TempDir")));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		listener = new FileUploadListener();
		HttpSession session = request.getSession();
		session.setAttribute("LISTENER", listener);
		upload.setProgressListener(listener);

		List<FileItem> uploadedItems = null;
		FileItem fileItem = null;
		String filePath = uploadProps.getProperty("UploadDir");

		log.info(request.getContextPath());

		try
		{
			uploadedItems = (List<FileItem>) upload.parseRequest(request);
			Iterator<FileItem> i = uploadedItems.iterator();

			log.info("Session ID in Post: " + session.getId());

			while (i.hasNext())
			{
				fileItem = (FileItem) i.next();
				if (fileItem.isFormField() == false)
				{
					if (fileItem.getSize() > 0)
					{
						File uploadedFile = null;
						String myFullFileName = fileItem.getName();
						String myFileName = "";
						String slashType = (myFullFileName.lastIndexOf("\\") > 0) ? "\\" : "/";
						int startIndex = myFullFileName.lastIndexOf(slashType);

						myFileName = myFullFileName.substring(startIndex + 1, myFullFileName.length());
						uploadedFile = new File(filePath, myFileName);
						log.info("Writing file...");
						fileItem.write(uploadedFile);
						log.info("Done.");
					}
				}
			}
		}
		catch (FileUploadException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
