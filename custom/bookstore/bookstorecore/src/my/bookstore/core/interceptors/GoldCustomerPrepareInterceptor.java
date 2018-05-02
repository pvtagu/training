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

import org.springframework.beans.factory.annotation.Autowired;

import my.bookstore.core.enums.RewardStatusLevel;


public class GoldCustomerPrepareInterceptor implements PrepareInterceptor<CustomerModel> //TODO exercise 12.2: implement interface
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

	@Override
	public void onPrepare(final CustomerModel customer, final InterceptorContext ctx) throws InterceptorException
	{
		final UserGroupModel goldCustomerGroup = userGroupDao.findUserGroupByUid(GOLD_CUSTOMER);
		if (goldCustomerGroup == null) //i.e., hasn't been defined yet
		{
			return; //do nothing at this time
		}

		if (RewardStatusLevel.GOLD.equals(customer.getRewardStatusLevel()))
		{

			if (!customer.getGroups().contains(goldCustomerGroup))
			{
				final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(customer.getGroups());
				newGroups.add(goldCustomerGroup);
				customer.setGroups(newGroups);
			}

		}
		else
		{
			if (customer.getGroups().contains(goldCustomerGroup))
			{
				final Set<PrincipalGroupModel> newGroups = new HashSet<PrincipalGroupModel>(customer.getGroups());
				newGroups.remove(goldCustomerGroup);
				customer.setGroups(newGroups);
			}
		}

	}

}
