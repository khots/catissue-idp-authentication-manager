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
public interface IDPInterface {

	String getName();
    Properties getIDPProperties();
    Properties getUserProperties();
    List<Properties> getMigrationProperties();
    void setName(String name);
    void setIDPProperties(Properties idpProperties);
    void setUserProperties(Properties userProperties);
    void setMigrationProperties(List<Properties> migrationProperties);
    boolean isMigrationEnabled();
    void setMigrationEnabled(boolean migrationEnabled);



}
