/**
 *
 */
package edu.wustl.migration.rules;

import edu.wustl.domain.LoginCredentials;

/**
 * @author supriya_dankh
 *
 */
public interface RuleInterface {

	public boolean checkMigrationRules(LoginCredentials loginCredentials);

}
