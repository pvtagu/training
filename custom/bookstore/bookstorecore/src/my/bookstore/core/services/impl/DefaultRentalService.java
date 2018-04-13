/**
 *
 */
package my.bookstore.core.services.impl;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import my.bookstore.core.model.RentalModel;
import my.bookstore.core.services.RentalService;


/**
 * @author pvtagu
 *
 */
public class DefaultRentalService implements RentalService
{
	@Override
	public List<RentalModel> getActiveRentalsForCustomer(final CustomerModel customer)
	{
		// TODO exercise 6.4: add implementation
		return null;
	}

}
