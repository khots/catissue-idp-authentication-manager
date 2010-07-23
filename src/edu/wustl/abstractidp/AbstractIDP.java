/**
 *
 */
package edu.wustl.abstractidp;

import java.util.Properties;

/**
 * @author supriya_dankh
 *
 */
public abstract class AbstractIDP {
	private String name;
	private String displayName;
	private boolean defaultIdp;
	private Properties userProperties;
	private Properties idpProperties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isDefaultIdp() {
		return defaultIdp;
	}

	public void setDefaultIdp(boolean defaultIdp) {
		this.defaultIdp = defaultIdp;
	}

	public Properties getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(Properties userProperties) {
		this.userProperties = userProperties;
	}

	public Properties getIdpProperties() {
		return idpProperties;
	}

	public void setIdpProperties(Properties idpProperties) {
		this.idpProperties = idpProperties;
	}

}
