package edu.wustl.migrator;

import edu.wustl.authmanager.IAbstractIDP;

public class WUSTLKeyMigrator extends AbstractMigrator {

	public WUSTLKeyMigrator(final IAbstractIDP targetDomain)
	{
	    super(targetDomain);
	}

	@Override
    public void finalize() throws Throwable {

	}
}