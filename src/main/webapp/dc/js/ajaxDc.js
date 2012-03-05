/**
 * 
 */

function ajaxDcRequest(pid)
{
	var servletURL = contextPath + "/dc/dc.do";
	servletURL += "?req=get";
	servletURL += "&pid=" + pid;

	var ajaxReq = null;

	if (window.XMLHttpRequest)
		ajaxReq = new XMLHttpRequest();
	else if (window.ActiveXObject)
		ajaxReq = new ActiveXObject("Microsoft.XMLHTTP");

	if (ajaxReq != null)
	{
		ajaxReq.onreadystatechange = function()
		{
			if (ajaxReq.readyState == 4 && ajaxReq.status == 200)
			{
				// Read the XML response from the AJAX request.
				var respXml = ajaxReq.responseXML;
				var dcNs = "http://purl.org/dc/elements/1.1/";

				console.log(respXml.getElementsByTagNameNS(dcNs, "title")[0].textContent);
				var title = respXml.getElementsByTagNameNS(dcNs, "title")[0];
				if (title != null)
					document.getElementById("id_title").value = title.textContent;
			}
		};

		ajaxReq.open("GET", servletURL, true);
		ajaxReq.send();
	}
}
