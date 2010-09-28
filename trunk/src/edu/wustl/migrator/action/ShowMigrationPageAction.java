package edu.wustl.migrator.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class performs the required processing to render the migration page.
 *
 * @author niharika_sharma
 *
 */
public class ShowMigrationPageAction extends Action
{

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws MigratorException
    {
        return mapping.findForward(Constants.SUCCESS);
    }
}
