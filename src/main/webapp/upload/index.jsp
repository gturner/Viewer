<!-- Folder: /upload -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>File Upload</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() + "/"%>upload/css/fileUploadProgressBar.css"></link>
<script type="text/javascript" src="<%=request.getContextPath() + "/"%>upload/js/ajaxProgress.js"></script>
<script type="text/javascript">
	var contextPath = "<%=request.getContextPath() + "/"%>";
	console.log(contextPath);
</script>
</head>
<body>
	<h1>Upload to Holding folder</h1>
	<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" style="display: none;"></iframe>
	<form id="myForm" enctype="multipart/form-data" method="post" target="uploadFrame" action="<%=request.getContextPath() + "/"%>upload.do"
		onsubmit="ajaxFunction()">
		<input type="file" name="txtFile" id="txtFile" size="50" />&nbsp;&nbsp;<input type="submit" id="submitID" name="submit" value="Upload" />
	</form>

	<div class="progressBar" id="progressBar">
		<div class="progressBarFill" id="progressBarFill" style="width: 1%;"></div>
	</div>
	&nbsp;
	<div class="statusText" id="statusText">&nbsp;</div>
</body>
</html>