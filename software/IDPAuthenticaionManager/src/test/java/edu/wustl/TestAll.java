/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.wustl.authmanager.TestLDAPAuthManager;
import edu.wustl.authmanager.factory.TestAuthManagerFactory;
import edu.wustl.idp.factory.TestIDPFactory;
import edu.wustl.migrator.TestWUSTLKeyMigrator;
import edu.wustl.processor.TestLoginProcessor;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestAll extends TestCase
{

    /**
     * The main method.
     *
     * @param args arg
     */
    public static void main(final String[] args)
    {
        junit.textui.TestRunner.run(TestAll.class);
    }

    /**
     * Suite.
     *
     * @return test
     */
    public static Test suite()
    {
        final TestSuite suite = new TestSuite("Test suite for IDP Authentictation Manager business logic");
        suite.addTestSuite(TestWUSTLKeyMigrator.class);
        suite.addTestSuite(TestAuthManagerFactory.class);
        suite.addTestSuite(TestLDAPAuthManager.class);
        suite.addTestSuite(TestIDPFactory.class);
        suite.addTestSuite(TestLoginProcessor.class);

        return suite;
    }
}
