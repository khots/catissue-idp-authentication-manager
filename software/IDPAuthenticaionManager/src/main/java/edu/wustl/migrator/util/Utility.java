/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.auth.exception.MigrationRuleException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.UserDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.idp.factory.IDPFactory;
import edu.wustl.migration.rules.RuleInterface;
import edu.wustl.migrator.MigratorInterface;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class contains the common methods required by the Migrator classes.
 *
 * @author niharika_sharma
 *
 */
public class Utility
{
    /** The Constant LOGGER. */
    private static final org.apache.log4j.Logger LOGGER = LoggerConfig.getConfiguredLogger(Utility.class);

    /**
     * Execute query using data source.
     *
     * @param queryString
     *            the query string
     * @param isDML
     *            the is dml
     * @param applicationName
     *            the application name
     *
     * @return the list
     *
     * @throws ApplicationException
     *             the application exception
     */
    public static List executeQueryUsingDataSource(final String queryString,
            final List<ColumnValueBean> parameterDetails, final Boolean isDML, final String applicationName)
            throws ApplicationException
    {
        JDBCDAO jdbcDAO = null;
        List resultList = null;
        try
        {
            jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
            jdbcDAO.openSession(null);
            if (!isDML)
            {
                resultList = jdbcDAO.executeQuery(queryString, parameterDetails);
                if (resultList.isEmpty())
                {
                    resultList = null;
                }
            }
            else
            {
                if (parameterDetails == null)
                {
                    jdbcDAO.executeUpdate(queryString);
                }
                else
                {
                    jdbcDAO.executeUpdate(queryString, parameterDetails);
                }
            }
        }
        catch (final DAOException ex)
        {
            LOGGER.debug(ex.getMessage(), ex);
            jdbcDAO.rollback();
            // @Bugid 19485
            //throw new ApplicationException(ex.getErrorKey(), ex, ex.getMessage());
            throw ex;
        }
        catch (final Throwable throwable)
        {
            LOGGER.debug(throwable.getMessage(), throwable);
            // throw new ApplicationException(throwable.getErrorKey(), ex,
            // ex.getMessage());
        }
        finally
        {
            jdbcDAO.closeSession();
        }
        return resultList;
    }

    /**
     * This method returns the instance of MigratorInterface given the class
     * name and the target idp.
     *
     * @param className
     * @param targetIdp
     * @return
     * @throws AuthenticationException
     */
    public static MigratorInterface getMigratorInstance(final String className, final IDPInterface targetIdp)
            throws AuthenticationException
    {
        MigratorInterface migrator = null;
        try
        {
            Class<?> actualClass;
            actualClass = Class.forName(className);
            if (className != null && actualClass != null)
            {
                final Class<?>[] parameterTypes = { edu.wustl.idp.IDPInterface.class };
                final Constructor<?> declaredConstructor = actualClass.getDeclaredConstructor(parameterTypes);
                migrator = (MigratorInterface) declaredConstructor.newInstance(targetIdp);
            }
        }
        catch (final ClassNotFoundException e)
        {
            LOGGER.debug(e.getMessage(), e);
            throw new AuthenticationException(e);
        }
        catch (final Exception e)
        {
            LOGGER.debug(e.getMessage(), e);
            throw new AuthenticationException(e);
        }
        return migrator;
    }

    /**
     * This method returns a list of NameValueBeans of configured idps,
     * including the default idp if "getDefaultIdp" argument is set to true.
     *
     * @param getDefaultIdp
     * @return
     * @throws AuthFileParseException
     */
    public static List<NameValueBean> getConfiguredIDPNVB(final boolean getDefaultIdp)
            throws AuthFileParseException
    {
        final List<NameValueBean> idpsList = new ArrayList<NameValueBean>();
        final Map<String, IDPInterface> idpsMap = IDPFactory.getInstance().getConfiguredIDPs();

        final Set<Entry<String, IDPInterface>> idpsMapEntrySet = idpsMap.entrySet();

        for (final Entry<String, IDPInterface> entry : idpsMapEntrySet)
        {
            if (!entry.getValue().getIDPProperties().getProperty(edu.wustl.migrator.util.Constants.IS_DEFAULT_IDP)
                    .equalsIgnoreCase(Constants.TRUE))
            {
                final NameValueBean idpNVB = createIDPNVB(entry);
                idpsList.add(idpNVB);
            }
        }

        return idpsList;
    }

    /**
     * This method creates an NVB instance for the given idp.
     *
     * @param entry
     * @return
     */
    private static NameValueBean createIDPNVB(final Entry<String, IDPInterface> entry)
    {
        final NameValueBean idpNVB = new NameValueBean();
        idpNVB.setValue(entry.getKey());
        idpNVB.setName(entry.getValue().getIDPProperties().getProperty(
                edu.wustl.migrator.util.Constants.IDP_DISPLAY_NAME_TAG_NAME));
        return idpNVB;
    }

    /**
     * Given the user details, this method performs the migration of the user.
     *
     * @param userDetails
     * @throws MigratorException
     */
    public static void migrateUser(final UserDetails userDetails) throws MigratorException, DAOException
    {
        try
        {
            final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
            final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

            for (final Properties properties : migrationProperties)
            {
                final String migratorClassName = (String) properties
                        .get(edu.wustl.migrator.util.Constants.MIGRATOR_CLASS_TAG_NAME);
                final String ruleClassName = (String) properties
                        .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);
                final RuleInterface rule = (RuleInterface) edu.wustl.common.util.Utility.getObject(ruleClassName);

                final LoginCredentials baseIdpCredentials = new LoginCredentials();
                baseIdpCredentials.setLoginName(userDetails.getLoginName());
                if (rule.checkMigrationRules(baseIdpCredentials))
                {
                    final IDPAuthManager targetAuthManager = AuthManagerFactory.getInstance()
                            .getAuthManagerInstance(userDetails.getTargetIDP());
                    final MigratorInterface migrator = Utility.getMigratorInstance(migratorClassName,
                            targetAuthManager.getIDP());
                    migrator.migrate(userDetails);
                    break;
                }
            }
        }
        catch (final AuthenticationException e)
        {
            LOGGER.debug(e.getMessage(), e);
            throw new MigratorException(e);
        }
    }

    /**
     * This method checks if the user specified by the user details satisfies
     * the migration rule of the target domain.
     *
     * @param userDetails
     * @return
     * @throws MigrationRuleException
     */
    public static boolean isStatisfyMigrationRule(final UserDetails userDetails) throws MigrationRuleException
    {
        boolean stistifiesRule = false;
        try
        {
            final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
            final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

            for (final Properties properties : migrationProperties)
            {
                final String targetIdpName = (String) properties
                        .get(edu.wustl.migrator.util.Constants.TARGET_IDP_TAG_NAME);

                if (targetIdpName.equals(userDetails.getTargetIDP()))
                {
                    final String ruleClassName = (String) properties
                            .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);
                    final RuleInterface rule = (RuleInterface) edu.wustl.common.util.Utility
                            .getObject(ruleClassName);

                    final LoginCredentials baseIdpCredentials = new LoginCredentials();
                    baseIdpCredentials.setLoginName(userDetails.getLoginName());
                    if (rule.checkMigrationRules(baseIdpCredentials))
                    {
                        stistifiesRule = true;
                        break;
                    }
                }
            }
        }
        catch (final AuthenticationException e)
        {
            LOGGER.debug(e.getMessage(), e);
            throw new MigrationRuleException(e);
        }
        return stistifiesRule;
    }

}
