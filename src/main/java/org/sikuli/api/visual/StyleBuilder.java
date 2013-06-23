package org.sikuli.api.visual;

import java.awt.Color;

import org.sikuli.api.visual.element.Element;

public class StyleBuilder {

	final private Element element;
	final private Canvas canvas;
	public StyleBuilder(Canvas canvas, Element element) {
		this.element = element;
		this.canvas = canvas;
	}

	public StyleBuilder withLineColor(Color color){
		element.lineColor = color;
		return this;
	}

	public StyleBuilder withColor(Color color){
		element.color = color;
		return this;
	}
	
	public StyleBuilder withBackgroundColor(Color color) {
		element.backgroundColor = color;
		return this;
	}		
	
	public StyleBuilder withFontSize(int size){
		element.fontSize = size;
		return this;
	}

	public StyleBuilder withLineWidth(int width){
		element.lineWidth = width;
		return this;
	}
	
	public StyleBuilder withVerticalAlignmentMiddle(){
		element.verticalAlignment = Element.VerticalAlignment.MIDDLE;
		return this;
	}
	
	public StyleBuilder withVerticalAlignmentTop(){
		element.verticalAlignment = Element.VerticalAlignment.TOP;
		return this;
	}
	
	public StyleBuilder withVerticalAlignmentBottom(){
		element.verticalAlignment = Element.VerticalAlignment.BOTTOM;
		return this;
	}
	
	public StyleBuilder withHorizontalAlignmentLeft(){
		element.horizontalAlignment = Element.HorizontalAlignment.LEFT;
		return this;
	}

	public StyleBuilder withHorizontalAlignmentCenter(){
		element.horizontalAlignment = Element.HorizontalAlignment.CENTER;
		return this;
	}

	public StyleBuilder withHorizontalAlignmentRight(){
		element.horizontalAlignment = Element.HorizontalAlignment.RIGHT;
		return this;
	}
	
	public void display(int seconds){
		canvas.display(seconds);
	}

	public void display(double seconds){
		canvas.display(seconds);
	}


}