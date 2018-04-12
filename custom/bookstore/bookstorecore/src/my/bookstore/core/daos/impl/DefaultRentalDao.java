package my.bookstore.core.daos.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import my.bookstore.core.daos.RentalDao;
import my.bookstore.core.model.BookModel;
import my.bookstore.core.model.RentalModel;


public class DefaultRentalDao extends AbstractItemDao implements RentalDao
{
	@Resource
	CatalogVersionService catalogVersionService;

	@Override
	public List<RentalModel> getActiveRentalsForCustomer(final CustomerModel customer)
	{
		/*
		 * This could be done using GenericDao but for learning purposes we are using Flexible Search
		 *
		 * When figuring out when rentals start/end, we want to be generous: begin at the start of the first day, and stop
		 * at the end of the last day. For that, we need two date variables: dayStart and dayEnd.
		 */

		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final Date dayStart = cal.getTime(); // dayStart
		cal.add(Calendar.DATE, 1);
		final Date dayEnd = cal.getTime(); // dayEnd

		/*
		 * Now, your part of the implementation. Use the Flexible Search statement you created in exercise 5.2:
		 *
		 * SELECT ...
		 *
		 * Now implement it in Java:
		 */

		// ---------------------------------------------------------------------------
		// TODO exercise 5.3: Get the list of all the active rentals for this customer

		return null; // Delete this line before updating code below

		/*
		 * final String queryString = "SELECT ...";
		 * 
		 * 
		 * // 1. Compile a query from this string final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		 * 
		 * // 2. Add the query parameters // ...
		 * 
		 * // 3. Execute! return getFlexibleSearchService().<RentalModel> search(query).getResult();
		 */
	}

	@Override
	public List<BookModel> getMostRentedBooks(final int numberOfBooks)
	{

		/*
		 * Use the Flexible Search statement you created in exercise 5.4:
		 *
		 * SELECT ...
		 */

		// ----------------------------------------------
		// TODO exercise 5.5: Get the 5 most rented books

		return null; // Delete this line before updating code below

		/*
		 * 
		 * final String queryString = "SELECT ...";
		 * 
		 * // 1. Compile a query from this string final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		 * 
		 * // 2. Add the query parameter // ...
		 * 
		 * // 3. Execute final SearchResult<BookModel> books = getFlexibleSearchService().search(query); return
		 * books.getResult();
		 */
	}
}
