function ParsedQueryString() {
	this._init();
}

ParsedQueryString.version = '1.0';

ParsedQueryString.prototype = {
	_init : function() {
		this._parameters = {};

		if (location.search.length <= 1)
			return;
		var pairs = location.search.substr(1).split(/[&;]/);
		for (var i = 0; i < pairs.length; i++) {
			var pair = pairs[i].split(/=/);
			var name = this._decodeURL(pair[0]);
			if (Boolean(pair[1])) {
				var value = this._decodeURL(pair[1]);
				if (Boolean(this._parameters[name]))
					this._parameters[name].push(value);
				else
					this._parameters[name] = [value];
			}
		}
	},

	_decodeURL : function(url) {
		return decodeURIComponent(url.replace(/\+/g, " "));
	},

	param : function(name) {
		if (Boolean(this._parameters[name]))
			return this._parameters[name][0];
		else
			return "";
	},

	params : function(name) {
		if (Boolean(name)) {
			if (Boolean(this._parameters[name])) {
				var values = [];
				for (var i = 0; i < this._parameters[name].length; i++)
					values.push(this._parameters[name][i]);
				return values;
			} else
				return [];
		} else {
			var names = [];
			for (var name in this._parameters)
				names.push(name);
			return names;
		}
	}
};
