// http://stackoverflow.com/a/20598914
String.prototype.nthIndexOf = function(pattern, n) {
    var i = -1;

    while (n-- && i++ < this.length) {
        i = this.indexOf(pattern, i);
        if (i < 0) break;
    }

    return i;
}

var Client = function(url, ctx) {
	this.websocket = new WebSocket(url);
	this.ids = []
	this.data = {
		labels: [],
		datasets: [
			{
				label: "Feedback",
				fillColor: "rgba(181,126,220,0.5)",
				strokeColor: "rgba(181,126,220,0.8)",
				highlightFill: "rgba(181,126,220,0.75)",
				highlightStroke: "rgba(181,126,220,1)",
				data: []
			}
		]
	}
	this.options = {
		scaleBeginAtZero: true,
		scaleShowGridLines: false,
		responsive: true,
		maintainAspectRatio: false
	}
	this.barChart = new Chart(ctx).Bar(this.data, this.options);
	this.ignore = 0;

 	this.update = function() {
		this.barChart.update();
	}

	this.parseData = function(data) {
		var index = data.indexOf(" ");
		var parse = {};
		// If there's no space, then assume the data as the whole command.
		if(index < 0)
		{
			parse.cmd = data;
		}
		else
		{
			parse.cmd = data.substring(0, index);
			parse.args = [];
			switch(parse.cmd)
			{
				case 'ADD':
					var lastIndex = data.lastIndexOf(" ");
					var secondToLast = data.substring(0,lastIndex).lastIndexOf(" ");
					// Exclude the name's quotation marks.
					parse.args.push(data.substring(index+2, secondToLast-1));
					parse.args.push(data.substring(secondToLast+1,lastIndex));
					parse.args.push(data.substring(lastIndex+1));
					break;
				case 'REMOVE':
				case 'UP':
				case 'DOWN':
					parse.args.push(data.substring(index+1));
					break;
			}
		}

		return parse;
	}

	// Cached this for parent object access.
	var self = this;
	this.websocket.onmessage = function(event) {
		var parse = self.parseData(event.data);
		if(self.ignore && parse.cmd != "UNHELP") {
			console.log(event.data);
			return;
		}
		switch(parse.cmd) {
			case 'ADD':
				var name = parse.args[0];
				var id = parseInt(parse.args[1]);
				var votes = parseInt(parse.args[2]);
				self.ids.push(id);
				self.barChart.addData([votes], name);
				// self.update();
				break;
			case 'REMOVE':
				var index = self.ids.indexOf(parseInt(parse.args[0]));
				self.entries.splice(index, 1);
				self.barChart.removeData(id);
				// self.update();
				break;
			case 'UP':
				var index = self.ids.indexOf(parseInt(parse.args[0]));
				self.barChart.datasets[0].bars[index].value += 1;
				self.update();
				break;
			case 'DOWN':
				var index = self.ids.indexOf(parseInt(parse.args[0]));
				self.barChart.datasets[0].bars[index].value -= 1;
				self.update();
				break;
			case 'HELP':
				self.ignore = 1;
				break;
			case 'UNHELP':
				self.ignore = 0;
				break;
			default:
				console.log(event.data);
		}
	}
}