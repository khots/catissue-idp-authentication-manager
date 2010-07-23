package edu.wustl.migrator;

import java.util.List;

import edu.wustl.authmanager.IAbstractIDP;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;

public abstract class AbstractMigrator implements IAbstractMigrator
{
    private final IAbstractIDP targetDomain;

    public AbstractMigrator(final IAbstractIDP targetDomain)
    {
        this.targetDomain = targetDomain;
    }

    public void migrate(final UserDetails userDetails) throws MigratorException
    {
        try
        {
            final String queryStr = "INSERT INTO CSM_MIGRATE_USER VALUES" + "('" + userDetails.getLoginName()
                    + "','" + targetDomain.getName() + "','" + userDetails.getMigratedLoginName() + "','"
                    + MigrationState.MIGRATED + "')";
            Utility.executeQueryUsingDataSource(queryStr, true, "WUSTLKey");
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }
    }

    public void checkMigrationRules(final UserDetails userDetails) throws MigratorException
    {
        // TODO Auto-generated method stub

    }

    public String getCSMName(final String migratedLoginName) throws MigratorException
    {
        String csmName = null;
        try
        {
            final String queryStr = "SELECT LOGIN_NAME FROM CSM_MIGRATE_USER WHERE MIGRATED_LOGIN_NAME = '"
                    + migratedLoginName + "'";

            final List<List<String>> resultList = Utility.executeQueryUsingDataSource(queryStr, false, "WUSTLKey");

            if (resultList != null)
            {
                final List<String> wuKey = resultList.get(0);
                csmName = wuKey.get(0).toString();
            }
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }

        return csmName;
    }

    public void neverMigrate(final UserDetails userDetails) throws MigratorException
    {
        try
        {
            final String queryStr = "INSERT INTO CSM_MIGRATE_USER VALUES" + "('" + userDetails.getLoginName()
                    + "','" + targetDomain.getName() + "',null,'" + MigrationState.DO_NOT_MIGRATE + "')";
            Utility.executeQueryUsingDataSource(queryStr, true, "WUSTLKey");
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }

    }

}
