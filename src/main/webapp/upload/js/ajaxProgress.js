/**
 * 
 */

var ajaxRequest;

function addCommas(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1))
	{
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

function ajaxFunction()
{
	// var servletUrl = "/FileUpload/FileUploadServlet";
	var servletUrl = contextPath + "upload.do";

	if (window.XMLHttpRequest)
	{
		ajaxRequest = new XMLHttpRequest();
		ajaxRequest.onreadystatechange = processStateChange;

		try
		{
			ajaxRequest.open("GET", servletUrl, true);
		}
		catch (e)
		{
			alert("url" + e);
		}
		ajaxRequest.send();
	}
	else if (window.ActiveXObject)
	{
		ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");

		if (ajaxRequest)
		{
			ajaxRequest.onreadystatechange = processStateChange;
			ajaxRequest.open("GET", servletUrl, true);
			ajaxRequest.send();
		}
	}
}

function processStateChange()
{
	if (ajaxRequest.readyState == 4 && ajaxRequest.status == 200)
	{
		var xml = ajaxRequest.responseXML;
		var isNotFinished = xml.getElementsByTagName("finished")[0];
		var myBytesRead = xml.getElementsByTagName("bytes_read")[0];
		var myContentLength = xml.getElementsByTagName("content_length")[0];
		var myPercent = xml.getElementsByTagName("percent_complete")[0];

		if ((isNotFinished == null) && (myPercent == null))
		{
			document.getElementById("statusText").innerHTML = "Initialising...";
			window.setTimeout("ajaxFunction();", 200);
		}
		else
		{
			myBytesRead = myBytesRead.firstChild.data;
			myContentLength = myContentLength.firstChild.data;

			if (myPercent == null)
				myPercent = "100";
			else
				myPercent = myPercent.firstChild.data;

			document.getElementById("progressBarFill").style.width = myPercent + "%";
			document.getElementById("statusText").innerHTML = myPercent + "%. " + addCommas(Math.round(myBytesRead / 1024)) + " KB of "
					+ addCommas(Math.round(myContentLength / 1024)) + "KB.";

			if (myPercent < 100)
				window.setTimeout("ajaxFunction();", 200);
		}
	}
	/*
	else
	{
		console.log("Ready State: " + ajaxRequest.readyState);
		console.log("Status: " + ajaxRequest.status);
		window.setTimeout("ajaxFunction();", 200);
	}
	*/
}
