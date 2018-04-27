package my.bookstore.core.daos.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import my.bookstore.core.daos.RentalDao;
import my.bookstore.core.model.BookModel;
import my.bookstore.core.model.RentalModel;

public class DefaultRentalDao extends AbstractItemDao implements RentalDao {

	@Resource
	CatalogVersionService catalogVersionService;
	
	@Override
	public List<RentalModel> getActiveRentalsForCustomer(final CustomerModel customer) {
		/* 
		 * This could be done using GenericDao but for learning purposes we are using Flexible Search
		 *
		 * When figuring out when rentals start/end, we want to be generous: begin at the start
		 * of the first day, and stop at the end of the last day.
		 * For that, we need two date variables: dayStart and dayEnd. 
		*/

		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final Date dayStart = cal.getTime(); // dayStart
		cal.add(Calendar.DATE, 1);
		final Date dayEnd = cal.getTime();   // dayEnd

		/* 
		 * Now, your part of the implementation. 
		 * Use the Flexible Search statement you created in exercise 5.2:
		 *
		 * SELECT {rental.pk} FROM {Rental as rental} 
		 *                    WHERE {rental.startDate} <= ?tomorrow" 
		 *                    AND {rental.endDate} >= ?today 
		 *                    AND {rental.customer} = ?customer
		 *
		 * Now implement it in Java:
		 */		
		
		// TODO exercise 5.3

		/* 
		 * This is what your implementation might look like:
		 * 
		 * final String queryString = "SELECT {rental.pk} FROM {Rental as rental} " + 
		 *                            "WHERE {rental.startDate} <= ?tomorrow " +
		 *                            "AND {rental.endDate} >= ?today " +
		 *                            "AND {rental.customer} = ?customer";
		 *
       *
		 * Remember, though, that we're encouraging you to write your Flexible Search queries 
		 * using model class constants, to improve code maintenance. So we'll write it like this:
		 */ 
		
		final String queryString = "SELECT {rental.pk}" + " FROM {" + RentalModel._TYPECODE + " as rental}"
				+ " WHERE {rental." + RentalModel.STARTDATE + "} <= ?tomorrow" + " and {rental." + RentalModel.ENDDATE
				+ "} >= ?today" + " and {rental." + RentalModel.CUSTOMER + "} = ?customer";

		// 1. Compile a query from this string...
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		
		// 2. Add the query parameters...
		query.addQueryParameter("tomorrow", dayEnd);
		query.addQueryParameter("today", dayStart);
		query.addQueryParameter("customer", customer);
		
		// 3. Execute
		return getFlexibleSearchService().<RentalModel> search(query).getResult();
	}

	@Override
	public List<BookModel> getMostRentedBooks(final int numberOfBooks) {
		
		/* Use the Flexible Search statement you created in exercise 5.4:
		 * 
		 * SELECT pk FROM ({{ SELECT COUNT(*) AS num, {Book.pk} as pk
		 *                    FROM {Rental JOIN BOOK ON {Rental.product} = {Book.pk}}
		 *                    GROUP BY {Rental.product}, {Book.pk}
		 *                    ORDER BY num DESC LIMIT ?limit }})
		 */
		
		// TODO exercise 5.5
		/*
		 * This is what your implementation might look like:
		 * 
		 * final String queryString = "SELECT pk FROM ({{SELECT COUNT(*) as num, {Book.pk} as pk " +
		 *                                              "FROM {Rental JOIN Book ON {Rental.product} = {Book.pk}} " +
		 *                                              "GROUP BY {Rental.product}, {Book.pk} " +
		 *                                              "ORDER BY num DESC LIMIT ?limit}})";
		 *
       *
		 * Remember, though, that we're encouraging you to write your Flexible Search queries 
		 * using model class constants, to improve code maintenance. So we'll write it like this:
		 */ 

		final String queryString = "SELECT pk" + " FROM ({{SELECT COUNT(*) AS num, {Book." + BookModel.PK + "} AS pk"
				+ " FROM {" + RentalModel._TYPECODE + " JOIN " + BookModel._TYPECODE + " ON {Rental."
				+ RentalModel.PRODUCT + "}={Book." + BookModel.PK + "}}" + " GROUP BY {Rental." + RentalModel.PRODUCT
				+ "}, {Book." + BookModel.PK + "}" + " ORDER BY num DESC LIMIT ?limit}})";

		// 1. Compile a query from this string
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		
		// 2. Add the query parameter
		query.addQueryParameter("limit", numberOfBooks);
		
		// 3. Execute
		final SearchResult<BookModel> books = getFlexibleSearchService().search(query);
		return books.getResult();
	}

}
