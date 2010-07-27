/**
 *
 */
package edu.wustl.abstractidp;

import java.util.Properties;

/**
 * @author supriya_dankh
 *
 */
public abstract class AbstractIDP implements IAbstractIDP {
	private String name;
	private String displayName;
	private boolean defaultIdp;
	private Properties userProperties;
	private Properties idpProperties;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public boolean isDefaultIdp() {
		return defaultIdp;
	}

	public void setDefaultIdp(final boolean defaultIdp) {
		this.defaultIdp = defaultIdp;
	}

	public Properties getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(final Properties userProperties) {
		this.userProperties = userProperties;
	}

	public Properties getIdpProperties() {
		return idpProperties;
	}

	public void setIdpProperties(final Properties idpProperties) {
		this.idpProperties = idpProperties;
	}

    public Properties getIDPProperties()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isDefaultIDP()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
