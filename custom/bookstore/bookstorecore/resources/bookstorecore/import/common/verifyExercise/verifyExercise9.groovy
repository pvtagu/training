import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.catalog.model.CatalogModel;
import java.util.List;

def script = new GroovyScriptEngine( '.' ).with {
    loadScriptByName( '../../Logger.groovy' ) //for this to work, Logger.groovy should've already been put inside platform directory
}
this.metaClass.mixin script

//NOTE: If we run scripts from ant there is no user session information
userService.setCurrentUser(userService.getAdminUser())

def checkPage(String code,String catalogName,String version ){
    CatalogVersionModel catalogVersionModel=catalogVersionService.getCatalogVersion(catalogName,version)
    List<AbstractPageModel>pages= cmsPageDao.findPagesByIdAndCatalogVersion(code,catalogVersionModel)
    if(pages.isEmpty()){
        addError("NOT OK: Page "+code+" doesn't exist in "+catalogName+":"+version);
    }else{        
        PageTemplateModel pageTemplate=pages.get(0).getMasterTemplate()
        if(pageTemplate.getFrontendTemplateName()==null || !pageTemplate.getFrontendTemplateName().contains('landingLayout2Page')){
            addError("NOT OK: Page "+code+" in "+catalogName+":"+version+" doesn't have landingLayout2Page page template");
        }else{
            addLog("OK: Page "+code+" in "+catalogName+":"+version+" has landingLayout2Page page template");
        }
        List<ContentSlotNameModel> contentSlots=pageTemplate.getAvailableContentSlots()
        if(contentSlots.isEmpty()){
            addError('NOT OK: Page'+code+' doesnt have assigned content slots')
        }else{
            addLog('OK: Page'+code+' has assigned content slots')
        }
       
        
    } 

}

def checkSite(String siteUid){
 
    CMSSiteModel baseSite= baseSiteService.getBaseSiteForUID(siteUid)
    if(baseSite==null){
        addError("NOT OK: CMSSite "+siteUid+" doesn't exist")
    }else{
        List<BaseStoreModel> stores=baseSite.getStores()
        if(stores.isEmpty()){
            addError("NOT OK: CMSSite "+siteUid+" doesn't have assigned stores")
        }else{
            addLog("OK: CMSSite "+siteUid+" has assigned stores")
        }
        if(baseSite.getUrlPatterns()==null || baseSite.getUrlPatterns().equals("")){
            addError("NOT OK: CMSSite "+siteUid+" doesn't have assigned url patterns")
        }else{
            addLog("OK: CMSSite "+siteUid+" has assigned url patterns")
        }
        if(baseSite.getDefaultCatalog()!=null && "bookstoreProductCatalog".equals(baseSite.getDefaultCatalog().getId())){
            addLog("OK: CMSSite "+siteUid+" has bookstoreProductCatalog as default catalog")
        }else{
            addError("NOT OK: CMSSite "+siteUid+" doesn't have bookstoreProductCatalog as default catalog")
        }
    
        boolean foundContentCatalog=false;
        for(ContentCatalogModel catalog:baseSite.getContentCatalogs()){
            if("bookstoreContentCatalog".equals(catalog.getId())){
                foundContentCatalog=true;
                break;
            }
        }
        if(foundContentCatalog){
            addLog("OK: CMSSite "+siteUid+" has bookstoreContentCatalog as content catalog")
        }else{
            addError("NOT OK: CMSSite "+siteUid+" doesn't have bookstoreContentCatalog as content catalog")
        }
        if(baseSite.getActive()){
            addLog("OK: CMSSite "+siteUid+" is active")
        }else{
            addError("NOT OK: CMSSite "+siteUid+" is not active") 
        }
        if(baseSite.getStartingPage()!=null && "homepage".equals(baseSite.getStartingPage().getUid())){
            addLog("OK: CMSSite "+siteUid+" has homepage as starting page")
        }else{
            addError("NOT OK: CMSSite "+siteUid+" doesn't have homepage as starting page")
        }
    }
}

def checkBaseStore(String basestoreUid){
    try{
        BaseStoreModel baseStore=baseStoreService.getBaseStoreForUid(basestoreUid)
        if(baseStore==null){
            addError("NOT OK: basestore "+basestoreUid+" not found")
        }else{
            List<CatalogModel> catalogs=baseStore.getCatalogs()
            boolean productCatFound=false;
            boolean classificationFound=false;
            for(CatalogModel catalog:catalogs){
                if("bookstoreProductCatalog".equals(catalog.getId())){
                    productCatFound=true;
                }else if("BookstoreClassification".equals(catalog.getId())){
                    classificationFound=true;
                }
            }
            if(productCatFound&&classificationFound){
                addLog('OK: catalogs found')
            }else if(productCatFound){
                addError('NOT OK: BookstoreClassification is not assigned as catalog to basestore')
            }else{
                addError('NOT OK: bookstoreProductCatalog is not assigned as catalog to basestore')
            }
        }
    }catch(de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException ex){
        addError("NOT OK: basestore "+basestoreUid+" not found")
    }
    
}

checkPage('homepage','bookstoreContentCatalog','Staged')
checkPage('homepage','bookstoreContentCatalog','Online')
checkSite('bookstore')
checkBaseStore('bookstore')
printOutputLog()