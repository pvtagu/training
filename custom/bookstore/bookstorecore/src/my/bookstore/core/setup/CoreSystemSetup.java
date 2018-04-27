/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
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
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_VERIFICATION_SCRIPTS, "Import TrainingLabTools Verification Scripts",
				false));

		return params;
	}

	/**
	 * This method will be called during the system initialization and update.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);

		final List<String> extensionNames = getExtensionNames();


		//Process.INIT means System Initialization operation,
		//Process.UPDATE means 'Update Running System' = true for System Update operation
		//Process.ALL means 'Update Running System' = false for System Update operation
		if (context.getProcess() == Process.INIT || getBooleanSystemSetupParameter(context, IMPORT_VERIFICATION_SCRIPTS))
		{
			importImpexFile(context, "/bookstorecore/import/common/verifyExercise/verifyExercises.impex");
		}
		

		processCockpit(context, importAccessRights, extensionNames, "cmscockpit",
				"/bookstorecore/import/cockpits/cmscockpit/cmscockpit-users.impex",
				"/bookstorecore/import/cockpits/cmscockpit/cmscockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames, "btgcockpit",
				"/bookstorecore/import/cockpits/cmscockpit/btgcockpit-users.impex",
				"/bookstorecore/import/cockpits/cmscockpit/btgcockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames, "productcockpit",
				"/bookstorecore/import/cockpits/productcockpit/productcockpit-users.impex",
				"/bookstorecore/import/cockpits/productcockpit/productcockpit-access-rights.impex",
				"/bookstorecore/import/cockpits/productcockpit/productcockpit-constraints.impex");

		processCockpit(context, importAccessRights, extensionNames, "cscockpit",
				"/bookstorecore/import/cockpits/cscockpit/cscockpit-users.impex",
				"/bookstorecore/import/cockpits/cscockpit/cscockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames, "reportcockpit",
				"/bookstorecore/import/cockpits/reportcockpit/reportcockpit-users.impex",
				"/bookstorecore/import/cockpits/reportcockpit/reportcockpit-access-rights.impex");

		if (extensionNames.contains("mcc"))
		{
			importImpexFile(context, "/bookstorecore/import/common/mcc-sites-links.impex");
		}

	}

	protected void processCockpit(final SystemSetupContext context, final boolean importAccessRights,
			final List<String> extensionNames, final String cockpit, final String... files)
	{
		if (importAccessRights && extensionNames.contains(cockpit))
		{
			for (final String file : files)
			{
				importImpexFile(context, file);
			}
		}
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
