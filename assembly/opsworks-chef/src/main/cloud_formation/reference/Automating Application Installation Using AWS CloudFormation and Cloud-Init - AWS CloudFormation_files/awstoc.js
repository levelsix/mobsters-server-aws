$(document).ready(function () {
    var browser = navigator.appName;
    var ie = "Microsoft Internet Explorer";
	var ua = navigator.userAgent.toLowerCase();
	var mobile = (ua.indexOf("mobile") != -1);    
	if (!mobile) { mobile = (ua.indexOf("silk/1.0") != -1); }    

	if (!mobile) {

	    document.getElementsByTagName("body")[0].style.overflow = "hidden";

		$( "li.awstoc.closed ul" ).hide();		
		
		var thispageTocEntry = $("li.awstoc a[href='" + thispage + "']");
		if (thispageTocEntry.length) {
			if (thispageTocEntry.position().top > $( "#divLeft" ).height()) {
				$( "#divLeft" ).scrollTop(thispageTocEntry.position().top-5);
			}
		}
		
		$( "li.awstoc" ).bind("click", function(event) {
			event.stopPropagation();

			if (event.target.nodeName == "LI") {
				if ($( event.target ).hasClass("closed") || $( event.target ).hasClass("opened")) {
					$( event.target ).toggleClass("closed opened");
					if ($( event.target ).hasClass("closed")) {
						$( event.target ).children("ul").hide();
					}
					if ($( event.target ).hasClass("opened")) {
						$( event.target ).children("ul").show();
					}
				}
			}
		});


		if ((browser != ie) || ((browser == ie) && (navigator.appVersion >= 5.0))) {


		    $("#divLeft").resizable({
		    });


		    $("#divLeft").bind("resize", function (event, ui) {
		        resizePanes();
		    });

		}


		resizePanes();
		window.onresize = resizePanes;
	
		if (location.hash != null && location.hash.length > 0 && ua.indexOf("firefox") > 0) {
			location.hash = location.hash;
		}
	}
});

function resizePanes() {
	var setWidth = $("#divLeft").width();
	var headerWidth = $("#divHeader").width();
	var headerHeight = $("#divHeader").height();
	$("#divTOC").width(setWidth - 6);
	$("#divRight").width(headerWidth - setWidth - 5);
	$("#divRight").height($(window).height() - headerHeight - 10);
	$("#divContent").height($(window).height() - headerHeight - 10);
	$("#divLeft").height($(window).height() - headerHeight - 10);
}

function searchFormSubmit(formElement) {
    //#facet_doc_product=Amazon+CloudFront&amp;facet_doc_guide=Developer+Guide+(API+Version+2012-07-01)
    var so = $("#sel").val();
    if (so.indexOf("documentation") === 0) {
        var this_doc_product = $("#this_doc_product").val();
        var this_doc_guide =  $("#this_doc_guide").val();
        var action = "";
        var facet = "";
        if (so === "documentation-product" || so === "documentation-guide") {
            action += "?doc_product=" + encodeURIComponent(this_doc_product);
            facet += "#facet_doc_product=" + encodeURIComponent(this_doc_product);
            if (so === "documentation-guide") {
                action += "&doc_guide=" + encodeURIComponent(this_doc_guide);
                facet += "&facet_doc_guide=" + encodeURIComponent(this_doc_guide);
            }
        }

        if (navigator.appName == 'Microsoft Internet Explorer')
	    {
            var sq = $("#sq").val();
            action += "&searchPath=" + encodeURIComponent(so);
            action += "&searchQuery=" + encodeURIComponent(sq);
            window.location.href = "/search/doc-search.html" + action + facet;
            return false;
        } else {
            formElement.action = "/search/doc-search.html" + facet;
        }
    } else {
        formElement.action = "http://aws.amazon.com/search";
    }
    return true;
}
