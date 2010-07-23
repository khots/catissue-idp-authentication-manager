package edu.wustl.migrator;

import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;



/**
 * @version 1.0
 * @created 21-Jul-2010 3:22:18 PM
 */
public interface IAbstractMigrator {

	/**
	 *
	 * @param userDetails
	 */
	public void checkMigrationRules(UserDetails userDetails) throws MigratorException;

	/**
	 *
	 * @param key
	 */
	public String getCSMName(String key) throws MigratorException;

	/**
	 *
	 * @param toIDP
	 * @param fromIDP
	 * @param userDetails
	 */
	public void migrate(UserDetails userDetails) throws MigratorException;

	/**
	 *
	 * @param loginID
	 */
	public void neverMigrate(UserDetails userDetails) throws MigratorException;

}