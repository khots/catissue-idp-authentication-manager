package edu.wustl.processor;

import java.util.List;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.CSMIDPAuthManager;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.LDAPAuthManager;
import edu.wustl.domain.LoginResult;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.util.Utility;

public class LoginProcessor
{
    public static LoginResult processUserLogin(final String loginName, final String password)
    {
        final LoginResult loginResult;
        final UserDetails userDetails = getUserDetails(loginName);
        if (userDetails == null)
        {
            loginResult = processUserLoginWithoutMigrationInfo(loginName, password);
        }
        else
        {
            loginResult = processUserLoginWithMigrationInfo(loginName, password, userDetails);
        }
        return loginResult;
    }

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
                final IDPAuthManager authManager = new CSMIDPAuthManager();
                loginResult.setAuthenticationSuccess(authManager.authenticate(loginName, password));
                loginResult.setMigrationState(MigrationState.TO_BE_MIGRATED);
            }
        }
        catch (final ApplicationException e)
        {
            //
        }
        catch (AuthenticationException e)
        {

        }
        loginResult.setLoginId(loginName);
        return loginResult;
    }

    private static LoginResult processUserLoginWithMigrationInfo(final String loginName, final String password,
            final UserDetails userDetails)
    {
        final LoginResult loginResult = new LoginResult();
        final IDPAuthManager authManager;
        if (MigrationState.MIGRATED.equals(userDetails.getMigrationState()))
        {
            loginResult.setMigrationState(MigrationState.MIGRATED);
            loginResult.setLoginId(userDetails.getMigratedLoginName());
            authManager = new LDAPAuthManager();
        }
        else
        {
            loginResult.setLoginId(userDetails.getLoginName());
            loginResult.setMigrationState(MigrationState.DO_NOT_MIGRATE);
            authManager = new CSMIDPAuthManager();
        }
        try
        {
        loginResult.setAuthenticationSuccess(authManager.authenticate(loginName, password));
        }catch (AuthenticationException e)
        {

        }

        return loginResult;
    }

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
