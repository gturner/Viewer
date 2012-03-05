function generateQueryStr()
{
	var queryStr = "";

	// Title.
	if (document.getElementById("idTitleTerms").value != "")
	{
		if (queryStr.length > 0)
			queryStr += "&";

		queryStr += "title";
		queryStr += document.getElementById("id_title_operator").options[document.getElementById("id_title_operator").selectedIndex].value;
		if (document.getElementById("idTitleTerms").value.indexOf(" ") != -1)
		{
			queryStr += "'";
			queryStr += document.getElementById("idTitleTerms").value;
			queryStr += "'";
		}
		else
		{
			queryStr += document.getElementById("idTitleTerms").value;
		}
	}

	document.getElementById("idQuery").value = queryStr;

	return true;
}

/**
 * Generates the SPARQL query from the basic search terms specified.
 * 
 * @returns {Boolean} If the query's unable to be generated, false is returned so the form calling this function does not proceed with the submit.
 */
function generateBasicQuery()
{
	/*
	 * SPARQL Query used:
	 *  
	PREFIX  dc:  <http://purl.org/dc/elements/1.1/>
	SELECT ?item ?title ?description
	WHERE
	{
	?item dc:title ?title
	OPTIONAL {?item dc:creator ?creator}
	OPTIONAL {?item dc:description ?description}
	OPTIONAL {?item dc:subject ?subject}
	OPTIONAL {?item dc:publisher ?publisher}
	OPTIONAL {?item dc:contributor ?contributor}
	OPTIONAL {?item dc:date ?date}
	OPTIONAL {?item dc:type ?type}
	OPTIONAL {?item dc:format ?format}
	OPTIONAL {?item dc:identifier ?identifier}
	OPTIONAL {?item dc:source ?source}
	OPTIONAL {?item dc:language ?language}
	OPTIONAL {?item dc:relation ?relation}
	OPTIONAL {?item dc:coverage ?coverage}
	OPTIONAL {?item dc:rights ?rights}

	FILTER
	(
	(
	regex(?title, "term1", "i")
	|| regex(?creator, "term1", "i")
	|| regex(?subject, "term1", "i")
	|| regex(?description, "term1", "i")
	|| regex(?publisher, "term1", "i")
	|| regex(?contributor, "term1", "i")
	|| regex(?date, "term1", "i")
	|| regex(?type, "term1", "i")
	|| regex(?format, "term1", "i")
	|| regex(?identifier, "term1", "i")
	|| regex(?source, "term1", "i")
	|| regex(?language, "term1", "i")
	|| regex(?relation, "term1", "i")
	|| regex(?coverage, "term1", "i")
	|| regex(?rights, "term1", "i")
	)
	&&
	(
	regex(?title, "term2", "i")
	|| regex(?creator, "term2", "i")
	|| regex(?subject, "term2", "i")
	|| regex(?description, "term2", "i")
	|| regex(?publisher, "term2", "i")
	|| regex(?contributor, "term2", "i")
	|| regex(?date, "term2", "i")
	|| regex(?type, "term2", "i")
	|| regex(?format, "term2", "i")
	|| regex(?identifier, "term2", "i")
	|| regex(?source, "term2", "i")
	|| regex(?language, "term2", "i")
	|| regex(?relation, "term2", "i")
	|| regex(?coverage, "term2", "i")
	|| regex(?rights, "term2", "i")
	)
	)
	}
	 */

	var searchTerms = document.getElementById("idBasicSearchTerms").value;

	/* Split the search text into phrases. E.g.:
	 * first second "third fourth" fifth "sixth seventh" will result in:
	 * "first", "second", ""third fourth"", "fifth", ""sixth seventh""
	 * Then remove double quotes from the multi-word phases to get:
	 * "first", "second", "third fourth", "fifth", "sixth seventh"
	*/
	var searchPhrases = searchTerms.match(/("[^"]+"|[^"\s]+)/g);
	for ( var i = 0; i < searchPhrases.length; i++)
	{
		searchPhrases[i] = searchPhrases[i].replace(/^"([^"]+)"$/, "$1");
	}

	// Generate the query.
	var query = "";

	query += "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>\r\n";
	query += "SELECT ?item ?title ?description\r\n";
	query += "WHERE\r\n";
	query += "{\r\n";
	query += "?item dc:title ?title\r\n";
	query += "OPTIONAL {?item dc:creator ?creator}\r\n";
	query += "OPTIONAL {?item dc:description ?description}\r\n";
	query += "OPTIONAL {?item dc:subject ?subject}\r\n";
	query += "OPTIONAL {?item dc:publisher ?publisher}\r\n";
	query += "OPTIONAL {?item dc:contributor ?contributor}\r\n";
	query += "OPTIONAL {?item dc:date ?date}\r\n";
	query += "OPTIONAL {?item dc:type ?type}\r\n";
	query += "OPTIONAL {?item dc:format ?format}\r\n";
	query += "OPTIONAL {?item dc:identifier ?identifier}\r\n";
	query += "OPTIONAL {?item dc:source ?source}\r\n";
	query += "OPTIONAL {?item dc:language ?language}\r\n";
	query += "OPTIONAL {?item dc:relation ?relation}\r\n";
	query += "OPTIONAL {?item dc:coverage ?coverage}\r\n";
	query += "OPTIONAL {?item dc:rights ?rights}\r\n";
	query += "\r\n";
	query += "FILTER\r\n";
	query += "(\r\n";

	for (i = 0; i < searchPhrases.length; i++)
	{
		if (i > 0)
		{
			if (searchPhrases[i] == "OR")
			{
				query += "||\r\n";
				i++;
			}
			else if (searchPhrases[i] == "AND")
			{
				query += "&&\r\n";
				i++;
			}
			else
			{
				query += "&&\r\n";
			}
		}

		query += "(\r\n";
		query += "regex(?title, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?creator, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?subject, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?description, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?publisher, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?contributor, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?date, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?type, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?format, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?identifier, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?source, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?language, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?relation, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?coverage, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += "|| regex(?rights, \"" + searchPhrases[i] + "\", \"i\")\r\n";
		query += ")\r\n";
	}

	query += ")\r\n";
	query += "}\r\n";

	console.log(query);

	document.frmBasicSearch.query.value = query;

	// TODO: Include checks to ensure query's valid. If not valid, return false.
	return true;
}

function ajaxSearchRequest()
{
	var servletURL = "/Viewer/search/search.do";
	var params = "dt=on&format=Sparql&lang=sparql&limit=1000&type=tuples&query=" + encodeURIComponent(document.frmBasicSearch.query.value);

	var ajaxReq = null;

	if (window.XMLHttpRequest)
		ajaxReq = new XMLHttpRequest();
	else if (window.ActiveXObject)
		ajaxReq = new ActiveXObject("Microsoft.XMLHTTP");

	if (ajaxReq != null)
	{
		ajaxReq.onreadystatechange = function()
		{
			// Processing the AJAX response.
			if (ajaxReq.readyState == 4 && ajaxReq.status == 200)
			{
				// Read the XML response from the AJAX request.
				respXml = ajaxReq.responseXML;
				var sparqlNs = "http://www.w3.org/2001/sw/DataAccess/rf1/result";
				var divSearchResults = document.getElementById("divSearchResults").innerHTML;
				var columnHeadings = new Array();

				// Head element contains the columns that will be returned.
				var headElement = respXml.getElementsByTagNameNS(sparqlNs, "head")[0];
				// console.log(headeElement);
				var searchResultHtml = "<hr />";

				searchResultHtml += "<table border=\"1\">\r\n";

				// Read the columns and add them to table.
				searchResultHtml += "<tr>\r\n";
				for ( var i = 0; i < headElement.childNodes.length; i++)
				{
					if (headElement.childNodes[i].nodeType != 1)
						continue;

					searchResultHtml += "<th>";
					searchResultHtml += columnHeadings[columnHeadings.length] = headElement.childNodes[i].attributes.getNamedItem("name").nodeValue;
					// searchResultHtml += headElement.childNodes[i].attributes.getNamedItem("name").nodeValue;
					searchResultHtml += "</th>";
					console.log("Heading: " + columnHeadings[i]);
					console.log(searchResultHtml);
				}
				searchResultHtml += "</tr>\r\n";

				console.log("Column Headings: " + columnHeadings);

				// Add a row for each search result.
				var results = respXml.getElementsByTagNameNS(sparqlNs, "result");
				for ( var i = 0; i < results.length; i++)
				{
					searchResultHtml += "<tr>\r\n";
					for ( var j = 0; j < columnHeadings.length; j++)
					{
						searchResultHtml += "<td>\r\n";
						searchResultHtml += results[i].getElementsByTagNameNS(sparqlNs, columnHeadings[j])[0].textContent;
						searchResultHtml += "\r\n</td>\r\n";
					}
					searchResultHtml += "</tr>\r\n";
				}

				searchResultHtml += "</table>\r\n";
				document.getElementById("divSearchResults").innerHTML = searchResultHtml;
				console.log(document.getElementById("divSearchResults").innerHTML);

				// var results = respXml.getElementsByTagNameNS(dcNs, "result")[0];
			}
		};

		ajaxReq.open("POST", servletURL, true);
		ajaxReq.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		// ajaxReq.setRequestHeader("Content-length", params.length);
		// ajaxReq.setRequestHeader("Connection", "close");

		ajaxReq.send(params);
	}
}

function basicSearch()
{
	if (generateBasicQuery() == true)
		ajaxSearchRequest();
}
