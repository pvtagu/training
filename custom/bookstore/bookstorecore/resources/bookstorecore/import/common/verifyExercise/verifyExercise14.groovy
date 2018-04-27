import de.hybris.platform.payment.commands.SubscriptionAuthorizationCommand
import de.hybris.platform.payment.commands.factory.CommandFactory
import de.hybris.platform.payment.commands.request.SubscriptionAuthorizationRequest
import de.hybris.platform.payment.commands.result.AuthorizationResult
import de.hybris.platform.store.BaseStoreModel
import de.hybris.platform.payment.dto.TransactionStatus

def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())

def commandName = 'SubscriptionAuthorizationMockCommand'
def totalValueForTest = 3001
def paymentProvider = 'Bookstore_Mockup'
def basestoreUid = 'bookstore'
try{
	BaseStoreModel baseStore=baseStoreService.getBaseStoreForUid(basestoreUid)
	if(baseStore==null){
		addError("NOT OK: basestore $basestoreUid not found")
	}else{
		def curPaymentProvider = baseStore.getPaymentProvider()
		if (paymentProvider.equals(curPaymentProvider)){ 
			addLog("OK: payment provider $paymentProvider is assigned to store $basestoreUid")
			CommandFactory commandFactory = commandFactoryRegistry.getFactory(curPaymentProvider)
			command = commandFactory.createCommand(SubscriptionAuthorizationCommand.class)
			if (command.class.simpleName.equals(commandName)){
				addLog("OK: found $commandName inside $paymentProvider")
				SubscriptionAuthorizationRequest mockRequest = new SubscriptionAuthorizationRequest('', 'valid', Currency.getInstance('EUR'), totalValueForTest, null, curPaymentProvider)
				AuthorizationResult result = command.perform(mockRequest)
				if (TransactionStatus.REJECTED.equals(result.getTransactionStatus())){
					addLog("OK: $commandName rejected an order with total value $totalValueForTest")
				}else{
					addError("NOT OK: $commandName accepted an order with total value $totalValueForTest, but it shouldn't!")
				}
			}else{
				addError("NOT OK: could not find $commandName inside $paymentProvider")
			}
		}else{
			addError("NOT OK: payment provider $paymentProvider is NOT assigned to store $basestoreUid")
		}
	}
}catch(de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException ex){
	addError("NOT OK: basestore $basestoreUid not found")
}


printOutputLog()