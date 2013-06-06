/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

/**
 *
 */
package edu.wustl.authmanager;

import java.util.Hashtable;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migrator.util.Constants;

/**
 *
 * @author supriya_dankh
 *
 */
public class LDAPAuthManager extends IDPAuthManagerImpl
{

    transient private DirContext ctx;
    private final Hashtable<Object, Object> env = new Hashtable<Object, Object>();

    public LDAPAuthManager(final IDPInterface idp)
    {
        super(idp);
        this.idp = idp;
    }

    public LDAPAuthManager()
    {
    }

    /**
     * This method initializes LDAP context.
     *
     * @throws NamingException
     *             Throws naming exception in case of any error while
     *             initializing.
     */
    protected void initialize() throws NamingException
    {
        env.put("java.naming.factory.initial", idp.getIDPProperties().get(Constants.INIT_CONTEXT_FACTORY));
        env.put("java.naming.security.authentication", idp.getIDPProperties().get(
                Constants.SECURITY_AUTHENTICATION));
        env.put("java.naming.provider.url", idp.getIDPProperties().get(Constants.DIRECTORY));
        env.put("java.naming.security.principal", idp.getIDPProperties().get(Constants.BIND_USER));
        env.put("java.naming.security.credentials", idp.getIDPProperties().get(Constants.BIND_PASSWORD));

        if (!Validator.isEmpty(idp.getIDPProperties().getProperty(Constants.SECURITY_PROTOCOL)))
        {
            env.put("java.naming.security.protocol", idp.getIDPProperties().getProperty(
                    Constants.SECURITY_PROTOCOL));
        }

        ctx = new InitialLdapContext(env, null);
    }

    /**
     * This method authenticates user against LDAP.
     */
    public boolean authenticate(final LoginCredentials loginCredentials) throws AuthenticationException
    {
        boolean isSuccessful = false;

        try
        {
            initialize();
            final SearchResult searchResult = searchUser(loginCredentials.getLoginName(), new String[0]);

            if (searchResult != null)
            {
                env.put("java.naming.security.principal", searchResult.getNameInNamespace());
                env.put("java.naming.security.credentials", loginCredentials.getPassword());
                new InitialLdapContext(env, null);

                isSuccessful = true;
            }

        }
        catch (final NamingException e)
        {
            throw new AuthenticationException(e);
        }
        return isSuccessful;
    }

    private SearchResult searchUser(final String userName, final String[] returnAttributes) throws NamingException
    {

        SearchResult searchResult = null;

        final SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnAttributes);
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = idp.getIDPProperties().getProperty(Constants.SEARCH_FILTER);
        final String string_to_replace = idp.getIDPProperties().getProperty(Constants.STRING_TO_REPLACE);
        if (searchFilter.indexOf(string_to_replace) != -1)
        {
            searchFilter = searchFilter.replace(string_to_replace, userName);
        }

        final String searchBase = idp.getIDPProperties().getProperty(Constants.SEARCH_BASE);
        final NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
        while (answer.hasMoreElements())
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
