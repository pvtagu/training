/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
//Makes Color Box Responsive
var cboxOptions = {
	width: '95%',
	height: '95%',
	maxWidth: '960px',
	maxHeight: '960px',
}

$('.cbox-link').colorbox(cboxOptions);

$(window).resize(function(){

    if(!$('#colorbox').hasClass('variantSelectMobile')){
        $.colorbox.resize({
            width: window.innerWidth > parseInt(cboxOptions.maxWidth) ? cboxOptions.maxWidth : cboxOptions.width,
            height: window.innerHeight > parseInt(cboxOptions.maxHeight) ? cboxOptions.maxHeight : cboxOptions.height
        });
    }
});


ACC.colorbox = {
	config: {
		maxWidth:"100%",
		opacity:0.7,
		width:"auto",
		transition:"none",
		close:'<span class="glyphicon glyphicon-remove"></span>',
		title:'<div class="headline"><span class="headline-text">{title}</span></div>',
		onComplete: function() {
			$.colorbox.resize();
			ACC.common.refreshScreenReaderBuffer();
		},
		onClosed: function() {
			ACC.common.refreshScreenReaderBuffer();
		}
	},

	open: function(title,config){
		var config = $.extend({},ACC.colorbox.config,config);
		config.title = config.title.replace(/{title}/g,title);
		return $.colorbox(config);
	},

	resize: function(){
		$.colorbox.resize();
	},

	close: function(){
		$.colorbox.close();
	}
};

