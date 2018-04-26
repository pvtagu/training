/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package my.bookstore.core.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;

import java.util.ArrayList;
import java.util.List;

import my.bookstore.core.constants.BookstoreCoreConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 *
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = BookstoreCoreConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	public static final String IMPORT_VERIFICATION_SCRIPTS = "verificationScripts";

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/bookstorecore/import/common/essential-data.impex");
		importImpexFile(context, "/bookstorecore/import/common/countries.impex");
		importImpexFile(context, "/bookstorecore/import/common/delivery-modes.impex");

		importImpexFile(context, "/bookstorecore/import/common/themes.impex");
		importImpexFile(context, "/bookstorecore/import/common/user-groups.impex");


	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));

		return params;
	}

	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);

		final List<String> extensionNames = getExtensionNames();


		//Process.INIT means: applies to System Initialization operation,
		//Process.UPDATE means: applies to UpdateSystem operation with 'Update Running System' parameter = true
		//Process.ALL means: applies to all System Initialization / System Update operations
		//                   (regardless of value of 'Update Running System parameter')
		if (context.getProcess() == Process.INIT || getBooleanSystemSetupParameter(context, IMPORT_VERIFICATION_SCRIPTS))
		{
			importImpexFile(context, "/bookstorecore/import/common/verifyExercise/verifyExercises.impex");
		}

		if (importAccessRights && extensionNames.contains("cmscockpit"))
		{
			importImpexFile(context, "/bookstorecore/import/cockpits/cmscockpit/cmscockpit-users.impex");
			importImpexFile(context, "/bookstorecore/import/cockpits/cmscockpit/cmscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("btgcockpit"))
		{
			importImpexFile(context, "/bookstorecore/import/cockpits/cmscockpit/btgcockpit-users.impex");
			importImpexFile(context, "/bookstorecore/import/cockpits/cmscockpit/btgcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("productcockpit"))
		{
			importImpexFile(context, "/bookstorecore/import/cockpits/productcockpit/productcockpit-users.impex");
			importImpexFile(context, "/bookstorecore/import/cockpits/productcockpit/productcockpit-access-rights.impex");
			importImpexFile(context, "/bookstorecore/import/cockpits/productcockpit/productcockpit-constraints.impex");
		}

		if (importAccessRights && extensionNames.contains("cscockpit"))
		{
			importImpexFile(context, "/bookstorecore/import/cockpits/cscockpit/cscockpit-users.impex");
			importImpexFile(context, "/bookstorecore/import/cockpits/cscockpit/cscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("reportcockpit"))
		{
			importImpexFile(context, "/bookstorecore/import/cockpits/reportcockpit/reportcockpit-users.impex");
			importImpexFile(context, "/bookstorecore/import/cockpits/reportcockpit/reportcockpit-access-rights.impex");
		}

		if (extensionNames.contains("mcc"))
		{
			importImpexFile(context, "/bookstorecore/import/common/mcc-sites-links.impex");
		}

		importImpexFile(context, "/bookstorecore/import/common/projectdataSecurity.impex");
	}


	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}
