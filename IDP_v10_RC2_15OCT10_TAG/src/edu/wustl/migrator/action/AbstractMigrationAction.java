package edu.wustl.migrator.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * This class contains common methods that will be required by the migration
 * related action classes.
 *
 * @author niharika_sharma
 */
public abstract class AbstractMigrationAction extends Action
{

    /**
     * Handle error.
     *
     * @param request
     *            the request
     * @param errorKey
     *            the error key
     */
    protected void handleError(final HttpServletRequest request, final String errorKey)
    {
        final ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey));
        if (!errors.isEmpty())
        {
            saveErrors(request, errors);
        }
    }

    /**
     * This method is for showing Custom message.
     *
     * @param request
     *            HttpServletRequest
     */
    protected void handleCustomMessage(final HttpServletRequest request)
    {
        final ActionMessages msg = new ActionMessages();
        final String errorMsg = ApplicationProperties.getValue("migration.msg");
        final ActionMessage msgs = new ActionMessage("msg.item", errorMsg);
        msg.add(ActionErrors.GLOBAL_ERROR, msgs);
        if (!msg.isEmpty())
        {
            saveMessages(request, msg);
        }
    }

    /**
     * Gets the session data.
     *
     * @param request
     *            the request
     *
     * @return the session data
     */
    protected SessionDataBean getSessionData(final HttpServletRequest request)
    {

        return (SessionDataBean) request.getSession().getAttribute(
                edu.wustl.common.util.global.Constants.SESSION_DATA);
    }
}
