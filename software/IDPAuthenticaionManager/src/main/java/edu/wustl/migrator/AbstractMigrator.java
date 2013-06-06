/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator;

import java.util.ArrayList;
import java.util.List;

import org.globus.gsi.GlobusCredential;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.UserDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;
import edu.wustl.dao.exception.DAOException;

/**
 * Abstract class providing basic implementation of some of the methods of the
 * implementing interface.
 *
 * @author niharika_sharma
 *
 */
public abstract class AbstractMigrator implements MigratorInterface
{

    /** The target domain. */
    private final IDPInterface targetDomain;

    /**
     * Instantiates a new abstract migrator.
     *
     * @param targetDomain
     *            the target domain
     */
    public AbstractMigrator(final IDPInterface targetDomain)
    {
        this.targetDomain = targetDomain;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * edu.wustl.migrator.IAbstractMigrator#migrate(edu.wustl.domain.UserDetails
     * )
     */
    public void migrate(final UserDetails userDetails) throws MigratorException, DAOException
    {
        executeMigrationInsertQuery(userDetails, MigrationState.MIGRATED);
    }

    private void executeMigrationInsertQuery(final UserDetails userDetails, final MigrationState state)
            throws MigratorException, DAOException
    {
        try
        {
            
        	 final IDPAuthManager authManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
        			 userDetails.getTargetIDP());
        	 final LoginCredentials loginCredentials = new LoginCredentials();
             loginCredentials.setLoginName(userDetails.getMigratedLoginName());
             loginCredentials.setPassword(userDetails.getPassword());
             final String identity = authManager.getIdentity(loginCredentials);
             
        	
        	final String queryStr = "INSERT INTO CSM_MIGRATE_USER(LOGIN_NAME,MIGRATED_LOGIN_NAME, TARGET_IDP_NAME, MIGRATION_STATUS , IDENTITY ) VALUES"
                    + "(?,?,?,?,?)";
            final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
            final ColumnValueBean loginNameBean = new ColumnValueBean(userDetails.getLoginName());
            final ColumnValueBean migratedLoginNameBean = new ColumnValueBean(userDetails.getMigratedLoginName());
            final ColumnValueBean targetDomainNameBean = new ColumnValueBean(targetDomain.getName());
            final ColumnValueBean migrationStatusBean = new ColumnValueBean(state.getState());
            final ColumnValueBean identityBean = new ColumnValueBean(identity);

            parameters.add(loginNameBean);
            parameters.add(migratedLoginNameBean);
            parameters.add(targetDomainNameBean);
            parameters.add(migrationStatusBean);
            parameters.add(identityBean);
            
            Utility.executeQueryUsingDataSource(queryStr, parameters, true, "WUSTLKey");
        }
        // ------Niranjan's changes start here @Bugid 19485
        catch(final DAOException d)
        {
        	throw d;
        }
      //-------Niranjan's changes end here
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        } catch (AuthenticationException e) {
			// TODO Auto-generated catch block
        	throw new MigratorException(e);
		}
    }

    /*
     * (non-Javadoc)
     *
     * @see edu.wustl.migrator.IAbstractMigrator#neverMigrate(java.lang.String)
     */
    public void neverMigrate(final String loginName) throws MigratorException
    {
        try
        {
            final String queryStr = "INSERT INTO CSM_MIGRATE_USER(LOGIN_NAME,MIGRATED_LOGIN_NAME, TARGET_IDP_NAME, MIGRATION_STATUS) VALUES"
                    + "(?,?,?,?)";
            final List<ColumnValueBean> parameters = new ArrayList<ColumnValueBean>();
            final ColumnValueBean loginNameBean = new ColumnValueBean(loginName);
            final ColumnValueBean migratedLoginNameBean = new ColumnValueBean(null);
            final ColumnValueBean targetDomainNameBean = new ColumnValueBean(targetDomain.getName());
            final ColumnValueBean migrationStatusBean = new ColumnValueBean(MigrationState.DO_NOT_MIGRATE
                    .getState());

            parameters.add(loginNameBean);
            parameters.add(migratedLoginNameBean);
            parameters.add(targetDomainNameBean);
            parameters.add(migrationStatusBean);
            Utility.executeQueryUsingDataSource(queryStr, parameters, true, "WUSTLKey");
        }
        catch (final ApplicationException appException)
        {
            throw new MigratorException(appException);
        }
    }
}
