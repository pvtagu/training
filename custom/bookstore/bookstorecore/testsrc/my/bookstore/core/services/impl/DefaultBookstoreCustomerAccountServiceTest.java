/**
 *
 */
package my.bookstore.core.services.impl;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Test;

import my.bookstore.core.enums.RewardStatusLevel;
import my.bookstore.core.model.BookModel;
import my.bookstore.core.services.BookstoreCustomerAccountService;


/**
 * DefaultBookstoreCustomerAccountServiceTest
 *
 * The following class supports a Tesd-Driven-Development (TDD) approach for the SAP Hybris Commerce Developer training,
 * part 1.
 *
 * It tests the UpdateRewardStatusPoints() and getAllCustomersForLevel() methods of the
 * DefaultBookstoreCustomerAccountService class.
 *
 */
@IntegrationTest
public class DefaultBookstoreCustomerAccountServiceTest extends ServicelayerTransactionalTest
{

	private static final String PRODUCT_CATALOG_NAME = "bookstoreProductCatalog";
	private static final String STAGED_VERSION_NAME = "Staged";

	@Resource
	private ModelService modelService;

	@Resource
	private CatalogService catalogService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	BookstoreCustomerAccountService bookstoreCustomerAccountService;

	@Resource
	EnumerationService enumerationService;

	private CustomerModel customer;
	private OrderModel order;
	private final List<String> goldCustomerIds = new ArrayList<String>();

	private RewardStatusLevel blue;
	private RewardStatusLevel silver;
	private RewardStatusLevel gold;



	/**
	 * testUpdateRewardStatusPoints
	 *
	 * The initializeOrder() method creates the following objects:
	 *
	 * Customer - points = 0
	 *
	 * Order - containing the following 2 order entries: entry 1: quantity 1, book point value 40 entry 2: quantity 2,
	 * book point value 15
	 *
	 */

	@SuppressWarnings("boxing")
	@Test
	public void testUpdateRewardStatusPoints()
	{
		// Setup: we need a customer and an order (including order entries, books, units and a currency).
		initializeOrder();

		bookstoreCustomerAccountService.updateRewardStatusPoints(customer, order);

		if (!customer.getPoints().equals(Integer.valueOf(70)))
		{
			fail("Customer was not awarded the correct number of points");
		}

	}


	/**
	 * testGetAllCustomersForLevel
	 *
	 * The initializeCustomers() method creates five users: 1 with BLUE Reward Status Level, 1 with SILVER, and 3 with
	 * GOLD
	 *
	 * The Ids of the GOLD users are saved in: List<String> goldCustomerIds
	 *
	 */

	@Test
	public void testGetAllCustomersForLevel()
	{
		// Setup: we need a few customers with various reward levels.
		initializeCustomers();

		final List<CustomerModel> goldCustomersModel = bookstoreCustomerAccountService.getAllCustomersForLevel(gold);

		for (final CustomerModel customer : goldCustomersModel)
		{
			if (!goldCustomerIds.contains(customer.getUid()))
			{
				fail("The getAllCustomersForLevel did not correctly return all the gold customers.");
			}
		}

	}




	/**
	 * Constructor
	 *
	 *
	 */
	public DefaultBookstoreCustomerAccountServiceTest()
	{
		super();
	}




	/**
	 * initializeOrder
	 *
	 * Create the objects necessary to test the UpdateRewardStatusPoints() method.
	 *
	 * Customer Order - Currency - entries OrderEntry1 Book (rewardPoints=40) quantity=1 unit=pieces OrderEntry2 Book
	 * (rewardPoints=15) quantity=2 unit=pieces
	 *
	 */
	private void initializeOrder()
	{
		// Find the bookstoreProductCatalog catalog to insert books into
		CatalogModel bookstoreProductCatalog;
		try
		{
			bookstoreProductCatalog = catalogService.getCatalogForId(PRODUCT_CATALOG_NAME);
		}
		catch (final UnknownIdentifierException e)
		{
			//Only create if junit tenant doesn't already have this Catalog item created
			bookstoreProductCatalog = modelService.create(CatalogModel.class);
			bookstoreProductCatalog.setId(PRODUCT_CATALOG_NAME);
			modelService.save(bookstoreProductCatalog);
		}

		// Find the bookstoreProductCatalog:Staged catalog version to insert books into
		CatalogVersionModel bookstoreProductCatalogStaged;
		try
		{
			bookstoreProductCatalogStaged = catalogVersionService.getCatalogVersion(PRODUCT_CATALOG_NAME, STAGED_VERSION_NAME);
		}
		catch (final UnknownIdentifierException e)
		{
			bookstoreProductCatalogStaged = modelService.create(CatalogVersionModel.class);
			bookstoreProductCatalogStaged.setCatalog(bookstoreProductCatalog);
			bookstoreProductCatalogStaged.setVersion(STAGED_VERSION_NAME);
			modelService.save(bookstoreProductCatalogStaged);
		}

		// Create a customer
		customer = modelService.create(CustomerModel.class);
		customer.setUid("walt.whitman@poetry.org");
		customer.setName("Walt Whitman");
		customer.setPoints(0);
		modelService.save(customer);

		// Find the bookstoreProductCatalog:Staged catalog version to insert products into
		final CatalogVersionModel stagingCatalog = catalogVersionService.getCatalogVersion("bookstoreProductCatalog", "Staged");
		if (stagingCatalog == null)
		{
			fail("No bookstoreProductCatalog:Staged catalog version found");
		}

		// Create a Book with rewardPoints=40
		final BookModel book1 = modelService.create(BookModel.class);
		book1.setCode("bk_prod_0601");
		book1.setName("New and Selected Poems, Volume One", Locale.ENGLISH);
		book1.setCatalogVersion(stagingCatalog);
		book1.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		book1.setLanguage("en");
		book1.setPublisher("Beacon Press");
		book1.setISBN10("0807068780");
		book1.setISBN13("9780807068786");
		book1.setRentable(Boolean.valueOf(true));
		book1.setRewardPoints(Integer.valueOf(40));
		modelService.save(book1);

		// Create a Book with rewardPoints=15
		final BookModel book2 = modelService.create(BookModel.class);
		book2.setCode("bk_prod_0602");
		book2.setName("New and Selected Poems, Volume Two", Locale.ENGLISH);
		book2.setCatalogVersion(stagingCatalog);
		book2.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		book2.setLanguage("en");
		book2.setPublisher("Beacon Press");
		book2.setISBN10("0807068861");
		book2.setISBN13("9780807068861");
		book2.setRentable(Boolean.valueOf(true));
		book2.setRewardPoints(Integer.valueOf(15));
		modelService.save(book2);

		FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Unit} WHERE {code} = 'pieces'");
		final List<UnitModel> units = flexibleSearchService.<UnitModel> search(query).getResult();
		UnitModel pieces;
		if (units.isEmpty())
		{
			final UnitModel unit = modelService.create(UnitModel.class);
			unit.setCode("pieces");
			unit.setUnitType("pieces");
			modelService.save(unit);
			pieces = unit;
		}
		else
		{
			pieces = units.get(0);
		}


		query = new FlexibleSearchQuery("SELECT {pk} FROM {Currency} WHERE {isocode} = 'USD'");
		final List<CurrencyModel> currencies = flexibleSearchService.<CurrencyModel> search(query).getResult();
		CurrencyModel usd;
		if (currencies.isEmpty())
		{
			final CurrencyModel currency = modelService.create(CurrencyModel.class);
			currency.setIsocode("USD");
			modelService.save(currency);
			usd = currency;
		}
		else
		{
			usd = currencies.get(0);
		}

		//Create an Order
		order = modelService.create(OrderModel.class);
		order.setCurrency(usd);
		order.setDate(new Date());
		order.setDiscountsIncludeDeliveryCost(false);
		order.setDiscountsIncludePaymentCost(false);
		order.setNet(true);
		order.setUser(customer);

		//Create a cart entry for book 1
		OrderEntryModel entry = modelService.create(OrderEntryModel.class);
		entry.setProduct(book1);
		entry.setQuantity(1L);
		entry.setUnit(pieces);
		entry.setGiveAway(false);
		entry.setRejected(false);
		entry.setOrder(order);

		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		entries.add(entry);

		//Create a cart entry for book 2
		entry = modelService.create(OrderEntryModel.class);
		entry.setProduct(book2);
		entry.setQuantity(2L);
		entry.setUnit(pieces);
		entry.setGiveAway(false);
		entry.setRejected(false);
		entry.setOrder(order);

		entries.add(entry);

		// Add the Order Entries to the order and save it
		order.setEntries(entries);
		modelService.save(order);

	}

	/**
	 * initializeCustomers
	 *
	 * Create the objects necessary to test the getAllCustomersForLevel() method.
	 *
	 * Create five customers: 1 with BLUE reward level, one with SILVER, and three with GOLD Save the Ids of these
	 * customers in List<String> goldCustomerIds
	 *
	 */
	private void initializeCustomers()
	{
		// Initialize variable for the reward status levels
		blue = enumerationService.getEnumerationValue("RewardStatusLevel", "BLUE");
		silver = enumerationService.getEnumerationValue("RewardStatusLevel", "SILVER");
		gold = enumerationService.getEnumerationValue("RewardStatusLevel", "GOLD");

		// Delete the existing customers so we have an accurate count of GOLD customers
		// (remember: since we extend ServicelayerTransactionalTest, our changes are rolled back automatically)
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {Customer}");
		final List<CustomerModel> existingCustomers = flexibleSearchService.<CustomerModel> search(query).getResult();

		for (final CustomerModel cust : existingCustomers)
		{
			if (!cust.getUid().equals("anonymous"))
			{
				modelService.removeAll(cust.getOrders());
				modelService.removeAll(cust.getCarts());
				modelService.remove(cust);
			}
		}


		// Create a BLUE customer
		customer = modelService.create(CustomerModel.class);
		customer.setUid("walt.whitman@poetry.org");
		customer.setName("Walt Whitman");
		customer.setRewardStatusLevel(blue);
		modelService.save(customer);

		// Create a SILVER customer
		customer = modelService.create(CustomerModel.class);
		customer.setUid("octavio.paz@poetry.org");
		customer.setName("Octavio Paz");
		customer.setRewardStatusLevel(silver);
		modelService.save(customer);

		// Create a GOLD customer
		customer = modelService.create(CustomerModel.class);
		customer.setUid("mary.oliver@poetry.org");
		customer.setName("Mary Oliver");
		customer.setRewardStatusLevel(gold);
		modelService.save(customer);

		goldCustomerIds.add(customer.getUid());

		// Create a GOLD customer
		customer = modelService.create(CustomerModel.class);
		customer.setUid("pablo.neruda@poetry.org");
		customer.setName("Pablo Neruda");
		customer.setRewardStatusLevel(gold);
		modelService.save(customer);

		goldCustomerIds.add(customer.getUid());

		// Create a GOLD customer
		customer = modelService.create(CustomerModel.class);
		customer.setUid("rabindranath.tagore@poetry.org");
		customer.setName("Rabindranath Tagore");
		customer.setRewardStatusLevel(gold);
		modelService.save(customer);

		goldCustomerIds.add(customer.getUid());

	}

}
