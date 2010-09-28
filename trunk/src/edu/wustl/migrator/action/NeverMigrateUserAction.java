package edu.wustl.migrator.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.abstractidp.WustlKeyIDP;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.migrator.IAbstractMigrator;
import edu.wustl.migrator.WUSTLKeyMigrator;
import edu.wustl.migrator.exception.MigratorException;
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
        String forwardTo = "failure";

        final String doNotAskAgain = request.getParameter(Constants.DO_NOT_ASK_AGAIN);

        final IAbstractMigrator migrator = new WUSTLKeyMigrator(new WustlKeyIDP());
        try
        {
            if (Constants.TRUE.equals(doNotAskAgain))
            {
                migrator.neverMigrate(getSessionData(request).getUserName());
            }
            forwardTo = "login";
        }
        catch (final Exception e)
        {
            LOGGER.info("Exception: " + e.getMessage(), e);
            handleError(request, "errors.wustlkeyorpassword");
        }
        return mapping.findForward(forwardTo);
    }
}
