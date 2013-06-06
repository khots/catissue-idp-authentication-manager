/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.idp;

import java.util.List;
import java.util.Properties;

/**
 * @author supriya_dankh
 *
 */
public class AbstractBaseIDP implements IDPInterface {
	private String name;
	private Properties userProperties;
	private Properties idpProperties;
	private List<Properties> migrationProperties;
	private boolean migrationEnabled = false;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Properties getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(final Properties userProperties) {
		this.userProperties = userProperties;
	}

	public Properties getIDPProperties() {
		return this.idpProperties;
	}

	public List<Properties> getMigrationProperties() {
		return this.migrationProperties;

	}

	public void setIDPProperties(Properties idpProperties) {
		this.idpProperties = idpProperties;
	}

	public void setMigrationProperties(List<Properties> migrationProperties) {

		this.migrationProperties = migrationProperties;
	}

	public boolean isMigrationEnabled() {
		return this.migrationEnabled;
	}

	public void setMigrationEnabled(boolean migrationEnabled) {
		this.migrationEnabled = migrationEnabled;

	}

}
