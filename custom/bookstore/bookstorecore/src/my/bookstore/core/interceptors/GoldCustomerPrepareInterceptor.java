/**
 * 
 */
package my.bookstore.core.interceptors;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;

import java.util.HashSet;
import java.util.Set;

import my.bookstore.core.enums.RewardStatusLevel;

import org.springframework.beans.factory.annotation.Autowired;

public class GoldCustomerPrepareInterceptor //TODO exercise 12.2: implement interface
{

	private static final String GOLD_CUSTOMER = "goldcustomergroup";
	@Autowired
	private UserGroupDao userGroupDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.PrepareInterceptor#onPrepare(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	
	
	/* TODO exercise 12.2: comment out @Override annotation and  have a look at implementation of onPrepare method*/
	//@Override
	public void onPrepare(final CustomerModel model, final InterceptorContext ctx) throws InterceptorException
	{
		final UserGroupModel goldCustomerGroup = userGroupDao.findUserGroupByUid(GOLD_CUSTOMER);
		if (goldCustomerGroup == null) //i.e., hasn't been defined yet
		{
			return; //do nothing at this time
		}
		
		if (RewardStatusLevel.GOLD.equals(model.getRewardStatusLevel()))
		{

			if (!model.getGroups().contains(goldCustomerGroup))
			{
				final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(model.getGroups());
				newGroups.add(goldCustomerGroup);
				model.setGroups(newGroups);
			}

		}
		else
		{
			if (model.getGroups().contains(goldCustomerGroup))
			{
				final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(model.getGroups());
				newGroups.remove(goldCustomerGroup);
				model.setGroups(newGroups);
			}
		}

	}

}
