package my.bookstore.core.daos;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.List;

import my.bookstore.core.model.BookModel;
import my.bookstore.core.model.RentalModel;


public interface RentalDao extends Dao
{
	/**
	 * Searches for the rentals that belong to the <code>customer</code> and are currently active.
	 *
	 * @param customer
	 *           whose rentals are looked at
	 * @return a list of currently active rentals that the customer has
	 */
	List<RentalModel> getActiveRentalsForCustomer(CustomerModel customer);

	/**
	 * Finds the books which have rented the most number of times.
	 * 
	 * @param numberOfBooks
	 *           number of books that will be returned.
	 * @return a list of books with the size <code>numberOfBooks</code>
	 */
	List<BookModel> getMostRentedBooks(int numberOfBooks);

}
