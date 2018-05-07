package my.bookstore.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import my.bookstore.core.services.BookstoreCustomerAccountService;
import my.bookstore.fulfilmentprocess.events.CustomerUpdateEvent;



public class CustomerUpdateEventListener extends AbstractSiteEventListener<CustomerUpdateEvent>
{

	private UserService userService;

	private BookstoreCustomerAccountService bookstoreCustomerService;

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
		
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
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
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @param bookstoreCustomerService
	 *           the bookstoreCustomerService to set
	 */
	public void setBookstoreCustomerService(final BookstoreCustomerAccountService bookstoreCustomerService)
	{
		this.bookstoreCustomerService = bookstoreCustomerService;
	}

}
