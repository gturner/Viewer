<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/search/js/search.js"></script>
</head>
<body>
	<h1>Search</h1>
	<div id="divBasicSearch">
		<form name="frmBasicSearch" action="<%=request.getContextPath()%>/search/search.do" method="post" onsubmit="basicSearch(); return false;">
			<label for="basicSearchTerms">Search for: </label><input type="text" id="idBasicSearchTerms" size="30"  />&nbsp;
			<input type="hidden" name="query" id="idBasicQuery" value="null">
			<input type="hidden" name="dt" value="on" />
			<input type="hidden" name="format" value="Sparql" />
			<input type="hidden" name="lang" value="sparql" />
			<input type="hidden" name="limit" value="1000" />
			<input type="hidden" name="type" value="tuples" />
			
			<input type="button" value="Search" onclick="basicSearch()" />
		</form>
	</div>
	<div id="divAdvancedSearch" style="display: none;">
		<form action="http://localhost:8081/fedora/risearch" method="post">
			<table>
				<tr>
					<td><label for="idTitleTerms">Title</label></td>
					<td><select id="id_title_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idTitleTerms" /></td>
				</tr>

				<tr>
					<td><label for="idCreatorTerms">Creator</label></td>
					<td><select id="id_creator_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idCreatorTerms" /></td>
				</tr>

				<tr>
					<td><label for="idSubjectTerms">Subject</label></td>
					<td><select id="id_subject_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idSubjectTerms" /></td>
				</tr>

				<tr>
					<td><label for="idDescriptionTerms">Description</label></td>
					<td><select id="id_description_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idDescriptionTerms" /></td>
				</tr>

				<tr>
					<td><label for="idPublisherTerms">Publisher</label></td>
					<td><select id="id_publisher_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idPublisherTerms" /></td>
				</tr>

				<tr>
					<td><label for="idContributorTerms">Contributor</label></td>
					<td><select id="id_contributor_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idContributorTerms" /></td>
				</tr>

				<tr>
					<td><label for="idDateTerms">Date</label></td>
					<td><select id="id_date_operator">
							<option value="=">equals</option>
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idDateTerms" /></td>
				</tr>

				<tr>
					<td><label for="idTypeTerms">Type</label></td>
					<td><select id="id_type_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idTypeTerms" /></td>
				</tr>

				<tr>
					<td><label for="idFormatTerms">Format</label></td>
					<td><select id="id_format_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idFormatTerms" /></td>
				</tr>

				<tr>
					<td><label for="idIdentifierTerms">Identifier</label></td>
					<td><select id="id_identifier_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idIdentifierTerms" /></td>
				</tr>

				<tr>
					<td><label for="idSourceTerms">Source</label></td>
					<td><select id="id_source_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idSourceTerms" /></td>
				</tr>

				<tr>
					<td><label for="idLanguageTerms">Language</label></td>
					<td><select id="id_language_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idLanguageTerms" /></td>
				</tr>

				<tr>
					<td><label for="idRelationTerms">Relation</label></td>
					<td><select id="id_relation_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idRelationTerms" /></td>
				</tr>

				<tr>
					<td><label for="idCoverageTerms">Coverage</label></td>
					<td><select id="id_coverage_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idCoverageTerms" /></td>
				</tr>

				<tr>
					<td><label for="idRightsTerms">Rights</label></td>
					<td><select id="id_rights_operator">
							<option value="~">contains</option>
					</select></td>
					<td><input type="text" size="40" id="idRightsTerms" /></td>
				</tr>
				<tr>
					<td><input type="checkbox" value="true" name="pid" id="idDispPid" checked="checked" /><label for="idDispPid">Pid</label> <br /> <input
						type="checkbox" value="true" name="title" id="idDispTitle" checked="checked" /><label for="idDispTitle">Title</label></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Search" onclick="return generateQueryStr()" /></td>
					<td><input type="reset" value="Reset" /></td>
				</tr>
			</table>
			<input type="hidden" name="query" id="idAdvQuery" /> <input type="hidden" name="format" value="xml" />
		</form>
	</div>
	<div id="divSearchResults"></div>
</body>
<script type="text/javascript">
	document.frmBasicSearch.elements['idBasicSearchTerms'].focus();
</script>
</html>