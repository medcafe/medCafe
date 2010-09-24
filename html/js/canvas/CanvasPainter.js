/****************************************************************************************************
	Copyright (c) 2005, 2006 Rafael Robayna

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

	Additional Contributions by: Morris Johns
****************************************************************************************************/
function shape (x, y, width, height, type, color, options)
{
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	this.type = type;
	this.color = color;
	//alert(color);
	return this;
}

function createHiddenValues (shape, i) {
     
     	 return '<div class="shape" id="shape' + i + '" name="shape' + i + '" custom:type="' + shape.type +'" custom:x="' + shape.x +
        			'" custom:y="' + shape.y + '" custom:width="' + shape.width + '" custom:height="' + shape.height + '" custom:color="'+ shape.color+ '" custom:offset="0" />' ;
     
    };
    
    function createShapeElement (shape, shapeID) 
    {
       alert("shapeId " + shapeID);
       	$("#" + shapeID).attr("type", shape.type);
       	
     };
    
var CanvasPainter = CanvasWidget.extend({
	canvasName: "",
	canvasInterface: "",
	contextI: "",
	canvasWidth: 0,
	canvasHeight: 0,

	startPos: {x:-1,y:-1},
	curPos: {x:-1,y:-1},

	drawColor: "rgb(0,0,0)",  //need to change to drawColor...

	drawActions: null,
	curDrawAction: 0,

	curShape: null,
	
	cpMouseDownState: false,

	shapes: null,
	
	/***
		init(String canvasName, String canvasInterfaceName, Array position) 
				initializes the canvas elements, adds event handlers and 
				pulls height and width information from the canvas element

		Parameters:
			canvasName - the name of the bottom canvas element
			canvasInterfaceName - the name of the top canvas element
			canvasPos - the absolution position of both canvas elements, used for mouse tracking. 
				ex. {x: 10, y: 10}
	***/

	constructor: function(canvasName, canvasInterfaceName, position, width, height) {
		this.canvasName = canvasName;
		this.canvasInterface = document.getElementById(canvasInterfaceName);
		this.contextI = this.canvasInterface.getContext("2d");	
		this.inherit(canvasName, position);
		this.canvasHeight = this.canvas.getAttribute('height');
		this.canvasWidth = this.canvas.getAttribute('width');
		this.drawActions = [this.drawRectangle, this.drawCircle, this.clearCanvas];
		},

	initMouseListeners: function() {
		this.mouseMoveTrigger = new Function();
		if(document.all) {
			this.canvasInterface.attachEvent("onmousedown", this.mouseDownActionPerformed.bindAsEventListener(this));
			this.canvasInterface.attachEvent("onmousemove", this.mouseMoveActionPerformed.bindAsEventListener(this));
			this.canvasInterface.attachEvent("onmouseup", this.mouseUpActionPerformed.bindAsEventListener(this));
			attachEvent("mouseup", this.mouseUpActionPerformed.bindAsEventListener(this));
		} else {
			this.canvasInterface.addEventListener("mousedown", this.mouseDownActionPerformed.bindAsEventListener(this), false);
			this.canvasInterface.addEventListener("mousemove", this.mouseMoveActionPerformed.bindAsEventListener(this), false);
			this.canvasInterface.addEventListener("mouseup", this.mouseUpActionPerformed.bindAsEventListener(this), false);
			addEventListener("mouseup", this.mouseUpActionPerformed.bindAsEventListener(this), false);
		}
	},


	mouseDownActionPerformed: function(e) {
		this.startPos = this.getCanvasMousePos(e, this.position);
		this.context.lineJoin = "round";
		//Link mousemove event to the cpMouseMove Function through the wrapper
		this.mouseMoveTrigger = function(e) {
			this.cpMouseMove(e);
		};
    },
	
	cpMouseMove: function(e) {
		//alert(JSON.stringify($('#viewer').position()));
		this.setDrawingColor();
		this.curPos = this.getCanvasMousePos(e, this.position);

		if(this.curDrawAction == 0) {
			this.contextI.clearRect(0,0,this.canvasInterface.getAttribute('width'),this.canvasInterface.getAttribute('height'));
			this.drawRectangle(this.startPos, this.curPos, this.contextI, this.drawColor);
		} else if(this.curDrawAction == 1) {
			
				this.contextI.clearRect(0,0,this.canvasInterface.getAttribute('width'),this.canvasInterface.getAttribute('height'));
			this.drawCircle(this.startPos, this.curPos, this.contextI, this.drawColor);
			//console.log("CanvasPainter.js line 115 start pos x: " + this.startPos.x + " y: " + this.startPos.y);
			//console.log("CanvasPainter.js line 115 curr pos x: " + this.curPos.x + " y: " + this.curPos.y);
		}
		this.cpMouseDownState = true;
	},

	mouseUpActionPerformed: function(e) {
		if(!this.cpMouseDownState) return;
		this.cpMouseDownState = false;
		this.curPos = this.getCanvasMousePos(e, this.position);
		if(this.curDrawAction != 2 ) {
			this.setDrawingColor();
			this.drawActions[this.curDrawAction](this.startPos, this.curPos, this.context, this.drawColor);
			this.clearInterface();
			this.callWidgetListeners();
		

		if (this.shapes == null)
		{
			this.shapes = [currShape];
		}
		else
		{
			this.shapes.push(currShape);
		}
		
		var shapesCount = 0;
		//this.form = $('<div class="shape" id="shape' + this.shapes.length +'"></div>');
		$('.shape').each(function ()
		{
				shapesCount++;
		});
			
		var html = createHiddenValues(currShape, shapesCount);
		$("#canvas").append(html);
		}
		else 
		{
			clearCanvas();
		}
		this.mouseMoveTrigger = new Function();

	},

	//Draw Functions
	drawRectangle: function(pntFrom, pntTo, context, color) {
		context.beginPath();
		context.strokeRect(pntFrom.x, pntFrom.y, pntTo.x - pntFrom.x, pntTo.y - pntFrom.y);
		context.closePath();
		var rect = new shape(pntFrom.x, pntFrom.y, pntTo.x - pntFrom.x, pntTo.y - pntFrom.y,"rectangle", color);
		currShape = rect;
	},
	drawCircle: function (pntFrom, pntTo, context, color) {
		
		var centerX = Math.max(pntFrom.x,pntTo.x) - Math.abs(pntFrom.x - pntTo.x)/2;
		var centerY = Math.max(pntFrom.y,pntTo.y) - Math.abs(pntFrom.y - pntTo.y)/2;
		context.beginPath();
		var distance = Math.sqrt(Math.pow(pntFrom.x - pntTo.x,2) + Math.pow(pntFrom.y - pntTo.y,2)); 
		context.arc(centerX, centerY, distance/2,0,Math.PI*2 ,true);
		context.stroke();
		context.closePath();
		var circle = new shape(centerX, centerY, distance/2, 0,"circle" , color );
		currShape = circle;
	},
	//GH needed as zooming seems to take slightly different parameters - width is half circle distance
	drawCircleZoom: function (pntFrom, pntTo, context, color) {
		var centerX = Math.max(pntFrom.x,pntTo.x) - Math.abs(pntFrom.x - pntTo.x);
		var centerY = Math.max(pntFrom.y,pntTo.y) - Math.abs(pntFrom.y - pntTo.y)/2;
		context.beginPath();
		var distance = Math.sqrt(Math.pow(pntFrom.x - pntTo.x,2) + Math.pow(pntFrom.y - pntTo.y,2)); 
		context.arc(centerX, centerY, distance/2,0,Math.PI*2 ,true);
		context.stroke();
		context.closePath();
		var circle = new shape(centerX, centerY, distance/2, 0,"circle", color );
		currShape = circle;
	},
	drawLine: function(pntFrom, pntTo, context) {
		context.beginPath();
		context.moveTo(pntFrom.x,pntFrom.y);
		context.lineTo(pntTo.x,pntTo.y);
		context.stroke();
		context.closePath();
	},
	drawPencil: function(pntFrom, pntTo, context) {
		context.save();
		context.beginPath();
		context.lineCap = "round";
		context.moveTo(pntFrom.x,pntFrom.y);
		context.lineTo(pntTo.x,pntTo.y);
		context.stroke();
		context.closePath();
		context.restore();
	},
	drawBrush: function(pntFrom, pntTo, context) {
		context.beginPath();
		context.moveTo(pntFrom.x, pntFrom.y);
		context.lineTo(pntTo.x, pntTo.y);
		context.stroke();
		context.closePath();
	},
	clearCanvas: function(context) {
		
		canvasPainter.context.beginPath();	
		canvasPainter.context.clearRect(0,0,canvasPainter.canvasWidth,canvasPainter.canvasHeight);
		canvasPainter.context.closePath();
		canvasPainter.shapes = null;
		
	},
	clearInterface: function() {
		
		this.contextI.beginPath();
		this.contextI.clearRect(0,0,this.canvasWidth,this.canvasHeight);
		this.contextI.closePath();
	//	this.shapes == null;
	},
	clearShapes: function() {
		
		$('#canvas').html("");
	},
	//Setter Methods
	setColor: function(color) {
		
		//this.context.globalAlpha = 0.4;
		this.context.fillStyle = color;
		this.context.strokeStyle = color;
		this.drawColor = color;
		this.contextI.fillStyle = color;
		this.contextI.strokeStyle = color;
	},
	setDrawingColor: function() {
		var colorIn = $('#colorSelector2').find('div').css('backgroundColor');
		this.setColor(colorIn);
		//this.context.globalAlpha = 0.04;
	},

	setLineWidth: function(lineWidth) {
		this.context.lineWidth = lineWidth;
		this.contextI.lineWidth = lineWidth;
	},
	
	//TODO: look into the event responce/calling for this function
	setDrawAction: function(action) {
		if(action == 2) {
		
			var lastAction = this.curDrawAction;
			this.curDrawAction = action;
			this.callWidgetListeners();
			this.curDrawAction = lastAction;
			this.clearCanvas(this.context);
			this.clearShapes();
			this.shapes == null;
		} else {
			this.curDrawAction = action;
			this.context.fillStyle = this.drawColor;
			this.context.strokeStyle = this.drawColor;
		}
	},
	
	getDistance: function(pntFrom, pntTo) {
		return Math.sqrt(Math.pow(pntFrom.x - pntTo.x,2) + Math.pow(pntFrom.y - pntTo.y,2));
	}
});

