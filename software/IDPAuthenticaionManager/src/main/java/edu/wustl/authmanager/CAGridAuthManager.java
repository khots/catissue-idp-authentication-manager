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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.NamingException;

import org.globus.gsi.GlobusCredential;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.IDPInterface;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

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
	 private String dorianUrl = "";

	 public static Properties serviceUrls() {			
			String serviceUrlsFile = System.getProperty("grid.service.urlsFilePath");
			
			InputStream in = null;
			Properties serviceUrls = new Properties();
			try {
				in = new FileInputStream(serviceUrlsFile);
				serviceUrls.load(in);
			} catch (IOException e) {
				e.printStackTrace();
				serviceUrls = null;		
			}
			return serviceUrls;
		}
		
	 	protected void initialize(final LoginCredentials loginCredentials) throws NamingException
	    {
	    	 dorianUrl = serviceUrls().getProperty("cagrid.master.dorian.service.url");
	    }

	    /**
	     * This method authenticates user against LDAP.
	     */
	    public boolean authenticate(final LoginCredentials loginCredentials) throws AuthenticationException
	    {

	        try
	        {
	            initialize(loginCredentials);
	            GridAuthenticationClient.authenticate(loginCredentials, dorianUrl, dorianUrl);
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
	            initialize(loginCredentials);
	            GlobusCredential credential = GridAuthenticationClient.authenticate(loginCredentials, dorianUrl, dorianUrl);
	            if (credential != null) {
	            	identity = credential.getIdentity();
	            }
	        }
	        catch (final Exception e)
	        {
	            throw new AuthenticationException(e);
	        }
	        return identity;
		}
		@Override
		public SAMLAssertion getSAMLAssertion(LoginCredentials loginCredentials)
				throws AuthenticationException {
			// TODO Auto-generated method stub
			SAMLAssertion saml  = null;
			try
	        {
	            initialize(loginCredentials);
	             saml = GridAuthenticationClient.getSAMLAssertion(loginCredentials, dorianUrl, dorianUrl);
	        }
	        catch (final Exception e)
	        {
	            throw new AuthenticationException(e);
	        }
	        return saml;
		}

}
