package edu.wustl.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.util.global.XMLParserUtility;
import edu.wustl.idp.AbstractBaseIDP;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.util.Constants;

/**
 * This class is a parser of authentication IDP XML file.
 * @author Supriya_Dankh
 * @version 1.0
 */
public class AuthFileParser {

	/**
	 * This method parse over all authentication file and create AbstractBaseIDP object for each IDP present in the
	 * authentication file and returns list of all IDPS.
	 * @param propFilePath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Map<String,IDPInterface> parseAuthFile(
			final String propFilePath) throws ParserConfigurationException,
			SAXException, IOException {

		IDPInterface abstractIDP ;
		final Map<String,IDPInterface> asbtList = new HashMap<String, IDPInterface>();

		final Document document = XMLParserUtility.getDocument(propFilePath);
		final Element root = document.getDocumentElement();
		final NodeList domains = root.getElementsByTagName(Constants.ROOT_NODE_NAME);
		for (int i = 0; i < domains.getLength(); i++) {
		    final Properties domainProperties = new Properties();
	        final Properties userAttributes = new Properties();
			abstractIDP = new AbstractBaseIDP();
			final Node domain = domains.item(i);
			final NamedNodeMap attributes = domain.getAttributes();
			final Node idpNameNode = attributes.getNamedItem(Constants.NAME_NODE);
			final String name = idpNameNode.getNodeValue();
			abstractIDP.setName(name);

			final NodeList childNodes = domain.getChildNodes();
			final Node classNode = childNodes.item(1);
			final String className = classNode.getAttributes().item(0).getNodeValue();
			domainProperties.put(Constants.CLASS_NAME, className);

			Node configNode = childNodes.item(3);
			parseChildNodes(configNode, domainProperties);
			configNode = childNodes.item(5);
			parseChildNodes(configNode, domainProperties);
			abstractIDP.setIDPProperties(domainProperties);
			final Node userAttrNode = childNodes.item(7);
			parseChildNodes(userAttrNode, userAttributes);
			abstractIDP.setUserProperties(userAttributes);
			final Node migrationDetailsNode = childNodes.item(9);
			final List<Properties> migrationProperties = parseMigrationDetailsNode(migrationDetailsNode);
			if(migrationProperties!=null && !migrationProperties.isEmpty())
			{
				abstractIDP.setMigrationEnabled(true);
			}
			abstractIDP.setMigrationProperties(migrationProperties);
			asbtList.put(name, abstractIDP);
		}

		return asbtList;
	}

	/**
	 * This method parse child nodes of any root node passed to this method and stores details in properties
	 * object passed to this method.
	 * @param node root node that needs to be parsed.
	 * @param properties java Properties object that stores all the details related all to child nodes.
	 */
	private static void parseChildNodes(final Node node, final Properties properties) {

		final NodeList childNodes = node.getChildNodes();
		if (childNodes.getLength() > 1) {
			for (int j = 1; j <= childNodes.getLength() - 2; j += 2) {
				final Node childNode = childNodes.item(j);
				final String value = childNode.getTextContent();
				properties.put(childNode.getNodeName(), value);
			}
		}
	}

	/**
	 * This method parse the migration node details node from authentication file and creates a list of Properties in case
	 * of multiple migrators.
	 * @param migrationDetailsNode Migration node details to be parsed.
	 * @return List<Properties> List of java Properties object which contains all the details related to migration.
	 * */
	private static List<Properties> parseMigrationDetailsNode(final Node migrationDetailsNode)
	{
		final NodeList childNodes = migrationDetailsNode.getChildNodes();
		final List<Properties> propertiesList = new ArrayList<Properties>();
		if (childNodes.getLength() > 1) {
			for (int j = 1; j <= childNodes.getLength()-2; j += 2) {
				final Properties migrationProperties = new Properties();
				final Node childNode = childNodes.item(j);
				parseChildNodes(childNode, migrationProperties);
				propertiesList.add(migrationProperties);
			}
		}
		return propertiesList;
	}

	}


