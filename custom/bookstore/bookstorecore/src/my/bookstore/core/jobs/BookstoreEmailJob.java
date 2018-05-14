package my.bookstore.core.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import my.bookstore.core.daos.RentalDao;
import my.bookstore.core.model.BookModel;
import my.bookstore.core.services.BookstoreEmailService;
import org.springframework.beans.factory.annotation.Required;


public class BookstoreEmailJob extends AbstractJobPerformable<CronJobModel>// TODO exercise 16.1 : extend appropriate class
{

	private BookstoreEmailService bookstoreEmailService;
	private RentalDao rentalDao;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		// TODO exercise 16.1 : add implementation;
		List<BookModel> mostRentedBooks = rentalDao.getMostRentedBooks(Integer.valueOf(5));
		bookstoreEmailService.sendMostRentedBooks(mostRentedBooks);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Required
	public void setBookstoreEmailService(BookstoreEmailService bookstoreEmailService) {
		this.bookstoreEmailService = bookstoreEmailService;
	}

	@Required
	public void setRentalDao(RentalDao rentalDao) {
		this.rentalDao = rentalDao;
	}
}
