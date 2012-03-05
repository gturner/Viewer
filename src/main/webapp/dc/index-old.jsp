<!-- Folder /dc -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="<%=request.getContextPath()%>/dc/js/ajaxDc.js"></script>
<script type="text/javascript">
	contextPath = "<%=request.getContextPath()%>";
</script>
<title>Dublin Core</title>
</head>
<body>
	<h1>Dublin Core</h1>
	<form>
		<label for="id_pid">Pid: </label> <input id="id_pid" name="pid" type="text" size="40" /> <input name="request" type="button" value="Retrieve DC"
			onclick="ajaxDcRequest(document.getElementById('id_pid').value)" />
	</form>
	<hr />
	<form>
		<table>
			<tr>
				<td><label for="id_title">Title</label></td>
				<td><input type="text" size="40" name="title" id="id_title" /></td>
			</tr>

			<tr>
				<td><label for="id_creator">Creator</label></td>
				<td><input type="text" size="40" name="creator" id="id_creator" /></td>
			</tr>

			<tr>
				<td><label for="id_subject">Subject</label></td>
				<td><input type="text" size="40" name="subject" id="id_subject" /></td>
			</tr>

			<tr>
				<td><label for="id_description">Description</label></td>
				<td><input type="text" size="40" name="description" id="id_description" /></td>
			</tr>

			<tr>
				<td><label for="id_publisher">Publisher</label></td>
				<td><input type="text" size="40" name="publisher" id="id_publisher" /></td>
			</tr>

			<tr>
				<td><label for="id_contributor">Contributor</label></td>
				<td><input type="text" size="40" name="contributor" id="id_contributor" /></td>
			</tr>

			<tr>
				<td><label for="id_date">Date</label></td>
				<td><input type="text" size="40" name="date" id="id_date" /></td>
			</tr>

			<tr>
				<td><label for="id_type">Type</label></td>
				<td><input type="text" size="40" name="type" id="id_type" /></td>
			</tr>

			<tr>
				<td><label for="id_format">Format</label></td>
				<td><input type="text" size="40" name="format" id="id_format" /></td>
			</tr>

			<tr>
				<td><label for="id_identifier">Identifier</label></td>
				<td><input type="text" size="40" name="identifier" id="id_identifier" /></td>
			</tr>

			<tr>
				<td><label for="id_source">Source</label></td>
				<td><input type="text" size="40" name="source" id="id_source" /></td>
			</tr>

			<tr>
				<td><label for="id_language">Language</label></td>
				<td><input type="text" size="40" name="language" id="id_language" /></td>
			</tr>

			<tr>
				<td><label for="id_relation">Relation</label></td>
				<td><input type="text" size="40" name="relation" id="id_relation" /></td>
			</tr>

			<tr>
				<td><label for="id_coverage">Coverage</label></td>
				<td><input type="text" size="40" name="coverage" id="id_coverage" /></td>
			</tr>

			<tr>
				<td><label for="id_rights">Rights</label></td>
				<td><input type="text" size="40" name="rights" id="id_rights" /></td>
			</tr>

			<tr>
				<td>&nbsp;</td>
				<td><input type="reset" value="Reset" /></td>
			</tr>

		</table>
	</form>
</body>
</html>