package edu.wustl.migrator.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class performs the required processing to render the migration page.
 *
 * @author niharika_sharma
 *
 */
public class ShowMigrationPageAction extends Action
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
            final HttpServletRequest request, final HttpServletResponse response) throws Exception
    {
        System.out.println("In ShowMigrationPageAction");
        return mapping.findForward(Constants.SUCCESS);
    }
}
