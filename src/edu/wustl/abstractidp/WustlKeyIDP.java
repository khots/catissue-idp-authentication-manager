package edu.wustl.abstractidp;

import java.util.Properties;

import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.exception.MigratorException;

/**
 * @version 1.0
 * @created 21-Jul-2010 3:22:19 PM
 */
public class WustlKeyIDP extends AbstractIDP {

    public WustlKeyIDP()
    {
        super();
        setName("WUSTLKEY_IDP");
    }

    @Override
    public Properties getIDPProperties()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public MigrationState getUserState(final String loginName) throws MigratorException
    {
        return null;
    }

}