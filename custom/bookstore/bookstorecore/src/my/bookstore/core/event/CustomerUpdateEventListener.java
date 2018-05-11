package my.bookstore.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Required;

import my.bookstore.core.services.BookstoreCustomerAccountService;
import my.bookstore.fulfilmentprocess.events.CustomerUpdateEvent;


public class CustomerUpdateEventListener extends AbstractSiteEventListener<CustomerUpdateEvent>
{

	private UserService userService;

	private BookstoreCustomerAccountService bookstoreCustomerAccountService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final CustomerUpdateEvent event)
	{
		// TODO exercise 15.2: write the implementation as explained in the instructions.
		CustomerModel customer = event.getCustomer();
		OrderModel order = event.getProcess().getOrder();
		bookstoreCustomerAccountService.updateRewardStatusPoints(customer, order);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.
	 * servicelayer .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final CustomerUpdateEvent event)
	{
		final UserModel anon = userService.getAnonymousUser();
		if (anon.equals(event.getCustomer()))
		{
			return false;
		}
		return true;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @param bookstoreCustomerAccountService
	 *           the bookstoreCustomerService to set
	 */
	@Required
	public void setBookstoreCustomerAccountService(final BookstoreCustomerAccountService bookstoreCustomerAccountService)
	{
		this.bookstoreCustomerAccountService = bookstoreCustomerAccountService;
	}

}
