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

	String getName();
    String getDisplayName();
    Properties getIDPProperties();
    Properties getUserProperties();
    boolean isDefaultIDP();

}
