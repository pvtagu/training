/**
 *
 */
package my.bookstore.fulfilmentprocess.actions.order;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.task.RetryLaterException;

import my.bookstore.fulfilmentprocess.events.CustomerUpdateEvent;


// TODO exercise 15.1: extend the class and then override its executeAction method!
public class UpdateCustomerPointsAction 
{

	private EventService eventService;

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

}
