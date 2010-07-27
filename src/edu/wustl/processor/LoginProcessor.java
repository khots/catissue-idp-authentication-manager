package edu.wustl.processor;

import java.util.List;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.CSMAuthManager;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.LDAPAuthManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.domain.LoginResult;
import edu.wustl.domain.UserDetails;
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
     * Process user login.
     *
     * @param loginName
     *            the login name
     * @param password
     *            the password
     *
     * @return the login result
     */
    public static LoginResult processUserLogin(final String loginName, final String password)
    {
        final LoginResult loginResult;
        UserDetails userDetails;
        if (Boolean.parseBoolean(XMLPropertyHandler.getValue("idp.enabled")))
        {
            userDetails = getUserDetails(loginName);
        }
        else
        {
            userDetails = null;
        }

        if (userDetails == null)
        {
            loginResult = processUserLoginWithoutMigrationInfo(loginName, password);
        }
        else
        {
            loginResult = processUserLoginWithMigrationInfo(loginName, password, userDetails);
        }
        System.out.println("");
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
     */
    private static LoginResult processUserLoginWithoutMigrationInfo(final String loginName, final String password)
    {
        final LoginResult loginResult = new LoginResult();
        try
        {
            final String queryStr = "SELECT * FROM CSM_USER WHERE LOGIN_NAME = '" + loginName + "'";
            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, false, "WUSTLKey");
            if (resultList == null)
            {
                // New user
                loginResult.setMigrationState(MigrationState.NEW_WUSTL_USER);
                loginResult.setAuthenticationSuccess(true);

            }
            else
            {
                final IDPAuthManager authManager = new CSMAuthManager();
                loginResult.setAuthenticationSuccess(authManager.authenticate(loginName, password));
                if (loginName.endsWith(Constants.WUSTL_EDU) || loginName.endsWith(Constants.WUSTL_EDU_CAPS))
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
            //
        }
        catch (final AuthenticationException e)
        {

        }
        loginResult.setAppLoginName(loginName);
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
     */
    private static LoginResult processUserLoginWithMigrationInfo(final String loginName, final String password,
            final UserDetails userDetails)
    {
        final LoginResult loginResult = new LoginResult();
        final IDPAuthManager authManager;
        if (MigrationState.MIGRATED.equals(userDetails.getMigrationState()))
        {
            loginResult.setMigrationState(MigrationState.MIGRATED);
            loginResult.setAppLoginName(userDetails.getLoginName());
            loginResult.setMigratedLoginName(userDetails.getMigratedLoginName());
            authManager = new LDAPAuthManager();
        }
        else
        {
            loginResult.setAppLoginName(userDetails.getLoginName());
            loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
            authManager = new CSMAuthManager();
        }
        try
        {
            loginResult.setAuthenticationSuccess(authManager.authenticate(loginName, password));
        }
        catch (final AuthenticationException e)
        {

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
     */
    private static UserDetails getUserDetails(final String loginName)
    {
        UserDetails userDetails = null;
        try
        {
            final String queryStr = "SELECT LOGIN_NAME, TARGET_IDP_NAME, MIGRATED_LOGIN_NAME,MIGRATION_STATUS FROM CSM_MIGRATE_USER WHERE MIGRATED_LOGIN_NAME = '"
                    + loginName + "' or LOGIN_NAME='" + loginName + "'";

            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, false, "WUSTLKey");

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
            // throw new MigratorException(appException);
        }
        return userDetails;
    }

}
