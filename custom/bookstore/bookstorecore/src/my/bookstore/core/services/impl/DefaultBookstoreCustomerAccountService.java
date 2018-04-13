/**
 *
 */
package my.bookstore.core.services.impl;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

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

	@Override
	public void updateRewardStatusPoints(final CustomerModel customer, final OrderModel order)
	{
		int total = 0; //represents total number of point that Customer will get for this order
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			final BookModel book = (BookModel) entry.getProduct();
			total += book.getRewardPoints() * entry.getQuantity();

		}

		// TODO exercise 6.2 : update customer points

	}

	@Override
	public List<CustomerModel> getAllCustomersForLevel(final RewardStatusLevel level)
	{
		// TODO exercise 6.3 : implement the method
		return null;
	}

}
