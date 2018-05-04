/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package my.bookstore.payment.setup;

import static my.bookstore.payment.constants.BookstorepaymentConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import my.bookstore.payment.constants.BookstorepaymentConstants;
import my.bookstore.payment.service.BookstorepaymentService;


@SystemSetup(extension = BookstorepaymentConstants.EXTENSIONNAME)
public class BookstorepaymentSystemSetup
{
	private final BookstorepaymentService bookstorepaymentService;

	public BookstorepaymentSystemSetup(final BookstorepaymentService bookstorepaymentService)
	{
		this.bookstorepaymentService = bookstorepaymentService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		bookstorepaymentService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return BookstorepaymentSystemSetup.class.getResourceAsStream("/bookstorepayment/sap-hybris-platform.png");
	}
}
