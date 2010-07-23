/**
 *
 */
package edu.wustl.domain;

import edu.wustl.migrator.MigrationState;

/**
 * @author supriya_dankh
 *
 */
public class LoginResult
{ /** The authentication success. */
    private boolean authenticationSuccess;

    /** The cider login id. */
    private String loginId;

    public boolean isAuthenticationSuccess() {
		return authenticationSuccess;
	}

	public void setAuthenticationSuccess(boolean authenticationSuccess) {
		this.authenticationSuccess = authenticationSuccess;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String ciderLoginId) {
		this.loginId = ciderLoginId;
	}

	public MigrationState getMigrationState() {
		return migrationState;
	}

	public void setMigrationState(MigrationState migrationState) {
		this.migrationState = migrationState;
	}

	/** The migration state. */
    private MigrationState migrationState;


}
