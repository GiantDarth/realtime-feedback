var Client = function(url, canvas, btn, input) {
	this.websocket = new WebSocket(url);
	this.names = [];
	this.ids = [];
	this.canvas = canvas;
	this.btn = btn;
	this.input = input;
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
		scaleStartValue: -1,
		scaleShowGridLines: false,
		responsive: true,
		maintainAspectRatio: false
	}
	this.barChart = new Chart(this.canvas.getContext("2d")).Bar(this.data, this.options);
	// Use custom removeData atIndex to remove the data at an index.
	this.barChart.removeData = function(atIndex) {
		this.scale.xLabels.splice(atIndex, 1);
		this.scale.valuesCount--;
		this.scale.fit();

		// Then re-render the chart.
		Chart.helpers.each(this.datasets,function(dataset){
			dataset.bars.splice(atIndex, 1);
		}, this);

		this.update();
	}
	this.ignore = 0;

	canvas.ondblclick = function(event) {
		var activeBar = self.barChart.getBarsAtEvent(event)[0];
		if(!activeBar)
		{
			return;
		}
		var index = self.names.indexOf(activeBar.label);
		self.websocket.send("UP " + self.ids[index]);
	}

	this.btn.onclick = function(event) {
		var name = self.input.value;
		if(!name) { return; }
		if(name.indexOf('\"') >= 0)
		{
			window.alert('Value cannot contain quotes.')
			return;
		}
		for(n in self.names)
		{
			if(self.names[n].toUpperCase() == name.toUpperCase())
			{
				window.alert('\"' + name + '\" is already used.')
				return;
			}
		}
		self.websocket.send('ADD \"' + name + '\"');
	}

	this.canvas.ondragstart = function(event) {
		var img = document.createElement('img');
		img.style.opacity = 0;
		event.dataTransfer.setDragImage(img, 0, 0);

		var activeBar = self.barChart.getBarsAtEvent(event)[0];
		if(!activeBar)
		{
			return;
		}
		var index = self.names.indexOf(activeBar.label);
		if(self.barChart.datasets[0].bars[index].value <= 0)
		{
			return;
		}

		self.websocket.send("DOWN " + self.ids[index]);
	}

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
					parse.args.push(data.substring(secondToLast+1, lastIndex));
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
				self.names.push(name);
				self.barChart.addData([votes], name);
				// self.update();
				break;
			case 'REMOVE':
				var index = self.ids.indexOf(parseInt(parse.args[0]));
				self.ids.splice(index, 1);
				self.names.splice(index, 1);
				self.barChart.removeData(index);
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