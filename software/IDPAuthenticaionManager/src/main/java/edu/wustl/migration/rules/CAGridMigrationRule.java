/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migration.rules;

import edu.wustl.domain.LoginCredentials;

public class CAGridMigrationRule implements RuleInterface
{

    public CAGridMigrationRule()
    {
    }

    public boolean checkMigrationRules(LoginCredentials loginCredentials)
    {

        return true;
    }

}
