<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="param1" />
	<xsl:param name="item" />
	<xsl:param name="type" />
	<xsl:variable name="mDoc" select="$param1" />
	<xsl:template match="/">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="css/default.css" />
				<script type='text/javascript' src='scripts/form.js'></script>
			</head>
			<body>
				<form method="post" onsubmit="return validateForm()">
					<xsl:attribute name="action">submitController?type=<xsl:value-of select="$type" />&amp;item=<xsl:value-of select="$item" /></xsl:attribute>
					<table>
						<xsl:for-each select="webpage/item">
							<tr>
								<xsl:choose>
									<xsl:when test="@type='TextField'">
										<xsl:call-template name="Label" />
										<xsl:call-template name="TextField" />
									</xsl:when>
									<xsl:when test="@type='TextFieldMulti'">
										<xsl:call-template name="Label" />
										<xsl:call-template name="TextFieldMulti" />
									</xsl:when>
									<xsl:when test="@type='TextArea'">
										<xsl:call-template name="Label" />
										<xsl:call-template name="TextArea" />
									</xsl:when>
									<xsl:when test="@type='Checkbox'">
										<xsl:call-template name="Label" />
										<xsl:call-template name="Checkbox" />
									</xsl:when>
									<xsl:when test="@type='Table'">
										<xsl:call-template name="Label" />
										<xsl:call-template name="Table" />
									</xsl:when>
									<xsl:when test="@type='ComboBox'">
										<xsl:call-template name="Label" />
										<xsl:call-template name="ComboBox" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="Label" />
										<td>Nothing here!</td>
									</xsl:otherwise>
								</xsl:choose>
							</tr>
						</xsl:for-each>
					</table>
					<input type="submit" value="Submit" />
				</form>
			<!-- 	<xsl:for-each select="$param2"><xsl:value-of select="$param2" /></xsl:for-each> -->
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="Label">
		<td>
			<label>
				<xsl:attribute name="for">
					<xsl:value-of select="@name" />
				</xsl:attribute>
				<xsl:value-of select="@label" />
			</label>
		</td>
	</xsl:template>
	
	<xsl:template name="TextArea">
		<td>
			<textarea>
				<xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
				<xsl:attribute name="class"><xsl:value-of select="@class" /></xsl:attribute>
				<xsl:variable name="mVar" select="@name" />
				
				<xsl:if test="$param1 != ''">
					<xsl:value-of select="$mDoc/data/item[@name=$mVar]" />
				</xsl:if>
			</textarea>
		</td>
	</xsl:template>
	
	<xsl:template name="TextField">
		<td>
			<input>
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
				<xsl:attribute name="class"><xsl:value-of select="@class" /></xsl:attribute>
				<xsl:if test="@disabled = 'disabled'">
					<xsl:attribute name="disabled"><xsl:value-of select="@disabled" /></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="@value" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="@readonly = 'readonly'">
					<xsl:attribute name="readonly"><xsl:value-of select="@readonly" /></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="@value" /></xsl:attribute>
				</xsl:if>
				<xsl:if test="$param1 != ''">
					<xsl:variable name="mVar" select="@name"></xsl:variable>
					<xsl:attribute name="value"><xsl:value-of select="$mDoc/data/item[@name=$mVar]" /></xsl:attribute>
				</xsl:if>
			</input>
		</td>				
	</xsl:template>
	
	
	<xsl:template name="TextFieldMulti">
		<td>
			<input type="button" value="Add Row" onClick="cloneElement(nextSibling.nextSibling.childNodes[1].childNodes[0],1,100);" />
			<table>
				<xsl:variable name="mName" select="@name"></xsl:variable>
				<xsl:variable name="mClass" select="@class"></xsl:variable>
				<xsl:choose>
					<xsl:when test="$param1 != '' and $mDoc/data/item[@name=$mName]/option">
						<xsl:for-each select="$mDoc/data/item[@name=$mName]/option">
							<tr>
								<td>
									<input>
										<xsl:attribute name="type">text</xsl:attribute>
										<xsl:attribute name="name"><xsl:value-of select="$mName" /></xsl:attribute>
										<xsl:attribute name="class"><xsl:value-of select="$mClass" /></xsl:attribute>
										<xsl:attribute name="value"><xsl:value-of select="text()" /></xsl:attribute>
									</input>
								</td>
								<td><input type="button" value="Remove" onClick="removeElement(this.parentNode.parentNode, 1, 100)" /></td>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td>
								<input>
									<xsl:attribute name="type">text</xsl:attribute>
									<xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
									<xsl:attribute name="class"><xsl:value-of select="@class" /></xsl:attribute>
								</input>
							</td>
							<td><input type="button" value="Remove" onClick="removeElement(this.parentNode.parentNode, 1, 100)" /></td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</table>
		</td>				
	</xsl:template>
	
	<xsl:template name="Checkbox">
		<td>
			<xsl:variable name="mVar" select="@name" />
			<xsl:for-each select="option">
				<input>
					<xsl:attribute name="type">checkbox</xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="@value" /></xsl:attribute>
						
				 	<xsl:if test="$param1 != ''">
				 		<xsl:if test="$mDoc/data/item[@name=$mVar]/option = @value">
				 			<xsl:attribute name="checked">checked</xsl:attribute>
				 		</xsl:if>
					</xsl:if>
					<xsl:attribute name="name"><xsl:value-of select="$mVar" /></xsl:attribute>
				</input>
				<xsl:value-of select="@label" /><br />
			</xsl:for-each>
		</td>
	</xsl:template>
	
	<xsl:template name="Table">
		<td>
			<input type="button" value="Add Row" onClick="cloneElement(getNextTableRow(this),1,100);" />
				<xsl:variable name="cols" select="." />
			<table>
				<tr>
					<xsl:for-each select="column">
						<th><xsl:value-of select="@label" /></th>
					</xsl:for-each>
				</tr>
				<xsl:variable name="mVar" select="@name" />
				 	<xsl:choose>
						<xsl:when test="$param1 != '' and $mDoc/data/item[@name=$mVar]/row">
							<xsl:for-each select="$mDoc/data/item[@name=$mVar]/row">
								<xsl:variable name="itemVal" select="." />
								<tr>
								<xsl:for-each select="$cols/column">
									<td>			
										<input>
											<xsl:attribute name="type">text</xsl:attribute>
											<xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
											<xsl:attribute name="class"><xsl:value-of select="@class" /></xsl:attribute>
											<xsl:variable name="mVar" select="@name"></xsl:variable>
											<xsl:attribute name="value"><xsl:value-of select="$itemVal/column[@name=$mVar]" /></xsl:attribute>
										</input>
									</td>
								</xsl:for-each>
								<td><input type="button" value="Remove" onClick="removeElement(this.parentNode.parentNode, 1, 100)" /></td>
							
								</tr>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<tr>
							 	<xsl:for-each select="column">
									<xsl:choose>
										<xsl:when test="@type='TextField'">
											<xsl:call-template name="TextField" />
										</xsl:when>
										<xsl:when test="@type='TextArea'">
											<xsl:call-template name="TextArea" />
										</xsl:when>
									</xsl:choose>
									
								</xsl:for-each>
								<td><input type="button" value="Remove" onClick="removeElement(this.parentNode.parentNode, 1, 100)" /></td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
					
			</table>
		</td>
	</xsl:template>
	
	
	<xsl:template name="ComboBox">
		<td>
			<xsl:variable name="mVar" select="@name" />
			<select>
				<xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
				<xsl:attribute name="class"><xsl:value-of select="@class" /></xsl:attribute>
				<xsl:for-each select="option">
					<option>
						<xsl:attribute name="value"><xsl:value-of select="@value" /></xsl:attribute>
						<xsl:if test="$param1 != ''">
							<xsl:if test="$mDoc/data/item[@name=$mVar] = @value">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
						</xsl:if>
						<xsl:value-of select="@label" />
					</option>
				</xsl:for-each>
			</select>
		</td>
	</xsl:template>
	
</xsl:stylesheet>