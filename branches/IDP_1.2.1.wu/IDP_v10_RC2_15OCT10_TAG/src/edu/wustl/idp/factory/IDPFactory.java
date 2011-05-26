package edu.wustl.idp.factory;

import java.util.Iterator;
import java.util.Map;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.idp.ConfiguredIDPDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.util.Constants;

/**
 * This factory class is to get instances of the idps configured in the
 * application.
 *
 * @author niharika_sharma
 *
 */
public class IDPFactory
{
    /**
     * Single reference of the class.
     */
    private static IDPFactory idpFactory;

    /**
     * Private constructor to avoid instantiation outside this class.
     */
    private IDPFactory()
    {

    }

    /**
     * This method returns the single instance of this class.
     *
     * @return idpFactory
     */
    public static IDPFactory getInstance()
    {
        if (idpFactory == null)
        {
            idpFactory = new IDPFactory();
        }
        return idpFactory;
    }

    /**
     * This method returns the default idp configured in the application.
     *
     * @return default idp
     * @throws AuthenticationException
     */
    public IDPInterface getDefaultIDP() throws AuthenticationException
    {
        IDPInterface idp = null;
        try
        {
            final Iterator<?> it = ConfiguredIDPDetails.getInstance().getIdpMap().entrySet().iterator();

            IDPInterface value = null;
            while (it.hasNext())
            {
                final Map.Entry<String, IDPInterface> pairs = (Map.Entry<String, IDPInterface>) it.next();
                value = pairs.getValue();
                if (value.getIDPProperties().getProperty(Constants.IS_DEFAULT_IDP)
                        .equalsIgnoreCase(Constants.TRUE))
                {
                    idp = value;
                    break;
                }
            }
        }
        catch (final AuthFileParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return idp;
    }

    /**
     * This method returns a map of all the configured idps in the application.
     *
     * @return idp map
     * @throws AuthFileParseException
     */
    public Map<String, IDPInterface> getConfiguredIDPs() throws AuthFileParseException
    {
        return ConfiguredIDPDetails.getInstance().getIdpMap();
    }
}
