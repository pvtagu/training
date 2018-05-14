package my.bookstore.core.services.impl;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import my.bookstore.core.model.BookModel;
import my.bookstore.core.services.BookstoreEmailService;


public class DefaultBookstoreEmailService implements BookstoreEmailService
{

	@Resource
	private ModelService modelService;

	@Resource
	private ConfigurationService configurationService;


	private EmailService emailService;
	private boolean sendEmails;
	private DefaultGenericDao<CustomerModel> customerDao;

	@Override
	public void sendMostRentedBooks(final List<BookModel> books)
	{
		setSendEmails(true);
		System.out.println("Top rented books: ");
		for (final BookModel book : books)
		{
			System.out.println(book.getName());
		}
		if (isSendEmails())
		{
			sendEmails(books);
		}
	}

	private void sendEmails(final List<BookModel> books)
	{

		final String fromAddress = configurationService.getConfiguration().getString("mail.from");
		final String replyToAddress = configurationService.getConfiguration().getString("mail.replyto");
		final EmailAddressModel fromAddressModel = emailService.getOrCreateEmailAddressForEmail(fromAddress, fromAddress);

		final EmailMessageModel emailMessage = new EmailMessageModel();
		final StringBuilder builder = new StringBuilder("Top rented books: ");
		for (final BookModel book : books)
		{
			builder.append(book.getName() + "\n");
		}
		emailMessage.setBody(builder.toString());
		emailMessage.setToAddresses(getCustomerEmailAddresses(replyToAddress));

		emailMessage.setFromAddress(fromAddressModel);
		emailMessage.setSubject("Bookstore top rented books");
		emailMessage.setReplyToAddress(replyToAddress);
		emailMessage.setSent(false);

		modelService.save(emailMessage);

		emailService.send(emailMessage);
	}

	/**
	 * Gets the customer email addresses and adds the replyToAddress for sending email to participant also
	 *
	 * @param replyToAddress
	 * @return list with email addresses
	 */
	private List<EmailAddressModel> getCustomerEmailAddresses(final String replyToAddress)
	{
		final List<EmailAddressModel> addresses = new ArrayList<EmailAddressModel>();
		if (replyToAddress != null && replyToAddress.contains("@"))
		{
			addresses.add(emailService.getOrCreateEmailAddressForEmail(replyToAddress, replyToAddress));
		}
		final List<CustomerModel> customers = customerDao.find();
		for (final CustomerModel customer : customers)
		{
			//avoid sending emails to not valid email addresses
			if (customer.getUid().contains("@"))
			{
				addresses.add(emailService.getOrCreateEmailAddressForEmail(customer.getUid(), customer.getUid()));
			}
		}
		return addresses;
	}

	/**
	 * @param emailService
	 *           the emailService to set
	 */
	public void setEmailService(final EmailService emailService)
	{
		this.emailService = emailService;
	}

	/**
	 * @return the sendEmails
	 */
	public boolean isSendEmails()
	{
		return sendEmails;
	}

	/**
	 * @param sendEmails
	 *           the sendEmails to set
	 */
	public void setSendEmails(final boolean sendEmails)
	{
		this.sendEmails = sendEmails;
	}

	/**
	 * @param customerDao
	 *           the customerDao to set
	 */
	public void setCustomerDao(final DefaultGenericDao<CustomerModel> customerDao)
	{
		this.customerDao = customerDao;
	}

}
