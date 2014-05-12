package com.wira.pmgt.server.dao.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.wira.pmgt.server.dao.SettingsDaoImpl;
import com.wira.pmgt.server.dao.model.ADValue;
import com.wira.pmgt.server.db.DB;
import com.wira.pmgt.shared.model.Value;
import com.wira.pmgt.shared.model.settings.SETTINGNAME;
import com.wira.pmgt.shared.model.settings.Setting;

import static com.wira.pmgt.server.dao.helper.FormDaoHelper.*;

public class SettingsDaoHelper {

	Logger log = Logger.getLogger(SettingsDaoHelper.class);
	
	public static boolean save(List<Setting> settings){
		
		SettingsDaoImpl dao = DB.getSettingsDao();
		
		for(Setting setting: settings){			
			ADValue advalue = getValue(null, setting.getValue());
			dao.saveSettings(setting.getName(), advalue);
		}
		return false;
	}
	
	public static List<Setting> getSettings(List<SETTINGNAME> names){
		SettingsDaoImpl dao = DB.getSettingsDao();
		List<Setting> settings = new ArrayList<>();
		
		List<ADValue> values = dao.getSettingValues(names);
		for(ADValue advalue: values){
			Value value = getValue(advalue, advalue.getSettingName().getType());
			Setting setting = new Setting(advalue.getSettingName(), value);			
			settings.add(setting);
		}
		
		return settings;
	}

	public static Setting getSetting(SETTINGNAME settingName) {
		List<Setting> settings = getSettings(Arrays.asList(settingName));
		
		if(settings==null || settings.isEmpty()){
			return null;
		}
		
		return settings.get(0);
	}

	public static Object getSettingValue(SETTINGNAME settingName) {
		
		Setting setting = getSetting(settingName);
		//logger.debug(setting.getName()+" : "+setting.getValue().getValue());		
		return setting==null? null: setting.getValue().getValue();
	}

}
