package au.edu.anu.scanu.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import au.edu.anu.scanu.fedora.LocalFedoraRepository;
import au.edu.anu.scanu.fedora.datastream.DublinCore;

/**
 * Servlet implementation class DublinCoreServlet
 */
@WebServlet(name = "DublinCoreServlet", urlPatterns = "/dc/dc.do")
public final class DublinCoreServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = null;

		if (request.getParameter("submit") == null)
		{
			response.sendRedirect(request.getContextPath() + "/dc/index.jsp");
		}
		else if (request.getParameter("submit").equalsIgnoreCase("extract"))
		{
			String pid = request.getParameter("pid");
			String dsId = request.getParameter("dsid");
			DublinCore extractedDc = null;

			if (pid == null || dsId == null)
				return;

			extractedDc = LocalFedoraRepository.transformToDc(pid, dsId);

			request.setAttribute("dc", extractedDc);
			dispatcher = request.getRequestDispatcher("/dc/index.jsp");
		}
		else if (request.getParameter("submit").equalsIgnoreCase("get"))
		{
			if (request.getParameter("pid") == null)
				response.sendRedirect(request.getContextPath() + "/dc/index.jsp");

			try
			{
				// Get datastream content.
				DublinCore dc = LocalFedoraRepository.getDc(request.getParameter("pid"));

				request.setAttribute("dc", dc);
				dispatcher = request.getRequestDispatcher("/dc/index.jsp");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (dispatcher != null)
			dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		String action = request.getParameter("submit");
		if (action == null)
			return;

		if (action.equalsIgnoreCase("save"))
		{
			// Create a DC object from the form field values.
			DublinCore dc = null;

			try
			{
				dc = new DublinCore(request.getParameter("dctext"));
			}
			catch (SAXException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Update DC element values.
			for (int iDcElement = 0; iDcElement < DublinCore.DC_ELEMENTS.length; iDcElement++)
			{
				if (request.getParameter(DublinCore.DC_ELEMENTS[iDcElement]) != null && !request.getParameter(DublinCore.DC_ELEMENTS[iDcElement]).equals(""))
					dc.setElement(DublinCore.DC_ELEMENTS[iDcElement], request.getParameter(DublinCore.DC_ELEMENTS[iDcElement]));
			}

			System.out.println("DC XML:");
			System.out.println(dc.toString());

			LocalFedoraRepository.saveDc(request.getParameter("pid"), dc);
		}

		// Redirect.
		try
		{
			response.sendRedirect(request.getContextPath() + "/dc/dc.do?submit=get&pid=" + request.getParameter("pid"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
