import de.hybris.bootstrap.config.ConfigUtil
import de.hybris.platform.cms2.model.pages.AbstractPageModel
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel
import de.hybris.platform.core.Registry
import de.hybris.platform.jalo.extension.ExtensionInfo

import bsh.This
import my.bookstore.addons.trending.controllers.cms.*

def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())

def checkExtensionExists(String extName){
	final List<ExtensionInfo> allExtensions = ConfigUtil.getPlatformConfig(Registry.class).getExtensionInfosInBuildOrder()
	
	def ext = allExtensions.find {extension -> extension.getName().equals(extName)}
	
	if (ext == null){
		addError("NOT OK: There is no extension with the name ${extName} loaded in the system. " +
				"Make sure that you have created an extension called ${extName} and also have put it inside localextension.xml")
	} else{
		addLog("OK: The new extension called ${extName} is loaded into the system.")
	}
}

/********Check for trendingaddon*********************************/

def extName = "trendingaddon"
checkExtensionExists(extName)

/********Check bookFacade.getTrendingBooks()*********************/

try{
	bookFacade.getTrendingBooks(7).get(0)
	addLog("OK: bookFacade.getTrendingBooks() is returing trending books!")
} catch (NullPointerException){
	addError("NOT OK: bookFacade.getTrendingBooks() is not working properly!")
}

/********Check itemtype TrendingProductCarouselComponent********/

def componentName = "TrendingProductsCarouselComponent"
def componentPackage = "my.bookstore.addons.trending.model"
try{
	Class.forName(componentPackage+"."+componentName+"Model")
	addLog("OK: ${componentName} exists!")
} catch(ClassNotFoundException ex){
	addError("NOT OK: ${componentName} does not exist. "+
		"Make sure that you defined an item type for the component inside *items.xml")
}

/********Check TrendingProductsCarouselComponentController********/

def controllerName = "TrendingProductsCarouselComponentController"

// Since the groovy engine does not know about the classpath to the addon classes
// we check the addon controller's class file existence instead of initiating an
// object for checking
File classFile = new File(System.getProperty("HYBRIS_BIN_DIR") +
								"/custom/bookstore/bookstorestorefront/web/webroot/WEB-INF/classes/" +
								"my/bookstore/addons/trending/controllers/cms/${controllerName}.class")

if (classFile.exists()){
	addLog("OK: ${controllerName} exists!")
} else{
	addError("NOT OK: ${controllerName} does not exist. "+
		"Make sure that you defined the controller in the right place.")
}

/********Check trendingproductscarouselcomponent.jsp*************/

def addonDir = System.getProperty("HYBRIS_BIN_DIR") + "/custom/trendingaddon/"
def jspDir = "acceleratoraddon/web/webroot/WEB-INF/views/responsive/cms/"
def jspFile = new File(addonDir+jspDir+"trendingproductscarouselcomponent.jsp")
if (jspFile.exists()){
	addLog("OK: trendingproductscarouselcomponent.jsp has been created inside trendingaddon!")
} else{
	addError("NOT OK: the JSP file for the view has not been created or has not been put in the right directory!" +
		"Remember that it should be called trendingproductscarouselcomponent.jsp")
}

/**check TrendingProductsCarouselComponent in Section3 homepage**/

def contentCatOnline = catalogVersionService.getCatalogVersion("bookstoreContentCatalog","Online")
def homepage = cmsPageDao.findPagesByIdAndCatalogVersion("homepage",contentCatOnline).get(0)
def correct = false
def contentSlotsForPage = homepage.getContentSlots()
contentSlotsForPage.each{
	cs4page -> 
	if (cs4page.getPosition().equals("Section3")){
		cs4page.getContentSlot().getCmsComponents().each{
			cmsComponent ->
			if (cmsComponent.itemtype.equals("TrendingProductsCarouselComponent")){
				correct = true
			}
		}
	}
}
if (correct){
	addLog("OK: TrendingProductsCarouselComponent is placed in the correct position on the homepage!")
}else{
	addError("NOT OK: TrendingProductsCarouselComponent is not placed in the correct position on the homepage!" 
		+ " Make sure you have provided the expected localization for TrendingProductsCarouselComponent so that it can be recognized by the script!")
}

/********Print the Log******************************************/

printOutputLog()