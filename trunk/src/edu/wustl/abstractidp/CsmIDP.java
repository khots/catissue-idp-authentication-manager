package edu.wustl.abstractidp;

import java.util.List;
import java.util.Properties;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;


/**
 * @version 1.0
 * @created 21-Jul-2010 3:22:18 PM
 */
public class CsmIDP extends AbstractIDP {

    public Properties getIDPProperties()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public MigrationState getUserState(final String loginName) throws MigratorException
    {
        final MigrationState state;
        try
        {
            final String queryStr = "SELECT LOGIN_NAME, TARGET_IDP_NAME, MIGRATED_LOGIN_NAME, MIGRATION_STATUS FROM CSM_MIGRATE_USER WHERE MIGRATED_LOGIN_NAME = '"
                    + loginName + "' or LOGIN_NAME = '"+loginName+"'";

            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, false, "WUSTLKey");

            if(resultList==null)
            {
                //Check for valid csm login name
                state=MigrationState.TO_BE_MIGRATED;
            }
            else
            {
                state=MigrationState.get(resultList.get(0).get(3));
            }
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }
        return state;
    }

}