package org.sikuli.api.visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.sikuli.api.ScreenLocation;
import org.sikuli.api.ScreenRegion;

import com.google.common.collect.Lists;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolox.nodes.PShadow;


class Element {
	public int x;
	public int y;
	public int width;
	public int height;	

	public Color lineColor = Color.red;
	public Color color = Color.black;
	public int lineWidth = 2;
	public float fontSize = 12;

	public PNode createPNode(){
		return new PNode();
	}
}

class BoxElement extends Element {
}

class CircleElement extends Element {
}

class LabelElement extends Element {
	public String text;
}

class ImageElement extends Element {
	public BufferedImage image;
}

class PNodeFactory {

	static public PNode createFrom(Element element){
		Class<? extends Element> clazz = element.getClass();
		if (clazz == LabelElement.class){
			return createFrom((LabelElement)element);
		}else if (clazz == BoxElement.class){
			return createFrom((BoxElement) element);
		}else if (clazz == CircleElement.class){
			return createFrom((CircleElement) element);
		}else if (clazz == ImageElement.class){
			return createFrom((ImageElement) element);
		}	
		return new PNode();
	}

	static public PNode createFrom(LabelElement element){
		final PText txt = new PText(element.text);
		txt.setTextPaint(Color.black);
		txt.setPaint(Color.yellow);
		txt.setTextPaint(element.color);
		txt.setFont(txt.getFont().deriveFont(element.fontSize));

		PNode labelNode = new PNode();
		labelNode.setPaint(Color.yellow);
		labelNode.addChild(txt);
		labelNode.setHeight(txt.getHeight()+2);
		labelNode.setWidth(txt.getWidth()+4);
		txt.setOffset(2,1);


		labelNode.setOffset(element.x, element.y);
		return addShadow(labelNode);
	}
	
	static public PNode createFrom(CircleElement element){
		PPath p = PPath.createEllipse(1,1,element.width,element.height);
		p.setStrokePaint(element.lineColor);
		p.setPaint(null);		
		p.setStroke(new BasicStroke(element.lineWidth));

		PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight()+4);
		foregroundNode.setWidth(p.getWidth()+4);
		p.setOffset(2,2);

		foregroundNode.setOffset(element.x, element.y);

		return addShadow(foregroundNode);
	}

	static public PNode createFrom(BoxElement element){
		PPath p = PPath.createRectangle(1,1,element.width,element.height);
		p.setStrokePaint(element.lineColor);
		p.setPaint(null);		
		p.setStroke(new BasicStroke(element.lineWidth));


		PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight()+4);
		foregroundNode.setWidth(p.getWidth()+4);
		p.setOffset(2,2);

		foregroundNode.setOffset(element.x, element.y);

		return addShadow(foregroundNode);
	}
	
	static public PNode createFrom(ImageElement element){
		PImage p = new PImage(element.image);

		PNode foregroundNode = new PNode();
		foregroundNode.addChild(p);
		foregroundNode.setHeight(p.getHeight()+4);
		foregroundNode.setWidth(p.getWidth()+4);
		p.setOffset(2,2);

		foregroundNode.setOffset(element.x, element.y);

		return addShadow(foregroundNode);
	}

	static public void setStyle(){

	}

	static private final Color SHADOW_PAINT = new Color(10, 10, 10, 200);
	static private PNode addShadow(PNode contentNode){

		PNode contentNodeWithShadow = new PNode();

		double xoffset = contentNode.getXOffset();
		double yoffset = contentNode.getYOffset();

		int blurRadius = 4;
		int tx = 5;
		int ty = 5;

		PShadow shadowNode = new PShadow(contentNode.toImage(), SHADOW_PAINT, blurRadius );		
		contentNode.setOffset(tx, ty);
		shadowNode.setOffset(tx - (2 * blurRadius) + 1.0d, ty - (2 * blurRadius) + 1.0d);

		contentNodeWithShadow.addChild(shadowNode);
		contentNodeWithShadow.addChild(contentNode);		      
		contentNodeWithShadow.setOffset(xoffset - tx  - blurRadius, yoffset - ty - blurRadius);
		contentNodeWithShadow.setBounds(0,0, contentNode.getWidth() + 2*blurRadius + tx, contentNode.getHeight() + 2*blurRadius + ty);
		return contentNodeWithShadow;
	}
}


abstract public class Canvas {

	private final List<Element> elements = 	Lists.newArrayList();;
	
	public class StyleBuilder {

		final private Element element;
		public StyleBuilder(Element element) {
			this.element = element;
		}

		public StyleBuilder withLineColor(Color color){
			element.lineColor = color;
			return this;
		}

		public StyleBuilder withColor(Color color){
			element.color = color;
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
		
		public void display(int seconds){
			Canvas.this.display(seconds);
		}

		public void display(double seconds){
			Canvas.this.display(seconds);
		}

	}
	
	public StyleBuilder addCircle(ScreenLocation screenLocation){		
		CircleElement newElement = new CircleElement();		
		newElement.x = screenLocation.getX() - 10;
		newElement.y = screenLocation.getY() - 10;
		newElement.width = 20;
		newElement.height = 20;			
		return addElement(newElement);
	}
	
	public StyleBuilder addImage(ScreenLocation screenLocation, BufferedImage image){		
		ImageElement newElement = new ImageElement();		
		newElement.x = screenLocation.getX();
		newElement.y = screenLocation.getY();
		newElement.image = image;
		return addElement(newElement);
	}

	public StyleBuilder addBox(ScreenRegion screenRegion){
		Rectangle r = screenRegion.getBounds();
		BoxElement newElement = new BoxElement();		
		newElement.x = r.x;
		newElement.y = r.y;
		newElement.width = r.width;
		newElement.height = r.height;			
		return addElement(newElement);
	}
	
	public StyleBuilder addLabel(ScreenRegion screenRegion, String labelText){
		return addLabel(screenRegion.getCenter(), labelText);
	}

	public StyleBuilder addLabel(ScreenLocation screenLocation, String labelText){
		LabelElement newElement = new LabelElement();
		newElement.text = labelText;
		newElement.x = screenLocation.getX();
		newElement.y = screenLocation.getY();
		return addElement(newElement);
	}

	private StyleBuilder addElement(Element element){
		getElements().add(element);
		return new StyleBuilder(element);
	}


	public Canvas clear() {
		getElements().clear();
		return this;
	}

	
	abstract public void display(int seconds);
	abstract public void display(double seconds);
	abstract public BufferedImage createImage();

	protected List<Element> getElements() {
		return elements;
	}

}
