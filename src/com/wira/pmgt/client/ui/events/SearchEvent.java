package com.wira.pmgt.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.wira.pmgt.shared.model.SearchFilter;

public class SearchEvent extends GwtEvent<SearchEvent.SearchHandler> {

	public static Type<SearchHandler> TYPE = new Type<SearchHandler>();
	private SearchFilter filter;

	public interface SearchHandler extends EventHandler {
		void onSearch(SearchEvent event);
	}

	public SearchEvent(SearchFilter filter) {
		this.filter = filter;
	}

	public SearchFilter getFilter() {
		return filter;
	}

	@Override
	protected void dispatch(SearchHandler handler) {
		handler.onSearch(this);
	}

	@Override
	public Type<SearchHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SearchHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, SearchFilter filter) {
		source.fireEvent(new SearchEvent(filter));
	}
}
