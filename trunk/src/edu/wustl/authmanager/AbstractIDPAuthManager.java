package edu.wustl.authmanager;

import edu.wustl.abstractidp.IAbstractIDP;

public class AbstractIDPAuthManager implements IDPAuthManager
{
    private IAbstractIDP idp;

    public boolean authenticate(final String loginName, final String password)
    {
        return false;
    }
}
