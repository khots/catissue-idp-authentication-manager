/**
 *
 */
package edu.wustl.abstractidp;

import java.util.Properties;

/**
 * @author supriya_dankh
 *
 */
public interface IAbstractIDP {

	public String getName();
    public String getDisplayName();
    public Properties getIDPProperties();
    public Properties getUserProperties();
    public boolean isDefaultIDP();

}
