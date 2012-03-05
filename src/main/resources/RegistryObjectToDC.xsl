<?xml version="1.0" encoding="ISO-8859-1"?>

<!-- 
title: Done.
creator
subject: Done. Multiple values separated by commas.
description: Done. Brief (if exists) NEWLINE Full (if exists)
publisher
contributor
date
type
format
identifier
source
language: Unable to extract - multiple elements have their own language attribute.
relation
coverage: Done.
rights: Done. Assumption - either a dc:rights element OR (exclusive) a dc:description[@type='rights'] exists.
 -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ro="http://ands.org.au/standards/rif-cs/registryObjects"
	xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc.xsd">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" />
	<xsl:template match="/">
		<oai_dc:dc xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">

			<!-- DC Title element -->
			<xsl:element name="dc:title">
				<xsl:value-of select="//ro:name" />
			</xsl:element>

			<!-- DC Subject element -->
			<xsl:element name="dc:subject">
				<xsl:for-each select="//ro:subject[@type='local']">
					<xsl:value-of select="." />
					<xsl:if test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:if>
				</xsl:for-each>
			</xsl:element>

			<!-- DC Description element -->
			<xsl:element name="dc:description">
				<xsl:if test="//ro:description[@type='brief']">
					<xsl:value-of select="//ro:description[@type='brief']" />
					<xsl:if test="//ro:description[@type='full']">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:if>

				<xsl:if test="//ro:description[@type='full']">
					<xsl:value-of select="//ro:description[@type='full']" />
				</xsl:if>
			</xsl:element>
			
			<!-- DC Coverage element -->
			<xsl:element name="dc:coverage">
				<xsl:value-of select="//ro:coverage" />
			</xsl:element>
			
			<!-- DC Rights element -->
			<xsl:element name="dc:rights">
				<xsl:for-each select="//ro:rights">
					<xsl:value-of select="." />
					<xsl:if test="position() != last()">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>

				<xsl:for-each select="//ro:description[@type='rights']">
					<xsl:value-of select="." />
					<xsl:if test="position() != last()">
						<xsl:text>&#10;</xsl:text>
					</xsl:if>
				</xsl:for-each>
			</xsl:element>
		</oai_dc:dc>
	</xsl:template>
	
	<!-- Trim Template -->
	<xsl:template name="left-trim">
		<xsl:param name="s" />
		<xsl:choose>
			<xsl:when test="substring($s, 1, 1) = ''">
				<xsl:value-of select="$s" />
			</xsl:when>
			<xsl:when test="normalize-space(substring($s, 1, 1)) = ''">
				<xsl:call-template name="left-trim">
					<xsl:with-param name="s" select="substring($s, 2)" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$s" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="right-trim">
		<xsl:param name="s" />
		<xsl:choose>
			<xsl:when test="substring($s, 1, 1) = ''">
				<xsl:value-of select="$s" />
			</xsl:when>
			<xsl:when test="normalize-space(substring($s, string-length($s))) = ''">
				<xsl:call-template name="right-trim">
					<xsl:with-param name="s" select="substring($s, 1, string-length($s) - 1)" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$s" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="trim">
		<xsl:param name="s" />
		<xsl:call-template name="right-trim">
			<xsl:with-param name="s">
				<xsl:call-template name="left-trim">
					<xsl:with-param name="s" select="$s" />
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
</xsl:stylesheet>