package com.wira.pmgt.server.dao;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.wira.pmgt.server.dao.model.ADValue;
import com.wira.pmgt.shared.model.settings.SETTINGNAME;

public class SettingsDaoImpl extends BaseDaoImpl {

	public SettingsDaoImpl(EntityManager em) {
		super(em);
	}
	
	public void saveSettings(SETTINGNAME name, ADValue value){
		deleteName(name);
		value.setSettingname(name);
		save(value);
	}

	private void deleteName(SETTINGNAME name) {
		assert name!=null;
		String sql = "delete from advalue where settingName=?";
		Query query = em.createNativeQuery(sql).setParameter(1, name.name());
		query.executeUpdate();
	}
	
	public List<ADValue> getSettingValues(List<SETTINGNAME> names){
		String sql = "FROM ADValue where isActive=1 and settingName is not null";
		
		boolean hasNames= names!=null && !names.isEmpty();
		if(hasNames){
			sql = sql.concat(" and settingName in (:names)");
		}
		
		Query query = em.createQuery(sql);
		
		if(hasNames){
			query.setParameter("names", EnumSet.copyOf(names));
		}
		
		List<ADValue> values = new ArrayList<>();
		try{
			values = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return values;
	}

}
