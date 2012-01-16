package edu.wustl.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.exception.ApplicationException;
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
import edu.wustl.wustlkey.util.global.Constants;

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

    public static boolean authenticate(final LoginCredentials loginCredentials) throws AuthenticationException, ApplicationException
    {
        boolean isAuthentic;

        final IDPAuthManager authManager = getAuthenticationManager(loginCredentials,
                getUserDetails(loginCredentials.getLoginName()));

        if (authManager == null)
        {
            isAuthentic = false;
        }
        else
        {
        	// Changes made for allowing loginName to be case insensitive.
        	// Retrieve loginName from database and set to loginCredentials.
        	final String queryStr = "SELECT * FROM CATISSUE_USER WHERE UPPER(LOGIN_NAME) = ?";
        	final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
            final ColumnValueBean columnValueBean = new ColumnValueBean(loginCredentials.getLoginName().toUpperCase());
            parameters.add(columnValueBean);
            
            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, parameters, false,
                     CommonServiceLocator.getInstance().getAppName());
            if (resultList != null && !resultList.isEmpty() && "Active".equalsIgnoreCase(resultList.get(0).get(6)))
            {
            	loginCredentials.setLoginName(resultList.get(0).get(4));
            }
            isAuthentic = authManager.authenticate(loginCredentials);
        }

        return isAuthentic;
    }

    private static IDPAuthManager getAuthenticationManager(final LoginCredentials loginCredentials,
            final UserDetails userDetails) throws AuthenticationException
    {
        IDPAuthManager authManager = null;
        try
        {
            if (userDetails == null)
            {
                if (isUserPresentInApplicationDB(loginCredentials))
                {
                    authManager = AuthManagerFactory.getInstance().getAuthManagerInstance();
                }
            }
            else
            {
                if (MigrationState.MIGRATED.equals(userDetails.getMigrationState()))
                {
                    authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
                            Constants.WUSTL_IDP);
                }
                else
                {
                    authManager = AuthManagerFactory.getInstance().getAuthManagerInstance();
                }
            }
        }
        catch (final AuthenticationException e)
        {
            LOGGER.debug(e);
            throw new AuthenticationException(e);
        }
        catch (final ApplicationException e)
        {
            LOGGER.debug(e);
            throw new AuthenticationException(e);
        }
        return authManager;
    }

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
    public static LoginResult processUserLogin(final LoginCredentials loginCredentials)
            throws AuthenticationException
    {
        final LoginResult loginResult;
        System.out.println();
        final UserDetails userDetails = getUserDetails(loginCredentials.getLoginName());

        if (userDetails == null)
        {
            loginResult = processUserLoginWithoutMigrationInfo(loginCredentials);
        }
        else
        {
            loginResult = processUserLoginWithMigrationInfo(loginCredentials, userDetails);
        }
        final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
        if (!sourceIdp.isMigrationEnabled())
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
            if (isUserPresentInApplicationDB(loginCredentials))
            {
                final boolean isMigrationApplicable = checkIfMigrationIsApplicable(loginCredentials);

                if (isMigrationApplicable)
                {
                    loginResult.setMigrationState(MigrationState.TO_BE_MIGRATED);
                }
                else
                {
                    loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
                }
                loginResult.setAuthenticationSuccess(true);
            }
            else if (!loginCredentials.getLoginName().contains("@"))
            {
                // New user
                loginResult.setMigrationState(MigrationState.NEW_IDP_USER);
                loginResult.setAuthenticationSuccess(true);
            }
            else
            {
            	loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
            	loginResult.setAuthenticationSuccess(false);
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

    private static boolean checkIfMigrationIsApplicable(final LoginCredentials loginCredentials)
            throws AuthenticationException
    {
        boolean isMigrationApplicable = false;
        final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
        final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

        for (final Properties properties : migrationProperties)
        {
            final String ruleClassName = (String) properties
                    .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);

            final RuleInterface rule = (RuleInterface) edu.wustl.common.util.Utility.getObject(ruleClassName);
            if (rule.checkMigrationRules(loginCredentials))
            {
                isMigrationApplicable = true;
                break;
            }
        }
        return isMigrationApplicable;
    }

    private static boolean isUserPresentInApplicationDB(final LoginCredentials loginCredentials)
            throws ApplicationException
    {
        final String queryStr = "SELECT * FROM CATISSUE_USER WHERE UPPER(LOGIN_NAME) = ?";
        final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();

        final ColumnValueBean columnValueBean = new ColumnValueBean(loginCredentials.getLoginName().toUpperCase());
        parameters.add(columnValueBean);
        final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, parameters, false,
                CommonServiceLocator.getInstance().getAppName());
        boolean isPresent;
        if (resultList == null)
        {
            isPresent = false;
        }
        else if(!"Active".equalsIgnoreCase(resultList.get(0).get(6)))
		{
    		isPresent = false;
		}
    	else
    	{
    		isPresent = true;
    	}
        return isPresent;
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

         loginResult.setAppLoginName(userDetails.getLoginName());
         loginResult.setMigratedLoginName(userDetails.getMigratedLoginName());
         loginResult.setAuthenticationSuccess(true);
         loginResult.setMigrationState(userDetails.getMigrationState());
         if(userDetails.getMigrationState().equals(MigrationState.MIGRATED) && !loginCredentials.getLoginName().equalsIgnoreCase(userDetails.getMigratedLoginName()))
         {
        	 // user is migrated but has used his application id to login
        	 loginResult.setAuthenticationSuccess(false);
         }
        return loginResult;
    }

    /**
     * Gets the user details.
     *
     * @param loginName
     *            the login name
     *
     * @return the user details
     * @throws AuthenticationException
     */
    private static UserDetails getUserDetails(final String loginName) throws AuthenticationException
    {
        UserDetails userDetails = null;
        try
        {
        	final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
            if (sourceIdp.isMigrationEnabled())
            {
                final String queryStr = "SELECT LOGIN_NAME,WUSTLKEY FROM CSM_MIGRATE_USER WHERE UPPER(WUSTLKEY) = ? or UPPER(LOGIN_NAME)=?";

                final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
                final ColumnValueBean loginNameBean = new ColumnValueBean(loginName.toUpperCase());
                final ColumnValueBean migratedLoginNameBean = new ColumnValueBean(loginName.toUpperCase());

                parameters.add(loginNameBean);
                parameters.add(migratedLoginNameBean);
                final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, parameters,
                        false, "WUSTLKey");

                if (resultList != null)
                {
                    userDetails = new UserDetails();
                    final List<String> userDetailsRecord = resultList.get(0);
                    userDetails.setLoginName(userDetailsRecord.get(0));
                    String migratedLoginName =userDetailsRecord.get(1);
					userDetails.setMigratedLoginName(migratedLoginName);

                    if(migratedLoginName==null || "".equals(migratedLoginName))
                    {
                    	userDetails.setMigrationState(MigrationState.DO_NOT_MIGRATE);
                    }
                    else
                    {
                    	userDetails.setMigrationState(MigrationState.MIGRATED);
                    }

                }
            }
        }
        catch (final ApplicationException appException)
        {
            LOGGER.debug(appException);
        }
        return userDetails;
    }

}
