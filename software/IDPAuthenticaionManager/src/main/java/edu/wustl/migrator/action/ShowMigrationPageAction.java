/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.ConfiguredIDPDetails;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migration.rules.RuleInterface;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class performs the required processing to render the migration page.
 *
 * @author niharika_sharma
 *
 */
public class ShowMigrationPageAction extends AbstractMigrationAction
{
    /** The Constant LOGGER. */
    private static final org.apache.log4j.Logger LOGGER = LoggerConfig
            .getConfiguredLogger(NeverMigrateUserAction.class);

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws MigratorException
    {
        try
        {
            final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
            final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

            final List<NameValueBean> migrationIdps = new ArrayList<NameValueBean>();

            for (final Properties properties : migrationProperties)
            {
                final String ruleClassName = (String) properties
                        .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);
                final String targetIdpName = (String) properties
                        .get(edu.wustl.migrator.util.Constants.TARGET_IDP_TAG_NAME);

                final RuleInterface rule = (RuleInterface) edu.wustl.common.util.Utility.getObject(ruleClassName);
                final LoginCredentials baseIdpCredentials = new LoginCredentials();
                baseIdpCredentials.setLoginName(getSessionData(request).getUserName());
                if (rule.checkMigrationRules(baseIdpCredentials))
                {
                    final NameValueBean idpDetails = new NameValueBean();
                    idpDetails.setName(ConfiguredIDPDetails.getInstance().getIDP(targetIdpName).getIDPProperties()
                            .getProperty(edu.wustl.migrator.util.Constants.IDP_DISPLAY_NAME_TAG_NAME));
                    idpDetails.setValue(targetIdpName);
                    migrationIdps.add(idpDetails);
//                    break;
                }
            }

            if (request.getAttribute("idpSelection") == null)
            {
                request.setAttribute("idpSelection", "no");
            }
            request.setAttribute("migrationIdps", migrationIdps);
        }
        catch (final AuthenticationException e)
        {
            LOGGER.info("Exception: " + e.getMessage(), e);
            handleError(request, "errors.incorrectLoginIDPassword");
        }
        catch (final AuthFileParseException e)
        {
            LOGGER.info("Exception: " + e.getMessage(), e);
            handleError(request, "errors.incorrectLoginIDPassword");
        }

        return mapping.findForward(Constants.SUCCESS);
    }
}
