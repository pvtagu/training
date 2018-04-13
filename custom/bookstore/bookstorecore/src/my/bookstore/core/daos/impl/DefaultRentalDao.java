package my.bookstore.core.daos.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

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

		// TODO exercise 5.3: Get the list of all the active rentals for this customer
		
		final String queryString = "SELECT {r.PK}"
											+ "FROM {rental as r JOIN customer as c"
											+ "on {r.customer} = {c.PK} JOIN book as b"
											+ "on {r.book} = {b.PK}}"
											+ "WHERE {c.uid} = ?customer AND"
											+ "{r.startDate} < ?dayStart AND"
											+ "{r.endDate} > ?dayEnd";
		
		FlexibleSearchQuery fsq = new FlexibleSearchQuery(queryString);
		
		fsq.addQueryParameter("customer", customer);
		fsq.addQueryParameter("dayStart", dayStart);
		fsq.addQueryParameter("dayEnd", dayEnd);
		
		return getFlexibleSearchService().<RentalModel> search(fsq).getResult();
	}

	@Override
	public List<BookModel> getMostRentedBooks(final int numberOfBooks)
	{

		// TODO exercise 5.5: Get the 5 most rented books
		
		final String queryString = "SELECT TOP ?numberOfBooks {r.book}"
											+ "FROM {rental as r}"
											+ "GROUP BY {r.book}"
											+ "ORDER BY Count(*) DESC";
		
		FlexibleSearchQuery fsq = new FlexibleSearchQuery(queryString);
		
		fsq.addQueryParameter("numberOfBooks", Integer.valueOf(numberOfBooks));
		
		return getFlexibleSearchService().<BookModel> search(fsq).getResult();
	}
}
