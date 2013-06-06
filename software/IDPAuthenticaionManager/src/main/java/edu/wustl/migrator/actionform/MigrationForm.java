/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.util.global.Validator;

/**
 * This class contains migration information provided by the user on the
 * migration page.
 *
 * @author niharika_sharma
 *
 */
public class MigrationForm extends ActionForm
{

    /** The migrated login name. */
    private String migratedLoginName;

    /** The migrated password. */
    private String migratedPassword;

    /** The target idp. */
    private String targetIdp;

    /**
     * Gets the migrated password.
     *
     * @return the migrated password
     */
    public String getMigratedPassword()
    {
        return migratedPassword;
    }

    /**
     * Sets the migrated password.
     *
     * @param migratedPassword
     *            the new migrated password
     */
    public void setMigratedPassword(final String migratedPassword)
    {
        this.migratedPassword = migratedPassword;
    }

    /**
     * Gets the migrated login name.
     *
     * @return the migrated login name
     */
    public String getMigratedLoginName()
    {
        return migratedLoginName;
    }

    /**
     * Sets the migrated login name.
     *
     * @param migratedLoginName
     *            the new migrated login name
     */
    public void setMigratedLoginName(final String migratedLoginName)
    {
        this.migratedLoginName = migratedLoginName;
    }

    /**
     * Gets the target idp.
     *
     * @return the target idp
     */
    public String getTargetIdp()
    {
        return targetIdp;
    }

    /**
     * Sets the target idp.
     *
     * @param targetIdp
     *            the new target idp
     */
    public void setTargetIdp(final String targetIdp)
    {
        this.targetIdp = targetIdp;
    }

    @Override
    public ActionErrors validate(final ActionMapping mapping, final HttpServletRequest request)
    {
        final ActionErrors errors = new ActionErrors();

        final String doNotAskAgain = request
                .getParameter(edu.wustl.wustlkey.util.global.Constants.DO_NOT_ASK_AGAIN);
        if (!edu.wustl.wustlkey.util.global.Constants.TRUE.equals(doNotAskAgain))
        {
            if (Validator.isEmpty(migratedLoginName))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Login Name"));
            }
            if (Validator.isEmpty(migratedPassword))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Password"));
            }

            if (Validator.isEmpty(targetIdp))
            {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required", "Target Domain"));
            }
        }
        return errors;
    }
}
