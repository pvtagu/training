import de.hybris.platform.servicelayer.dto.converter.Converter

import my.bookstore.core.model.BookModel
import my.bookstore.facades.product.data.BookData

def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())

def checkPopulatorExists(Converter theConverter, String thePopulatorName){
	def exists = false
	theConverter.getProperties().get("populators").each{
		populator -> exists = (populator.toString().contains(thePopulatorName) || exists)}
	if (exists){
		addLog("OK: ${thePopulatorName} is added to the proper converter!")
	}else{
		addError("NOT OK: ${thePopulatorName} is not added to the proper converter!")
	}
}


def checkPopulatorForAttribute(theModel, theDTO, Converter theConverter, String theAttr, theVal){
	try{
		theModel.invokeMethod("set"+theAttr, theVal)
	} catch (MethodNotFoundException){
		addError("NOT OK: ${theAttr} is not defined as an attribute for ${theModel.getClass().getSimpleName()}")
	}
	theConverter.convert(theModel, theDTO)
	def dtoVal = theDTO.invokeMethod("get"+theAttr, null)
	
	if (dtoVal.equals(theVal)){
		addLog("OK: ${theConverter.getClass().getSimpleName()} populates the attribute ${theAttr} in ${theDTO.getClass().getSimpleName()}!")
	} else{
		addError("NOT OK: ${theConverter.getClass().getSimpleName()} is not working properly for populating attribute ${theAttr} in ${theDTO.getClass().getSimpleName()}")
	}
}


checkPopulatorExists(productConverter,"my.bookstore.facades.populators.BookstoreProductPopulator")


def catModel = catalogService.getCatalogForId('bookstoreProductCatalog')
theModel = (BookModel) productService.getProductForCode(catModel.getActiveCatalogVersion(), "2121888594")
def theDTO = BookData.class.newInstance()

checkPopulatorForAttribute(theModel, theDTO, productConverter, "RewardPoints", 2422)
checkPopulatorForAttribute(theModel, theDTO, productConverter, "Edition", "4")

printOutputLog()