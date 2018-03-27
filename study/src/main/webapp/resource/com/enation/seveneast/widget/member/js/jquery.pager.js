(function($) {

	$.fn.pager = function(options) {

		var opts = $.extend({}, $.fn.pager.defaults, options);

		return this.each(function() {

					$.data(this, "opts", opts);
					// empty out the destination element and then render out the
					// pager with the supplied options
					$(this).empty().append(renderpager(
							parseInt(options.pagenumber),
							parseInt(options.pagecount),
							options.buttonClickCallback));

					// specify correct cursor activity
					$('.pages li').mouseover(function() {
								document.body.style.cursor = "pointer";
							}).mouseout(function() {
								document.body.style.cursor = "auto";
							});
				});
	};

	// render and return the pager with the supplied options
	function renderpager(pagenumber, pagecount, buttonClickCallback) {

		// setup $pager to hold render
		var $pager = $('<ul class="pages"></ul>');

		// add in the previous and next buttons
		$pager.append(renderButton('&lt;&lt;', pagenumber, pagecount,
				buttonClickCallback)).append(renderButton('&lt;', pagenumber,
				pagecount, buttonClickCallback));

		// pager currently only handles 10 viewable pages ( could be easily
		// parameterized, maybe in next version ) so handle edge cases
		var startPoint = 1;
		var endPoint = 9;

		if (pagenumber > 4) {
			startPoint = pagenumber - 4;
			endPoint = pagenumber + 4;
		}

		if (endPoint > pagecount) {
			startPoint = pagecount - 8;
			endPoint = pagecount;
		}

		if (startPoint < 1) {
			startPoint = 1;
		}

		// loop thru visible pages and render buttons
		for (var page = startPoint; page <= endPoint; page++) {

			var currentButton = $('<li class="page-number">' + (page) + '</li>');

			page == pagenumber
					? currentButton.addClass('pgCurrent')
					: currentButton.click(function() {
								refresh(this.firstChild.data, $(this));
								buttonClickCallback(this.firstChild.data);
							});
			currentButton.appendTo($pager);
		}

		// render in the next and last buttons before returning the whole
		// rendered control back.
		$pager.append(renderButton('&gt;', pagenumber, pagecount,
				buttonClickCallback)).append(renderButton('&gt;&gt;', pagenumber,
				pagecount, buttonClickCallback));

		return $pager;
	}
	function refresh(pagenumber, btn) {
		var pager = btn.parent("ul").parent("div");
		var opts = $.data(pager.get(0), "opts");
		pager.empty().append(renderpager(parseInt(pagenumber),
				parseInt(opts.pagecount), opts.buttonClickCallback));

		$('.pages li').mouseover(function() {
					document.body.style.cursor = "pointer";
				}).mouseout(function() {
					document.body.style.cursor = "auto";
				});

	}

	// renders and returns a 'specialized' button, ie 'next', 'previous' etc.
	// rather than a page number button
	function renderButton(buttonLabel, pagenumber, pagecount,
			buttonClickCallback) {

		var $Button = "";
		
		switch (buttonLabel) {
		case "&lt;&lt;" :
			 $Button = $('<li class="pgNext" title="第一页">' + buttonLabel + '</li>');
			break;
		case "&lt;" :
			 $Button = $('<li class="pgNext" title="上一页">' + buttonLabel + '</li>');
			break;
		case "&gt;" :
			 $Button = $('<li class="pgNext" title="下一页">' + buttonLabel + '</li>');
			break;
		case "&gt;&gt;" :
			 $Button = $('<li class="pgNext" title="最后一页">' + buttonLabel + '</li>');
			break;
	}
		var destPage = 1;

		// work out destination page for required button type
		switch (buttonLabel) {
			case "&lt;&lt;" :
				destPage = 1;
				break;
			case "&lt;" :
				destPage = pagenumber - 1;
				break;
			case "&gt;" :
				destPage = pagenumber + 1;
				break;
			case "&gt;&gt;" :
				destPage = pagecount;
				break;
		}

		// disable and 'grey' out buttons if not needed.
		if (buttonLabel == "&lt;&lt;" || buttonLabel == "&lt;") {
			pagenumber <= 1 ? $Button.addClass('pgEmpty') : $Button.click(
					function() {
						refresh(destPage, $(this));
						buttonClickCallback(destPage);
					});
		} else {
			pagenumber >= pagecount ? $Button.addClass('pgEmpty') : $Button
					.click(function() {
								refresh(destPage, $(this));
								buttonClickCallback(destPage);
							});
		}

		return $Button;
	}

	// pager defaults. hardly worth bothering with in this case but used as
	// placeholder for expansion in the next version
	$.fn.pager.defaults = {
		pagenumber : 1,
		pagecount : 1
	};

})(jQuery);
