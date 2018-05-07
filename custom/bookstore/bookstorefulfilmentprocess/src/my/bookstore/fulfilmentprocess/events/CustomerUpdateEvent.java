/**
 *
 */
package my.bookstore.fulfilmentprocess.events;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.events.OrderProcessingEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

/**
 * This event suggests that an attribute of a customer item needs updating.
 * @author MR.K
 *
 */
public class CustomerUpdateEvent extends OrderProcessingEvent
{

	private final CustomerModel customer;
	private final OrderModel order;

	public CustomerUpdateEvent(final OrderProcessModel process)
	{
		super(process);

		this.order = process.getOrder();
		this.customer = (CustomerModel) this.order.getUser();

	}

	public CustomerModel getCustomer()
	{
		return this.customer;
	}

	public OrderModel getOrder()
	{
		return this.order;
	}

}
