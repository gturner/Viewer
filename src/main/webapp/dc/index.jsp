<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="au.edu.anu.scanu.fedora.datastream.DublinCore"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Extract Dublin Core</title>
</head>
<body>
	<form method="get" action="<%=request.getContextPath()%>/dc/dc.do">
		<table>
			<tr>
				<td><label for="idPid">Pid: </label></td>
				<td><input type="text" name="pid" id="idPid" size="30" value="<%=request.getParameter("pid") == null ? "" : request.getParameter("pid")%>" /></td>
			</tr>
			<tr>
				<td><label for="idDsid">Datastream ID: </label></td>
				<td><input type="text" name="dsid" id="idDsid" size="30" value="<%=request.getParameter("dsid") == null ? "" : request.getParameter("dsid")%>" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="submit" value="Get" />&nbsp;<input type="submit" name="submit" value="Extract"></td>
			</tr>
		</table>
	</form>
	<%
		DublinCore dc = (DublinCore) request.getAttribute("dc");
		if (dc != null)
		{
	%>
	<form method="post" action="<%=request.getContextPath()%>/dc/dc.do">
		<input name="pid" type="hidden" value="<%=request.getParameter("pid") == null ? "" : request.getParameter("pid")%>" />
		<hr />
		<textarea rows="10" cols="100" name="dctext"><%=dc.toString()%></textarea>
		<hr />
		<table>
			<tr>
				<td><label for="idTitle">Title</label></td>
				<td><textarea rows="5" cols="100" name="title" id="idTitle"><%=dc.getElement(DublinCore.ELEMENT_TITLE) == null ? "" : dc.getElement(DublinCore.ELEMENT_TITLE)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idCreator">Creator</label></td>
				<td><textarea rows="5" cols="100" name="creator" id="idCreator"><%=dc.getElement(DublinCore.ELEMENT_CREATOR) == null ? "" : dc.getElement(DublinCore.ELEMENT_CREATOR)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idSubject">Subject</label></td>
				<td><textarea rows="5" cols="100" name="subject" id="idSubject"><%=dc.getElement(DublinCore.ELEMENT_SUBJECT) == null ? "" : dc.getElement(DublinCore.ELEMENT_SUBJECT)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idDescription">Description</label></td>
				<td><textarea rows="5" cols="100" name="description" id="idDescription"><%=dc.getElement(DublinCore.ELEMENT_DESCRIPTION) == null ? "" : dc.getElement(DublinCore.ELEMENT_DESCRIPTION)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idPublisher">Publisher</label></td>
				<td><textarea rows="5" cols="100" name="publisher" id="idPublisher"><%=dc.getElement(DublinCore.ELEMENT_PUBLISHER) == null ? "" : dc.getElement(DublinCore.ELEMENT_PUBLISHER)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idContributor">Contributor</label></td>
				<td><textarea rows="5" cols="100" name="contributor" id="idContributor"><%=dc.getElement(DublinCore.ELEMENT_CONTRIBUTOR) == null ? "" : dc.getElement(DublinCore.ELEMENT_CONTRIBUTOR)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idDate">Date</label></td>
				<td><textarea rows="5" cols="100" name="date" id="idDate"><%=dc.getElement(DublinCore.ELEMENT_DATE) == null ? "" : dc.getElement(DublinCore.ELEMENT_DATE)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idType">Type</label></td>
				<td><textarea rows="5" cols="100" name="type" id="idType"><%=dc.getElement(DublinCore.ELEMENT_TYPE) == null ? "" : dc.getElement(DublinCore.ELEMENT_TYPE)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idFormat">Format</label></td>
				<td><textarea rows="5" cols="100" name="format" id="idFormat"><%=dc.getElement(DublinCore.ELEMENT_FORMAT) == null ? "" : dc.getElement(DublinCore.ELEMENT_FORMAT)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idIdentifier">Identifier</label></td>
				<td><textarea rows="5" cols="100" name="identifier" id="idIdentifier"><%=dc.getElement(DublinCore.ELEMENT_IDENTIFIER) == null ? "" : dc.getElement(DublinCore.ELEMENT_IDENTIFIER)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idSource">Source</label></td>
				<td><textarea rows="5" cols="100" name="source" id="idSource"><%=dc.getElement(DublinCore.ELEMENT_SOURCE) == null ? "" : dc.getElement(DublinCore.ELEMENT_SOURCE)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idLanguage">Language</label></td>
				<td><textarea rows="5" cols="100" name="language" id="idLanguage"><%=dc.getElement(DublinCore.ELEMENT_LANGUAGE) == null ? "" : dc.getElement(DublinCore.ELEMENT_LANGUAGE)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idRelation">Relation</label></td>
				<td><textarea rows="5" cols="100" name="relation" id="idRelation"><%=dc.getElement(DublinCore.ELEMENT_RELATION) == null ? "" : dc.getElement(DublinCore.ELEMENT_RELATION)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idCoverage">Coverage</label></td>
				<td><textarea rows="5" cols="100" name="coverage" id="idCoverage"><%=dc.getElement(DublinCore.ELEMENT_COVERAGE) == null ? "" : dc.getElement(DublinCore.ELEMENT_COVERAGE)%></textarea></td>
			</tr>

			<tr>
				<td><label for="idRights">Rights</label></td>
				<td><textarea rows="5" cols="100" name="rights" id="idRights"><%=dc.getElement(DublinCore.ELEMENT_RIGHTS) == null ? "" : dc.getElement(DublinCore.ELEMENT_RIGHTS)%></textarea></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="submit" value="Save" />
			</tr>
		</table>
	</form>
	<%
		}
	%>
</body>
</html>