package com.wira.pmgt.client.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class StarRating extends Composite {

	private static StarRatingUiBinder uiBinder = GWT
			.create(StarRatingUiBinder.class);

	interface StarRatingUiBinder extends UiBinder<Widget, StarRating> {
	}

	@UiField FlowPanel container;
	String id="rating-input-";
	private int groupId = Random.nextInt(10000);
	
	public StarRating() {
		initWidget(uiBinder.createAndBindUi(this));
		container.setStyleName("rating");
		id = id.concat(groupId+"-");
		setMaxValue(5);
		
	}
	
	public StarRating(int maxValue) {
		this();
		setMaxValue(maxValue);
	}
	
	public void setMaxValue(int maxValue){
		createElements(maxValue);
	}
	
	int rating= 0;
	public void setValue(int rating){
		this.rating = rating;
		if(this.isAttached()){
			DOM.getElementById(id+rating).setAttribute("checked", "checked");
		}
	}

	public int getValue(){
		return 0;
	}
	
	private void createElements(int maxRatingValue){
		
		StringBuffer elementBuffer = new StringBuffer("");
		
		for(int i=maxRatingValue; i>0; i--){		
			elementBuffer.append("<input type=\"radio\" class=\"rating-input\" "+
                "id=\"rating-input-"+groupId+"-"+i+"\" name=\"rating-input-"+groupId+"\"" +
                		" value=\""+i+"\"/> "+
                "<label for=\"rating-input-"+groupId+"-"+i+"\" class=\"rating-star\"></label>");
		}
		
		getElement().setInnerHTML(elementBuffer.toString());
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		DOM.getElementById(id+rating).setAttribute("checked", "checked");
	}

}
