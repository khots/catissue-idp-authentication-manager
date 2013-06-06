/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.domain;

import edu.wustl.migrator.MigrationState;

public class UserDetails
{
    private String loginName;

    private String wustlKey;

    private MigrationState migrationState;

    private String targetIDP;

    public String getTargetIDP()
    {
        return targetIDP;
    }

    public void setTargetIDP(final String targetIDP)
    {
        this.targetIDP = targetIDP;
    }

    public String getMigratedLoginName()
    {
        return wustlKey;
    }

    public void setMigratedLoginName(final String migratedLoginName)
    {
        wustlKey = migratedLoginName;
    }

    public MigrationState getMigrationState()
    {
        return migrationState;
    }

    public void setMigrationState(final MigrationState migrationState)
    {
        this.migrationState = migrationState;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(final String loginName)
    {
        this.loginName = loginName;
    }
}
