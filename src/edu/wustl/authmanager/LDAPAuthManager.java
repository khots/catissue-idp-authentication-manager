/**
 *
 */
package edu.wustl.authmanager;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.directory.SearchControls;
import javax.naming.NamingEnumeration;

import edu.wustl.auth.exception.AuthenticationException;

/**
 * @author supriya_dankh
 *
 */
public class LDAPAuthManager extends IDPAuthManagerImpl {



	transient private DirContext ctx;

	private final Hashtable<Object,Object> env = new Hashtable<Object,Object>();
	protected void initialize() throws NamingException
	{
		env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("java.naming.security.authentication","simple");
		env.put("java.naming.provider.url", "ldap://10.39.225.39:636/");
		env.put("java.naming.security.principal", "CN=cider binduser,OU=medical school,OU=service accounts,DC=wuaddev,DC=wustl,DC=edu");
		env.put("java.naming.security.credentials", "TLdUGv2wS385QxXRo9Km");
		env.put("java.naming.security.protocol", "ssl");

		/*if(domainProperties.getProperty("securityProtocol") != null) {
			env.put("java.naming.security.protocol", domainProperties.getProperty("securityProtocol"));
		}*/

		ctx = new InitialLdapContext(env, null);
	}

	public boolean authenticate(String userName, String password) throws AuthenticationException
 {
		boolean isSuccessful = false;

		try {
			initialize();
			SearchResult searchResult = searchUser(userName, new String[0]);

			if (searchResult != null) {
				env.put("java.naming.security.principal", searchResult
						.getNameInNamespace());
				env.put("java.naming.security.credentials", password);
				new InitialLdapContext(env, null);

				isSuccessful = true;
			}
		} catch (NamingException e) {
           throw new AuthenticationException(e);
		}
		return isSuccessful;
	}

	private SearchResult searchUser(String userName, String[] returnAttributes) throws NamingException{

		SearchResult searchResult = null;


		SearchControls searchCtls = new SearchControls();
		searchCtls.setReturningAttributes(returnAttributes);
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		String searchFilter = "(sAMAccountName=name)";
		String string_to_replace = "name";
		if(searchFilter.indexOf(string_to_replace) != -1)
		{
			searchFilter = searchFilter.replace(string_to_replace, userName);
		}

		String searchBase = "OU=Faculty,OU=Current,OU=People,DC=wuaddev,DC=wustl,DC=edu";
		NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
		while(answer.hasMoreElements())
		{
			searchResult = answer.next();
		}

		return searchResult;
	}



	protected void destroy() throws NamingException
	{
		ctx.close();
	}

}
