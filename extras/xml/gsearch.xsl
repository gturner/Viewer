<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" exclude-result-prefixes="exts" xmlns:audit="info:fedora/fedora-system:def/audit#" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exts="xalan://dk.defxws.fedoragsearch.server.GenericOperationsImpl" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dtu_meta="http://www.dtu.dk/dtu_meta/" xmlns:foxml="info:fedora/fedora-system:def/foxml#" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:meta="http://www.dtu.dk/dtu_meta/meta/">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
	<xsl:param name="REPOSITORYNAME" select="'FgsRepos'"/>
	<xsl:param name="REPOSBASEURL" select="'http://localhost:8380/fedora'"/>
	<xsl:param name="FEDORASOAP" select="'http://localhost:8380/fedora/services'"/>
	<xsl:param name="FEDORAUSER" select="'fedoraAdmin'"/>
	<xsl:param name="FEDORAPASS" select="'fedoraAdmin'"/>
	<xsl:param name="TRUSTSTOREPATH" select="'trustStorePath'"/>
	<xsl:param name="TRUSTSTOREPASS" select="'trustStorePass'"/>
	<xsl:variable name="PID" select="/foxml:digitalObject/@PID"/>
	<xsl:template match="/">
		<!--The following allows only active FedoraObjects to be indexed.-->
		<xsl:if test="foxml:digitalObject/foxml:objectProperties/foxml:property[@NAME='info:fedora/fedora-system:def/model#state' and @VALUE='Active']">
			<xsl:if test="not(foxml:digitalObject/foxml:datastream[@ID='METHODMAP'] or foxml:digitalObject/foxml:datastream[@ID='DS-COMPOSITE-MODEL'])">
				<xsl:if test="starts-with($PID,'')">
					<xsl:apply-templates mode="activeFedoraObject"/>
				</xsl:if>
			</xsl:if>
		</xsl:if>
		<!--The following allows inactive FedoraObjects to be deleted from the index.-->
		<xsl:if test="foxml:digitalObject/foxml:objectProperties/foxml:property[@NAME='info:fedora/fedora-system:def/model#state' and @VALUE='Inactive']">
			<xsl:if test="not(foxml:digitalObject/foxml:datastream[@ID='METHODMAP'] or foxml:digitalObject/foxml:datastream[@ID='DS-COMPOSITE-MODEL'])">
				<xsl:if test="starts-with($PID,'')">
					<xsl:apply-templates mode="inactiveFedoraObject"/>
				</xsl:if>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	<xsl:template match="/foxml:digitalObject" mode="activeFedoraObject">
		<add>
			<doc>
				<field name="id">
					<xsl:value-of select="$PID"/>
				</field>
				<field name="PID">
					<xsl:value-of select="$PID"/>
				</field>
				<field name="REPOSITORYNAME">
					<xsl:value-of select="$REPOSITORYNAME"/>
				</field>
				<field name="REPOSBASEURL">
					<xsl:value-of select="substring($FEDORASOAP, 1, string-length($FEDORASOAP)-9)"/>
				</field>
				<field name="TITLE_UNTOK">
					<xsl:value-of select="foxml:datastream/foxml:datastreamVersion[last()]/foxml:xmlContent/oai_dc:dc/dc:title"/>
				</field>
				<field name="AUTHOR_UNTOK">
					<xsl:value-of select="foxml:datastream/foxml:datastreamVersion[last()]/foxml:xmlContent/oai_dc:dc/dc:creator"/>
				</field>
				<!--indexing foxml property fields-->
				<xsl:for-each select="foxml:objectProperties/foxml:property">
					<field>
						<xsl:attribute name="name">
							<xsl:value-of select="concat('fgs.', substring-after(@NAME,'#'))"/>
						</xsl:attribute>
						<xsl:value-of select="@VALUE"/>
					</field>
				</xsl:for-each>
				<!--indexing foxml fields-->
				
				<xsl:for-each select="foxml:datastream/foxml:datastreamVersion[last()]/foxml:xmlContent/data/item">
					<xsl:variable name="name"><xsl:value-of select="@name" /></xsl:variable>
					<xsl:choose>
						<xsl:when test="option">
						 	<xsl:for-each select="option">
								<field>
									<xsl:attribute name="name">
										<xsl:value-of select="concat('item.', $name)" />
									</xsl:attribute>
									<xsl:value-of select="text()" />
								</field>
							</xsl:for-each>
						</xsl:when>
						<xsl:when test="row">
						 	<xsl:for-each select="row/column">
						 		<field>
									<xsl:attribute name="name">
										<xsl:value-of select="concat('item.', $name, '.', @name)" />
									</xsl:attribute>
									<xsl:value-of select="text()" />
						 		</field>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<field>
								<xsl:attribute name="name">
									<xsl:value-of select="concat('item.', @name)" />
								</xsl:attribute>
								<xsl:value-of select="text()" />
							</field>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
				
				
				<!-- a datastream is fetched, if its mimetype 
							     can be handled, the text becomes the value of the field.
							     This is the version using PDFBox,
							     below is the new version using Apache Tika. -->
				<!---->
				<!-- Text and metadata extraction using Apache Tika. -->
		<!-- 		<xsl:for-each select="foxml:datastream[@CONTROL_GROUP='M' or @CONTROL_GROUP='E' or @CONTROL_GROUP='R']">
					<xsl:value-of disable-output-escaping="yes" select="exts:getDatastreamFromTika($PID, $REPOSITORYNAME, @ID, 'field', concat('ds.', @ID), concat('dsmd.', @ID, '.'), '', $FEDORASOAP, $FEDORAUSER, $FEDORAPASS, $TRUSTSTOREPATH, $TRUSTSTOREPASS)"/>
				</xsl:for-each> -->
				<!--creating an index field with all text from the foxml record and its datastreams-->
		<!--		<field name="foxml.all.text">
					<xsl:for-each select="//text()">
						<xsl:value-of select="."/>
						<xsl:text> </xsl:text>
					</xsl:for-each>
					<xsl:for-each select="//foxml:datastream[@CONTROL_GROUP='M' or @CONTROL_GROUP='E' or @CONTROL_GROUP='R']">
						<xsl:value-of select="exts:getDatastreamText($PID, $REPOSITORYNAME, @ID, $FEDORASOAP, $FEDORAUSER, $FEDORAPASS, $TRUSTSTOREPATH, $TRUSTSTOREPASS)"/>
						<xsl:text> </xsl:text>
					</xsl:for-each> 
				</field> -->
			</doc>
		</add>
	</xsl:template>
	<xsl:template match="/foxml:digitalObject" mode="inactiveFedoraObject">
		<delete>
			<id>
				<xsl:value-of select="$PID"/>
			</id>
		</delete>
	</xsl:template>
</xsl:stylesheet>
