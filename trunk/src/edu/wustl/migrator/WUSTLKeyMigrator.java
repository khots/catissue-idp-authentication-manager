package edu.wustl.migrator;

import edu.wustl.abstractidp.IAbstractIDP;


public class WUSTLKeyMigrator extends AbstractMigrator {

	public WUSTLKeyMigrator(final IAbstractIDP targetDomain)
	{
	    super(targetDomain);
	}

	@Override
    public void finalize() throws Throwable {

	}
}