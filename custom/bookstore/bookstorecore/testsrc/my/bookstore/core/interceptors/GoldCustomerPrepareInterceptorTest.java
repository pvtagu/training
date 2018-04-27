
package my.bookstore.core.interceptors;

import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import my.bookstore.core.interceptors.GoldCustomerPrepareInterceptor;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.UserGroupDao;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import my.bookstore.core.enums.RewardStatusLevel;

/**
 * GoldCustomerPrepareInterceptorTest
 * 
 * The following class supports a Tesd-Driven-Development (TDD) approach
 * for the SAP Hybris Commerce Developer training, part 2.
 * 
 * It tests the interceptor that assigns a user to a group (goldcustomergroup) if s/he
 * has the reward status level GOLD.
 *
 */
@IntegrationTest
public class GoldCustomerPrepareInterceptorTest extends ServicelayerTransactionalTest
{
	@Resource
	private ModelService modelService;

	@Resource
	private UserGroupDao userGroupDao;
	
	@Resource
	private EnumerationService enumerationService;

	@Resource
	private GoldCustomerPrepareInterceptor goldCustomerPrepareInterceptor;

	private CustomerModel goldCustomer;
	private CustomerModel silverCustomer;
	private CustomerModel blueCustomer;

	private RewardStatusLevel blue; 
	private RewardStatusLevel silver; 
	private RewardStatusLevel gold;

   private static final String GOLD_CUSTOMER = "goldcustomergroup";
   private UserGroupModel goldCustomerGroup;


   /**
    * testOnPrepare
    * 
	 *	   The initialize() method created three customerModel objects: 
	 *       • blueCustomer has a reward status level of blue
	 *       • silverCustomer has a reward status level of silver
    *       • goldCustomer has a reward status level of gold
	 *     	
    *       It also created the group goldCustomerGroup, and saved it to the db.
    *    
    */

	@Test
	public void testOnPrepare()
	{
		
      /* ----------------------------------------------------------------------------------------------
	      TODO exercise 12.2: Using a customerModel object (not saved to the db), manually invoke the
		                       onPrepare() method of the goldCustomerInterceptor. Check that the customer
		                       is added to the goldCustomerGroup if their reward status level is gold,
		                       and removed from that group if it is not.
		   
		   NB: the onPrepare() method in this interceptor doesn't use the context object (second 
		       argument), so pass a null value. 
      */
				
		if (goldCustomerPrepareInterceptor == null)
			fail("Could not resolve the bean goldCustomerPrepareInterceptor");
		//try {
			// 0. Uncomment try and catch blocks!
			
			// 1. Call the prepare interceptor on the blue customer. 
			//    Test that it is NOT assigned to the goldCustomerGroup
         //...
		   
			// 2. Call the prepare interceptor on the silver customer. 
			//    Test that it is NOT assigned to the goldCustomerGroup
         //... 
		   
			// 3. Call the prepare interceptor on the gold customer. 
			//    Test that it IS assigned to the goldCustomerGroup
         //... 
		   
			// 4. Demote the gold customer to either silver or blue.
		   //    Call the prepare interceptor on the demoted gold customer. 
			//    Test that it NO LONGER belongs to the goldCustomerGroup
         //... 
		   
		//}
		//catch (InterceptorException e) {
			//fail("Exception thrown by goldCustomerPrepareInterceptor: " + e.getMessage());
		//}
		
		
	}
	
	
	/**
	 * Constructor
	 */
	public GoldCustomerPrepareInterceptorTest() {
		super();
		}
	
	
   /**
    * initialize
    *  
    *    Set variables referring to the three reward levels: blue, silver, and gold.
    *    Set a goldCustomerGroup variable pointing to the group 'goldcustomergroup'.  
    *    Create a blue, silver, and gold user.
    *    
    */
	@Before
	public void initialize()
	{		
		// Set variables for the three reward levels we'll be using
		blue = enumerationService.getEnumerationValue("RewardStatusLevel", "BLUE");
		silver = enumerationService.getEnumerationValue("RewardStatusLevel", "SILVER");
		gold = enumerationService.getEnumerationValue("RewardStatusLevel", "GOLD");
				
		// Find the gold customer group. If it doesn't exist, create and save it
		goldCustomerGroup = userGroupDao.findUserGroupByUid(GOLD_CUSTOMER);
		if (goldCustomerGroup == null) {
			goldCustomerGroup = modelService.create(UserGroupModel.class);
			goldCustomerGroup.setUid(GOLD_CUSTOMER);
			goldCustomerGroup.setName("Gold Customer Group");		
			modelService.save(goldCustomerGroup);
		}

		// Create a BLUE customer
		blueCustomer = modelService.create(CustomerModel.class);
		blueCustomer.setUid("walt.whitman@poetry.org");
		blueCustomer.setName("Walt Whitman");
		blueCustomer.setRewardStatusLevel(blue);
		Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
		blueCustomer.setGroups(groups);

		// Create a SILVER customer
		silverCustomer = modelService.create(CustomerModel.class);
		silverCustomer.setUid("octavio.paz@poetry.org");
		silverCustomer.setName("Octavio Paz");
		silverCustomer.setRewardStatusLevel(silver);
		groups = new HashSet<PrincipalGroupModel>();
		silverCustomer.setGroups(groups);

		// Create a GOLD customer
      goldCustomer = modelService.create(CustomerModel.class);
      goldCustomer.setUid("mary.oliver@poetry.org");
      goldCustomer.setName("Mary Oliver");
      goldCustomer.setRewardStatusLevel(gold);
		groups = new HashSet<PrincipalGroupModel>();
		goldCustomer.setGroups(groups);

	}
	
 
}

