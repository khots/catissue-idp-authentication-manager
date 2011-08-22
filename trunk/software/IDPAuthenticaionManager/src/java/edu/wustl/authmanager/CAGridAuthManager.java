package edu.wustl.authmanager;

import javax.naming.NamingException;

import org.globus.gsi.GlobusCredential;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.util.Constants;

public class CAGridAuthManager extends IDPAuthManagerImpl
{
	 public CAGridAuthManager(final IDPInterface idp)
	    {
	        super(idp);
	        this.idp = idp;
	    }
	 public CAGridAuthManager()
	    {
	    }
	 private String authenticationURL = "";
	 private String dorianUrl = "";
	 private String syncDescFile="";

	  /**
	     * This method initializes LDAP context.
	     *
	     * @throws NamingException
	     *             Throws naming exception in case of any error while
	     *             initializing.
	     */
//	 public static void main(String[] args)
//	 {
//		 LoginCredentials credentials = new LoginCredentials();
//		 credentials.setLoginName("nitesh");
//		 credentials.setPassword("Haanji^$!6");
//		 try {
//			authenticate(credentials);
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	 }
	    protected void initialize() throws NamingException
	    {
	    	 authenticationURL =idp.getIDPProperties().get(Constants.AUTH_SERVICE_URL).toString();
	    	 //"https://dorian.training.cagrid.org:8443/wsrf/services/cagrid/Dorian";
//	    	 dorianUrl = "https://dorian.training.cagrid.org:8443/wsrf/services/cagrid/Dorian";
	    	 dorianUrl = idp.getIDPProperties().get(Constants.DORIAN_SERVICE_URL).toString();
	    	 syncDescFile = idp.getIDPProperties().getProperty(Constants.SYNC_DESC_FILE);

	    }

	    /**
	     * This method authenticates user against LDAP.
	     */
	    public boolean authenticate(final LoginCredentials loginCredentials) throws AuthenticationException
	    {

	        try
	        {
	            initialize();
//	            synchronizePeriodic("");
//	            synchronizeOnce("");
	            GridAuthenticationClient.synchronizeOnce(syncDescFile);
	            GridAuthenticationClient.authenticate(loginCredentials, dorianUrl, authenticationURL);
//	            validateUser(loginCredentials, dorianUrl, authenticationURL);
	        }
	        catch (final Exception e)
	        {
	            throw new AuthenticationException(e);
	        }
	        return true;
	    }
		@Override
		public String getIdentity(LoginCredentials loginCredentials)
				throws AuthenticationException {
			String identity = null;
			try
	        {
	            initialize();
//	            synchronizePeriodic("");
//	            synchronizeOnce("");
	            GridAuthenticationClient.synchronizeOnce(syncDescFile);
	            GlobusCredential credential = GridAuthenticationClient.authenticate(loginCredentials, dorianUrl, authenticationURL);
	            if (credential != null) {
	            	identity = credential.getIdentity();
	            }
//	            validateUser(loginCredentials, dorianUrl, authenticationURL);
	        }
	        catch (final Exception e)
	        {
	            throw new AuthenticationException(e);
	        }
	        return identity;
		}

//	    public static boolean synchronizePeriodic(String syncDescriptionFile)
//		{
//			boolean success = false;
//			try
//			{
//				// Load Sync Description
//				SyncDescription description = (SyncDescription) Utils
//						.deserializeDocument(syncDescriptionFile,
//								SyncDescription.class);
//
//				// Sync with the Trust Fabric Once
//				SyncGTS.getInstance().syncAndResyncInBackground(description, success);//syncAndResync(description, success);
//				success = true;
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//
//			return success;
//		}
//		private GlobusCredential validateUser(LoginCredentials loginCredentials,String dorianURL, String authenticationURL) throws Exception
//		{
//			BasicAuthentication auth = new BasicAuthentication();
//	        auth.setUserId(loginCredentials.getLoginName());
//	        auth.setPassword(loginCredentials.getPassword());
//
//	        // Authenticate to the IdP (DorianIdP) using credential
//
//	        AuthenticationClient authClient = new AuthenticationClient(authenticationURL);
//	        SAMLAssertion saml = authClient.authenticate(auth);
//
//	        // Requested Grid Credential lifetime (12 hours)
//
//	        CertificateLifetime lifetime = new CertificateLifetime();
//	        lifetime.setHours(12);
//
//	        // Request PKI/Grid Credential
//	        GridUserClient dorian = new GridUserClient(dorianURL);
//	        GlobusCredential credential = dorian.requestUserCertificate(saml, lifetime);
//	        return credential;
//
//		}
////
////		// From: http://cagrid.org/display/gts12/Programmatic+Approach
////		// Purpose: obtain trusted CA certificates from Slave GTS service
//		public static boolean synchronizeOnce(String syncDescriptionFile)
//		{
//
//			boolean success = false;
//			try
//			{
//				// Load Sync Description
////				String pathToSyncDescription = "C:/sync-description.xml";
//				SyncDescription description = (SyncDescription) Utils
//						.deserializeDocument(syncDescriptionFile,
//								SyncDescription.class);
//
//				// Sync with the Trust Fabric Once
//				SyncGTS.getInstance().syncOnce(description);
//				success = true;
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//
//			return success;
//		}
//
//
//		private static boolean synchronizePeriodic(String syncDescriptionFile)
//		{
//			boolean success = false;
//			try
//			{
//				// Load Sync Description
//				String pathToSyncDescription = "C:/sync-description.xml"; // "conf/training/sync-description.xml";
//				SyncDescription description = (SyncDescription) Utils
//						.deserializeDocument(pathToSyncDescription,
//								SyncDescription.class);
//
//				// Sync with the Trust Fabric Once
//				SyncGTS.getInstance().syncAndResyncInBackground(description, success);//syncAndResync(description, success);
//				success = true;
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//
//			return success;
//		}

//		public static void main(String[] args)
//		 {
//			 LoginCredentials credentials = new LoginCredentials();
//			 credentials.setLoginName("nitesh");
//			 credentials.setPassword("Haanji^$!6");
//			 try {
//				authenticate(credentials,"https://dorian.training.cagrid.org:8443/wsrf/services/cagrid/Dorian","https://dorian.training.cagrid.org:8443/wsrf/services/cagrid/Dorian");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		 }






}
