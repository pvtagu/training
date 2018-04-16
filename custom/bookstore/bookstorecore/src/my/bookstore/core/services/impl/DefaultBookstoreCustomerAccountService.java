/**

 *
 */
package my.bookstore.core.services.impl;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import my.bookstore.core.enums.RewardStatusLevel;
import my.bookstore.core.model.BookModel;
import my.bookstore.core.services.BookstoreCustomerAccountService;


/**
 * @author pvtagu
 *
 */
public class DefaultBookstoreCustomerAccountService extends DefaultCustomerAccountService
		implements BookstoreCustomerAccountService
{
	public DefaultGenericDao<CustomerModel> customerDao;

	@SuppressWarnings("boxing")
	@Override
	public void updateRewardStatusPoints(final CustomerModel customer, final OrderModel order)
	{
		int total = 0; //represents total number of point that Customer will get for this order
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			final BookModel book = (BookModel) entry.getProduct();
			total += book.getRewardPoints() * entry.getQuantity();
		}

		customer.setPoints(Integer.valueOf(total));
		getModelService().save(customer);

	}

	@Override
	public List<CustomerModel> getAllCustomersForLevel(final RewardStatusLevel level)
	{
		// TODO exercise 6.3 : implement the method
		//		final String queryString = "SELECT {c.PK}"
		//											+ "FROM {customer as c JOIN RewardStatusLevel as r "
		//											+ "on {c.RewardStatusLevel} = {r.PK}}"
		//											+ "WHERE {r.code} = ?code";
		//
		//		FlexibleSearchQuery fsq = (FlexibleSearchQuery) getFlexibleSearchService().search(queryString);
		//
		//		fsq.addQueryParameter("code", level.getCode());
		//
		//		return getFlexibleSearchService().<CustomerModel> search(fsq).getResult();

		final Map<String, Object> params = new HashMap<>();
		params.put(CustomerModel.REWARDSTATUSLEVEL, level);
		final List<CustomerModel> customersForLevel = customerDao.find(params);

		return customersForLevel;

	}

	/**
	 * @param customerDao
	 *           the customerDao to set
	 */
	@Required
	public void setCustomerDao(final DefaultGenericDao<CustomerModel> customerDao)
	{
		this.customerDao = customerDao;
	}

}
