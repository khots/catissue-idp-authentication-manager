/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.jboss.naming.NamingContextFactory;
import org.jnp.server.NamingBeanImpl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.exception.ParseException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migration.rules.RuleInterface;
import edu.wustl.migrator.MigratorInterface;
import edu.wustl.migrator.util.Utility;


/**
 *
 * @author niharika_sharma
 *
 */
public class BaseTestCase extends TestCase
{
    public static String dbType = null, dbHost = null, dbPort = null, dbName = null, dbUserName, dbPassword,
    dbSchema;

    static
    {
        try
        {
            setConnectionDS();
        }
        catch (final FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final NamingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try
        {
            XMLPropertyHandler.init("./caTissueCore_Properties.xml");
        }
        catch (final ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        System.setProperty("gov.nih.nci.security.configFile",
        "../ApplicationSecurityConfig.xml");
        System.setProperty("app.domainAuthFilePath", "IDPAuthentication.xml");




    }

    private static void setConnectionDS() throws Exception, NamingException, FileNotFoundException, IOException
    {
        System.setProperty("java.naming.factory.initial", NamingContextFactory.class.getName());
        System.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        final NamingBeanImpl server = new NamingBeanImpl();
        server.start();
        final Context initialContext = new InitialContext();
        final DataSource ciderDS = getDataSource();

        initialContext.createSubcontext("java:/csm");

        initialContext.rebind("java:/csm", ciderDS);
    }

    protected MigratorInterface getWUSTLKeyMigrator() throws AuthenticationException
    {
        MigratorInterface migrator=null;
        final IDPAuthManager targetAuthManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
                "WUSTLKEY_IDP");

        final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
        final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

        for (final Properties properties : migrationProperties)
        {
            final String migratorClassName = (String) properties
                    .get(edu.wustl.migrator.util.Constants.MIGRATOR_CLASS_TAG_NAME);
            migrator = Utility.getMigratorInstance(migratorClassName, targetAuthManager.getIDP());

        }
        return migrator;
    }

    private static DataSource getDataSource() throws FileNotFoundException, IOException
    {
        final Properties props = new Properties();
        final java.io.File f = new java.io.File("idpTestInstall.properties");
        props.load(new FileInputStream(f));
        dbType = props.getProperty("database.type");
        dbHost = props.getProperty("database.host");
        dbPort = props.getProperty("database.port");
        dbName = props.getProperty("database.name");
        dbUserName = props.getProperty("database.user");
        dbPassword = props.getProperty("database.password");
        dbSchema = props.getProperty("database.schema");


        final MysqlDataSource dataSource=new MysqlDataSource();

        dataSource.setDatabaseName(dbName);
        dataSource.setServerName(dbHost);
        dataSource.setPortNumber(Integer.valueOf(dbPort));
        dataSource.setUser(dbUserName);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }




    protected RuleInterface getWUSTLKeyRule() throws AuthenticationException
    {
        RuleInterface rule=null;

        final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
        final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

        for (final Properties properties : migrationProperties)
        {
            final String ruleClassName = (String) properties
                    .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);
            rule = (RuleInterface) edu.wustl.common.util.Utility.getObject(ruleClassName);

        }
        return rule;
    }

}
