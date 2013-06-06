/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.idp;

import java.util.Properties;

import edu.wustl.migrator.MigrationState;
import edu.wustl.migrator.exception.MigratorException;

/**
 * @version 1.0
 * @created 21-Jul-2010 3:22:19 PM
 */
public class WustlKeyIDP extends AbstractBaseIDP {

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