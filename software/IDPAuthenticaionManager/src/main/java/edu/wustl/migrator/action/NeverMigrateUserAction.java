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

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.IDPInterface;
import edu.wustl.migration.rules.RuleInterface;
import edu.wustl.migrator.MigratorInterface;
import edu.wustl.migrator.actionform.MigrationForm;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.migrator.util.Utility;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class performs the processing required when the user clicks on do not
 * ask again check box.
 *
 * @author niharika_sharma
 *
 */
public class NeverMigrateUserAction extends AbstractMigrationAction
{

    /** The Constant LOGGER. */
    private static final org.apache.log4j.Logger LOGGER = LoggerConfig
            .getConfiguredLogger(NeverMigrateUserAction.class);

    /*
     * (non-Javadoc)
     *
     * @seeorg.apache.struts.action.Action#execute(org.apache.struts.action.
     * ActionMapping, org.apache.struts.action.ActionForm,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws MigratorException
    {
        String forwardTo = Constants.FAILURE;

        final MigrationForm migrationForm = (MigrationForm) form;

        final String doNotAskAgain = request.getParameter(Constants.DO_NOT_ASK_AGAIN);

        try
        {
            if (Constants.TRUE.equals(doNotAskAgain))
            {
                final IDPInterface sourceIdp = AuthManagerFactory.getInstance().getAuthManagerInstance().getIDP();
                final List<Properties> migrationProperties = sourceIdp.getMigrationProperties();

                for (final Properties properties : migrationProperties)
                {
                    final String migratorClassName = (String) properties
                            .get(edu.wustl.migrator.util.Constants.MIGRATOR_CLASS_TAG_NAME);
                    final String ruleClassName = (String) properties
                            .get(edu.wustl.migrator.util.Constants.RULE_CLASS_TAG_NAME);
                    final RuleInterface rule = (RuleInterface) edu.wustl.common.util.Utility
                            .getObject(ruleClassName);

                    final LoginCredentials baseIdpCredentials = new LoginCredentials();
                    baseIdpCredentials.setLoginName(getSessionData(request).getUserName());
                    if (rule.checkMigrationRules(baseIdpCredentials))
                    {
                        final IDPAuthManager targetAuthManager = AuthManagerFactory.getInstance()
                                .getAuthManagerInstance(migrationForm.getTargetIdp());
                        final MigratorInterface migrator = Utility.getMigratorInstance(migratorClassName,
                                targetAuthManager.getIDP());
                        migrator.neverMigrate(getSessionData(request).getUserName());
                        // handleCustomMessage(request);
                        forwardTo = Constants.LOGIN;
                        break;
                    }
                }
            }
            else
            {
                forwardTo = Constants.LOGIN;
            }
        }
        catch (final Exception e)
        {
            LOGGER.info("Exception: " + e.getMessage(), e);
            handleError(request, "errors.wustlkeyorpassword");
        }
        return mapping.findForward(forwardTo);
    }
}
