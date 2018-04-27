import de.hybris.platform.commercefacades.product.data.ProductData
import de.hybris.platform.commercefacades.search.data.SearchStateData
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData
import de.hybris.platform.servicelayer.dto.converter.Converter

import my.bookstore.core.model.BookModel



def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())
i18NService.setCurrentLocale(Locale.ENGLISH)

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

String populatorStr = "my.bookstore.facades.populators.SearchResultProductRewardPointsPopulator"

checkPopulatorExists(commerceSearchResultProductConverter, populatorStr)

baseSiteService.setCurrentBaseSite(baseSiteService.getAllBaseSites().get(0),true)

def catModel = catalogService.getCatalogForId('bookstoreProductCatalog')
theModel = (BookModel) productService.getProductForCode(catModel.getActiveCatalogVersion(), "2121888594")

addLog("The value of RewardPoints in the model is ${theModel.getRewardPoints()}")

ProductSearchPageData<SearchStateData, ProductData> resultBundle = productSearchFacade.textSearch("How to Make Friends and Infuriate People")

def theDTO = resultBundle.results.get(0)

if (theDTO.getRewardPoints() == null){
	addError("NOT OK: RewardPoints is not passed to ${theDTO.getClass().getSimpleName()}")	
} else{
	addLog("OK: RewardPoints is populated correctly by ${populatorStr}")
	addLog("The value of RewardPoints in the DTO is ${theDTO.getRewardPoints()}")
}


printOutputLog()