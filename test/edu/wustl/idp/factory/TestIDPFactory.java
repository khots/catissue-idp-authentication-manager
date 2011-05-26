package edu.wustl.idp.factory;

import java.util.Map;

import org.junit.Assert;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.util.Constants;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestIDPFactory extends BaseTestCase
{
    public void test_Get_Default_Idp()
    {

        try
        {
            final IDPFactory idpFactory = IDPFactory.getInstance();
            final IDPInterface defaultIdp = idpFactory.getDefaultIDP();
            Assert.assertNotNull(defaultIdp);
            Assert.assertEquals(Constants.TRUE, defaultIdp.getIDPProperties()
                    .getProperty(Constants.IS_DEFAULT_IDP));

        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Get_Default_Idp failed due to " + authenticationException);
        }

    }

    public void test_Get_Instance_Returns_Same_Instance()
    {
        final IDPFactory idpFactory = IDPFactory.getInstance();
        Assert.assertNotNull(idpFactory);
        final IDPFactory idpFactoryNew = IDPFactory.getInstance();
        Assert.assertNotNull(idpFactoryNew);

        Assert.assertEquals(idpFactory, idpFactoryNew);
    }

    /**
     * Three IDPs have been configured in the xml.
     */
    public void test_Get_Configured_IDPs_Should_Return_Three()
    {
        try
        {
            final IDPFactory idpFactory = IDPFactory.getInstance();
            final Map<String, IDPInterface> idpMap = idpFactory.getConfiguredIDPs();
            Assert.assertNotNull(idpMap);
            Assert.assertEquals(3, idpMap.keySet().size());
        }
        catch (final AuthFileParseException authenticationException)
        {
            fail("test_Get_Configured_IDPs_Should_Return_Three failed due to " + authenticationException);
        }

    }
}
