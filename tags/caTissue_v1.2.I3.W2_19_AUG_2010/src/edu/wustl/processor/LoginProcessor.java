package edu.wustl.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.domain.UserDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migration.rules.RuleInterface;
import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.util.Utility;

/**
 * This class contains the generic workflows in an application supporting single
 * or multiple domains for operations like authentication of users, etc.
 *
 * @author niharika_sharma
 *
 */
public class LoginProcessor
{
    /**
     * Private logger instance.
     */
    private static final Logger LOGGER = Logger.getCommonLogger(LoginProcessor.class);

    /**
     * Process user login.
     *
     * @param loginName
     *            the login name
     * @param password
     *            the password
     *
     * @return the login result
     * @throws AuthenticationException
     */
    public static LoginResult processUserLogin(final LoginCredentials loginCredentials) throws AuthenticationException
    {
        final LoginResult loginResult;
        UserDetails userDetails;
        if (Boolean.parseBoolean(XMLPropertyHandler.getValue("idp.enabled")))
        {
            userDetails = getUserDetails(loginCredentials.getLoginName());
        }
        else
        {
            userDetails = null;
        }

        if (userDetails == null)
        {
            loginResult = processUserLoginWithoutMigrationInfo(loginCredentials);
        }
        else
        {
            loginResult = processUserLoginWithMigrationInfo(loginCredentials, userDetails);
        }
        if (!Boolean.parseBoolean(XMLPropertyHandler.getValue("idp.enabled")))
        {
            loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
        }
        return loginResult;
    }

    /**
     * Process user login without migration info.
     *
     * @param loginName
     *            the login name
     * @param password
     *            the password
     *
     * @return the login result
     * @throws AuthenticationException
     */
    private static LoginResult processUserLoginWithoutMigrationInfo(final LoginCredentials loginCredentials)
            throws AuthenticationException
    {
        final LoginResult loginResult = new LoginResult();
        try
        {
            final String queryStr = "SELECT * FROM CATISSUE_USER WHERE LOGIN_NAME = ?";
            final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();

            final ColumnValueBean columnValueBean = new ColumnValueBean(loginCredentials.getLoginName());
            parameters.add(columnValueBean);
            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, parameters, false,
                    CommonServiceLocator.getInstance().getAppName());
            if (resultList == null)
            {
                // New user
                loginResult.setMigrationState(MigrationState.NEW_WUSTL_USER);
                loginResult.setAuthenticationSuccess(false);

            }
            else
            {

                final IDPAuthManager authManager = AuthManagerFactory.getInstance().getAuthManagerInstance();

                loginResult.setAuthenticationSuccess(authManager.authenticate(loginCredentials));

                boolean isMigrationApplicable = false;

                final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
                final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

                for (final Properties properties : migrationProperties)
                {
                    final String ruleClassName = (String) properties
                            .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);

                    final RuleInterface rule = (RuleInterface) edu.wustl.common.util.Utility
                            .getObject(ruleClassName);
                    if (rule.checkMigrationRules(loginCredentials))
                    {
                        isMigrationApplicable = true;
                        break;
                    }
                }

                if (isMigrationApplicable)
                {
                    loginResult.setMigrationState(MigrationState.TO_BE_MIGRATED);
                }
                else
                {
                    loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
                }

            }
        }
        catch (final ApplicationException e)
        {
            LOGGER.debug(e);
            throw new AuthenticationException(e);

        }
        loginResult.setAppLoginName(loginCredentials.getLoginName());
        return loginResult;
    }

    /**
     * Process user login with migration info.
     *
     * @param loginName
     *            the login name
     * @param password
     *            the password
     * @param userDetails
     *            the user details
     *
     * @return the login result
     * @throws AuthenticationException
     */
    private static LoginResult processUserLoginWithMigrationInfo(final LoginCredentials loginCredentials,
            final UserDetails userDetails) throws AuthenticationException
    {
        final LoginResult loginResult = new LoginResult();
            final IDPAuthManager authManager;
            if (MigrationState.MIGRATED.equals(userDetails.getMigrationState()))
            {
                loginResult.setMigrationState(MigrationState.MIGRATED);
                loginResult.setAppLoginName(userDetails.getLoginName());
                loginResult.setMigratedLoginName(userDetails.getMigratedLoginName());

                authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(userDetails.getTargetIDP());
            }
            else
            {
                loginResult.setAppLoginName(userDetails.getLoginName());
                loginResult.setMigrationState(userDetails.getMigrationState());
                authManager = AuthManagerFactory.getInstance().getAuthManagerInstance();
            }
            loginResult.setAuthenticationSuccess(authManager.authenticate(loginCredentials));
        return loginResult;
    }

    /**
     * Gets the user details.
     *
     * @param loginName
     *            the login name
     *
     * @return the user details
     */
    private static UserDetails getUserDetails(final String loginName)
    {
        UserDetails userDetails = null;
        try
        {
            final String queryStr = "SELECT LOGIN_NAME, TARGET_IDP_NAME, MIGRATED_LOGIN_NAME,MIGRATION_STATUS FROM CSM_MIGRATE_USER WHERE MIGRATED_LOGIN_NAME = ? or LOGIN_NAME=?";

            final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
            final ColumnValueBean loginNameBean = new ColumnValueBean(loginName);
            final ColumnValueBean migratedLoginNameBean = new ColumnValueBean(loginName);

            parameters.add(loginNameBean);
            parameters.add(migratedLoginNameBean);
            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, parameters, false,
                    "WUSTLKey");

            if (resultList != null)
            {
                userDetails = new UserDetails();
                final List<String> userDetailsRecord = resultList.get(0);
                userDetails.setLoginName(userDetailsRecord.get(0).toString());
                userDetails.setTargetIDP(userDetailsRecord.get(1).toString());
                userDetails.setMigratedLoginName(userDetailsRecord.get(2).toString());
                userDetails.setMigrationState(MigrationState.get(userDetailsRecord.get(3).toString()));
            }
        }
        catch (final ApplicationException appException)
        {
            LOGGER.debug(appException);
        }
        return userDetails;
    }

}
