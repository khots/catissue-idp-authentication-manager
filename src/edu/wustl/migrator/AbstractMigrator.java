package edu.wustl.migrator;

import java.util.List;

import edu.wustl.abstractidp.IAbstractIDP;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;

/**
 * Abstract class providing basic implementation of some of the methods of the
 * implementing interface.
 *
 * @author niharika_sharma
 *
 */
public abstract class AbstractMigrator implements IAbstractMigrator
{

    /** The target domain. */
    private final IAbstractIDP targetDomain;

    /**
     * Instantiates a new abstract migrator.
     *
     * @param targetDomain
     *            the target domain
     */
    public AbstractMigrator(final IAbstractIDP targetDomain)
    {
        this.targetDomain = targetDomain;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * edu.wustl.migrator.IAbstractMigrator#migrate(edu.wustl.domain.UserDetails
     * )
     */
    public void migrate(final UserDetails userDetails) throws MigratorException
    {
        try
        {
            final String queryStr = "INSERT INTO CSM_MIGRATE_USER VALUES" + "('" + userDetails.getLoginName()
                    + "','" + userDetails.getMigratedLoginName() + "','" + targetDomain.getName() + "','"
                    + MigrationState.MIGRATED + "')";
            Utility.executeQueryUsingDataSource(queryStr, true, "WUSTLKey");
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.wustl.migrator.IAbstractMigrator#getCSMName(java.lang.String)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see edu.wustl.migrator.IAbstractMigrator#neverMigrate(java.lang.String)
     */
    public void neverMigrate(final String loginName) throws MigratorException
    {
        try
        {
            final String queryStr = "INSERT INTO CSM_MIGRATE_USER VALUES" + "('" + loginName + "',null,'"
                    + targetDomain.getName() + "','" + MigrationState.DO_NOT_MIGRATE + "')";
            Utility.executeQueryUsingDataSource(queryStr, true, "WUSTLKey");
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }
    }
}
