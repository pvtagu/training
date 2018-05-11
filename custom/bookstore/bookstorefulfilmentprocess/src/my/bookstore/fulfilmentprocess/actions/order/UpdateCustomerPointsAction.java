/**
 *
 */
package my.bookstore.fulfilmentprocess.actions.order;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import my.bookstore.fulfilmentprocess.events.CustomerUpdateEvent;


// TODO exercise 15.1: extend the class and then override its executeAction method!
public class UpdateCustomerPointsAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(UpdateCustomerPointsAction.class);

	private EventService eventService;

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		eventService.publishEvent(new CustomerUpdateEvent(process));
		LOG.info("Process: " + process.getCode() + " in step " + getClass());
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

}
