/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.authmanager.factory;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.IDPAuthManagerImpl;
import edu.wustl.idp.ConfiguredIDPDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.util.Constants;

/**
 * @author supriya_dankh
 *
 */
public class AuthManagerFactory
{
    private static AuthManagerFactory authManagerFactory;

    private AuthManagerFactory()
    {

    }

    public static AuthManagerFactory getInstance()
    {
        if (authManagerFactory == null)
        {
            authManagerFactory = new AuthManagerFactory();
        }
        return authManagerFactory;
    }

    public IDPAuthManager getAuthManagerInstance() throws AuthenticationException
    {
        IDPAuthManagerImpl authManager = null;
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
                    break;
                }
            }
            authManager = getClassInstance(value);
        }
        catch (final AuthFileParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return authManager;
    }

    private IDPAuthManagerImpl getClassInstance(final IDPInterface value) throws AuthenticationException
    {
        IDPAuthManagerImpl authManager = null;
        try
        {
            Class<?> actualClass;
            final String className = value.getIDPProperties().getProperty(Constants.CLASS_NAME);
            actualClass = Class.forName(className);
            if (className != null && actualClass != null)
            {
                final Class<?>[] parameterTypes = { edu.wustl.idp.IDPInterface.class };
                final Constructor<?> declaredConstructor = actualClass.getDeclaredConstructor(parameterTypes);
                authManager = (IDPAuthManagerImpl) declaredConstructor.newInstance(value);
            }
        }
        catch (final ClassNotFoundException e)
        {
            throw new AuthenticationException(e);
        }
        catch (final Exception e)
        {
            throw new AuthenticationException(e);
        }
        return authManager;
    }

    public IDPAuthManager getAuthManagerInstance(final String idpName) throws AuthenticationException
    {
        IDPAuthManagerImpl classInstance = null;
        try
        {
            final IDPInterface abstractIDP =  ConfiguredIDPDetails.getInstance().getIDP(idpName);
            classInstance = getClassInstance(abstractIDP);
        }
        catch (final AuthFileParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return classInstance;
    }
}
