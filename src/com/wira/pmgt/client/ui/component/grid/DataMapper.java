package com.wira.pmgt.client.ui.component.grid;

import java.util.List;

public abstract class DataMapper {

	public abstract <T> T getData(DataModel model);

	public abstract List<DataModel> getDataModels(List<Object> funding);
}
