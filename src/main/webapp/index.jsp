<!-- Folder: / -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<body>
	<h2>Hello World!</h2>
	<br />
	<a href="pageController?layout=form&type=tmplt:1">Input Projects</a>
	<br />
	<a href="pageController?layout=form&item=test:25">Input Data Project</a>
	<br />
	<a href="resources/appletupload.html">Upload Data</a>
	<br />

	<ul>
		<li><a href="<%=request.getContextPath()%>/search/">Search</a></li>
		<li><a href="<%=request.getContextPath()%>/upload/">File Upload</a></li>
		<li><a href="<%=request.getContextPath()%>/dc/">Dublin Core</a></li>
	</ul>
	<h4>Debug Information:</h4>
	<table border="1" cellpadding="3" cellspacing="0">
		<tr>
			<th>Method</th>
			<th>Result</th>
		</tr>
		<tr>
			<td>request.getContextPath()</td>
			<td><%=request.getContextPath()%></td>
		</tr>
		<tr>
			<td>request.getServletPath()</td>
			<td><%=request.getServletPath()%></td>
		</tr>
		<tr>
			<td>session.getServletContext().getRealPath(request.getContextPath())</td>
			<td><%=session.getServletContext().getRealPath("/")%></td>
		</tr>
	</table>
</body>
</html>
