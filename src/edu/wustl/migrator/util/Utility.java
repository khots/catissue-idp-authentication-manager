package edu.wustl.migrator.util;

import java.util.List;

import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

public class Utility
{
    public static List executeQueryUsingDataSource(final String queryString, final Boolean isDML,
            final String applicationName) throws ApplicationException
    {
        JDBCDAO jdbcDAO = null;
        List resultList = null;
        try
        {
            jdbcDAO = DAOConfigFactory.getInstance().getDAOFactory(applicationName).getJDBCDAO();
            jdbcDAO.openSession(null);
            if (!isDML)
            {
                resultList = jdbcDAO.executeQuery(queryString);
                if (resultList.isEmpty())
                {
                    resultList = null;
                }
            }
            else
            {
                jdbcDAO.executeUpdate(queryString);
            }
        }
        catch (final DAOException ex)
        {
            //logger.debug(ex.getMessage(), ex);
            jdbcDAO.rollback();
            throw new ApplicationException(ex.getErrorKey(), ex, ex.getMessage());
        }
        finally
        {
            jdbcDAO.closeSession();
        }
        return resultList;
    }
}
