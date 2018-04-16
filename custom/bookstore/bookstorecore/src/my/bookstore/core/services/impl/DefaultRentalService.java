/**
 *
 */
package my.bookstore.core.services.impl;

import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import my.bookstore.core.daos.RentalDao;
import my.bookstore.core.model.RentalModel;
import my.bookstore.core.services.RentalService;


/**
 * @author pvtagu
 *
 */
public class DefaultRentalService implements RentalService
{
	private RentalDao rentalDao;

	@Override
	public List<RentalModel> getActiveRentalsForCustomer(final CustomerModel customer)
	{
		// TODO exercise 6.4: add implementation
		return rentalDao.getActiveRentalsForCustomer(customer);
	}

	/**
	 * @param rentalDao
	 *           the rentalDao to set
	 */
	@Required
	public void setRentalDao(final RentalDao rentalDao)
	{
		this.rentalDao = rentalDao;
	}

}
