/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.authmanager;

import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.CertificateLifetime;
import org.globus.gsi.GlobusCredential;

import edu.wustl.domain.LoginCredentials;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.syncgts.bean.SyncDescription;
import gov.nih.nci.cagrid.syncgts.core.SyncGTS;

public class GridAuthenticationClient
{

//	public static void main(String[] args)
//	 {
//		 LoginCredentials credentials = new LoginCredentials();
//		 credentials.setLoginName("nitesh");
//		 credentials.setPassword("Haanji^$!6");
//		 try {
//			 synchronizeOnce("c:/sync-description.xml");
//			authenticate(credentials,"https://dorian.training.cagrid.org:8443/wsrf/services/cagrid/Dorian","https://dorian.training.cagrid.org:8443/wsrf/services/cagrid/Dorian");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	 }

	public static boolean synchronizePeriodic(String syncDescriptionFile)
	{
		boolean success = false;
		try
		{
			// Load Sync Description
//			String pathToSyncDescription = "C:/sync-description.xml"; // "conf/training/sync-description.xml";
			SyncDescription description = (SyncDescription) Utils
					.deserializeDocument(syncDescriptionFile,
							SyncDescription.class);

			// Sync with the Trust Fabric Once
			SyncGTS.getInstance().syncAndResyncInBackground(description, success);//syncAndResync(description, success);
			success = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return success;
	}

	public static boolean synchronizeOnce(String syncDescriptionFile) throws Exception
	{

		boolean success = false;
			// Load Sync Description
			SyncDescription description = (SyncDescription) Utils
					.deserializeDocument(syncDescriptionFile,
							SyncDescription.class);

			// Sync with the Trust Fabric Once
			SyncGTS.getInstance().syncOnce(description);
			success = true;

		return success;
	}

	public static GlobusCredential authenticate(LoginCredentials loginCredentials,String dorianURL, String authenticationURL)throws Exception
	{
		 BasicAuthentication auth = new BasicAuthentication();
	        auth.setUserId(loginCredentials.getLoginName());
	        auth.setPassword(loginCredentials.getPassword());

	        // Authenticate to the IdP (DorianIdP) using credential

	        AuthenticationClient authClient = new AuthenticationClient(authenticationURL);
	        SAMLAssertion saml = authClient.authenticate(auth);

	        // Requested Grid Credential lifetime (12 hours)

	        CertificateLifetime lifetime = new CertificateLifetime();
	        lifetime.setHours(12);

	        // Request PKI/Grid Credential
	        GridUserClient dorian = new GridUserClient(dorianURL);
	        GlobusCredential credential = dorian.requestUserCertificate(saml, lifetime);
	        return credential;
	}
	
	public static SAMLAssertion getSAMLAssertion(LoginCredentials loginCredentials,String dorianURL, String authenticationURL)throws Exception
	{
		 BasicAuthentication auth = new BasicAuthentication();
	        auth.setUserId(loginCredentials.getLoginName());
	        auth.setPassword(loginCredentials.getPassword());

	        // Authenticate to the IdP (DorianIdP) using credential

	        AuthenticationClient authClient = new AuthenticationClient(authenticationURL);
	        SAMLAssertion saml = authClient.authenticate(auth);

	        // Requested Grid Credential lifetime (12 hours)

	        CertificateLifetime lifetime = new CertificateLifetime();
	        lifetime.setHours(12);

	        // Request PKI/Grid Credential
	        GridUserClient dorian = new GridUserClient(dorianURL);
	        GlobusCredential credential = dorian.requestUserCertificate(saml, lifetime);
	        return saml;
	}
	
	
}
