package edu.wustl.idp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.parser.AuthFileParser;

/**
 * This singleton class keeps all information regarding the configured idps in
 * the application. It reads the configuration file once and caches the
 * information for the further references.
 *
 * @author niharika_sharma
 *
 */
public class ConfiguredIDPDetails
{
    /**
     * Map containing idp information, with the idp name as the key.
     */
    private final Map<String, IDPInterface> idpMap;

    /**
     * Single instance of this class.
     */
    private static ConfiguredIDPDetails configuredIDPDetails;

    /**
     * Private constructor to avoid instantiation outside this class.
     *
     * @param idpMap
     *            Map containing idp information, with the idp name as the key.
     */
    private ConfiguredIDPDetails(final Map<String, IDPInterface> idpMap)
    {
        this.idpMap = idpMap;
    }

    /**
     * This method returns the idp map containing the IDPInterface instances
     * with the idp name as the key.
     *
     * @return idpMap
     */
    public Map<String, IDPInterface> getIdpMap()
    {
        return idpMap;
    }

    /**
     * This method returns an instance of this class on which provides the
     * information of the configured idps.
     *
     * @return instance of ConfiguredIDPDetails
     * @throws AuthFileParseException
     */
    public static ConfiguredIDPDetails getInstance() throws AuthFileParseException
    {
        if (configuredIDPDetails == null)
        {
            try
            {
                Map<String, IDPInterface> configuredIdps = new HashMap<String, IDPInterface>();
                configuredIdps = AuthFileParser.parseAuthFile(System.getProperty("app.domainAuthFilePath"));
                configuredIDPDetails = new ConfiguredIDPDetails(configuredIdps);

            }
            catch (final ParserConfigurationException e)
            {
                throw new AuthFileParseException(e);
            }
            catch (final SAXException e)
            {
                throw new AuthFileParseException(e);
            }
            catch (final IOException e)
            {
                throw new AuthFileParseException(e);
            }
        }
        return configuredIDPDetails;
    }

    /**
     * This method returns the IDPInterface instance with the given name.
     *
     * @param idpName
     *            name of the idp to be returned
     * @return IDPInterface
     * @throws AuthFileParseException
     */
    public IDPInterface getIDP(final String idpName) throws AuthFileParseException
    {
        return configuredIDPDetails.getIdpMap().get(idpName);
    }
}
