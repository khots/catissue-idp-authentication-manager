package edu.wustl.migrator;

import edu.wustl.idp.IDPInterface;

/**
 * This class defines the migrator for WUSTLKey idp.
 *
 * @author niharika_sharma
 *
 */
public class WUSTLKeyMigrator extends AbstractMigrator
{

    public WUSTLKeyMigrator(final IDPInterface targetDomain)
    {
        super(targetDomain);
    }

    @Override
    public void finalize() throws Throwable
    {

    }
}