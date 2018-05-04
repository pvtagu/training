/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package my.bookstore.payment.commands.impl;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.payment.commands.SubscriptionAuthorizationCommand;
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Test;


/**
 * SubscriptionAuthorizationMockCommandTest
 *
 * The following class supports a Tesd-Driven-Development (TDD) approach for the SAP Hybris Commerce Developer training,
 * part 2.
 *
 * It tests the perform() method of the SubscriptionAuthorizationMockCommand class.
 *
 */
@IntegrationTest
public class SubscriptionAuthorizationMockCommandTest
{
	/**
	 * testPerform
	 *
	 * The authorization command we're testing rejects an order whose amount is over 3000.
	 *
	 * Create an instance of both authorization request and command, and pass the first as an argument to the second.
	 *
	 * Since the command rejects orders over 3000, perform boundary testing with the
	 *
	 * values 2999, 3000, and 3001. The first two should pass, while the third should fail.
	 */

	@Test
	public void testPerform()
	{
		// Create an instance of the SubscriptionAuthorizationMockCommand class
		final SubscriptionAuthorizationCommand command = new SubscriptionAuthorizationMockCommand();
		// Create parameters for request constructor
		final Currency currency = Currency.getInstance("USD");
		final BillingInfo shippingInfo = new BillingInfo();
		/*
		 * -------------------------------------------------------------------------------------- -------------- TODO
		 * exercise 14.2: Test that the perform() method of the SubscriptionAuthorizationMockCommand class accepts
		 * requests with value up to and including 3000, and rejects requests with values over 3000.
		 */
		final TransactionStatus ACCEPTED = TransactionStatus.ACCEPTED;
		final TransactionStatus REJECTED = TransactionStatus.REJECTED;
		final TransactionStatusDetails SUCCESSFUL = TransactionStatusDetails.SUCCESFULL;
		final TransactionStatusDetails REVIEW_NEEDED = TransactionStatusDetails.REVIEW_NEEDED;

		// 1. Create a SubscriptionAuthorizationRequest with value 2999
		BigDecimal totalAmount = new BigDecimal(2999);
		SubscriptionAuthorizationRequest request = new SubscriptionAuthorizationRequest("VISA", "1", currency, totalAmount,shippingInfo, "PaymentProvider1");

		// 1.1 Test it
		if (!command.perform(request).getTotalAmount().equals(totalAmount))
		{
			fail("totalAmount should be equal to 2999");
		}

		// 1.2 perform() should return a result with TransactionStatus.ACCEPTED
		// and TransactionStatusDetails.SUCCESSFULL
		if (!command.perform(request).getTransactionStatus().equals(ACCEPTED) && !command.perform(request).getTransactionStatusDetails().equals(SUCCESSFUL))
		{
			fail("TotalAmount is not over 3000 so that Transaction should be accepted");
		}

		// 2. Create a SubscriptionAuthorizationRequest with value 5000
		totalAmount = new BigDecimal(3000);
		request = new SubscriptionAuthorizationRequest("VISA", "1", currency, totalAmount, shippingInfo, "PaymentProvider1");
		// 2.1 Test it
		if (!command.perform(request).getTotalAmount().equals(totalAmount))
		{
			fail("totalAmount should be equal to 3000");
		}

		// 2.2 perform() should return a result with TransactionStatus.ACCEPTED
		// and TransactionStatusDetails.SUCCESSFULL
		if (!command.perform(request).getTransactionStatus().equals(ACCEPTED) && !command.perform(request).getTransactionStatusDetails().equals(SUCCESSFUL))
		{
			fail("TotalAmount is equal to 3000 so that Transaction should be accepted");
		}

		// 3. Create a SubscriptionAuthorizationRequest with value 3001
		totalAmount = new BigDecimal(3001);
		request = new SubscriptionAuthorizationRequest("VISA", "1", currency, totalAmount, shippingInfo, "PaymentProvider1");

		// 3.1 Test it
		if (!command.perform(request).getTotalAmount().equals(totalAmount))
		{
			fail("totalAmount should be equal to 3001");
		}

		// 3.2 perform() should return a result with TransactionStatus.REJECTED
		// and TransactionStatusDetails.REVIEW_NEEDED
		if (!command.perform(request).getTransactionStatus().equals(REJECTED) && !command.perform(request).getTransactionStatusDetails().equals(REVIEW_NEEDED))
		{
			fail("Transaction with totalAmount over 3000 should be REJECTED");
		}

	}

	public SubscriptionAuthorizationMockCommandTest()
	{
	}

}
