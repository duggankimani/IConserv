package com.wira.pmgt.shared.model;

public class Objective implements Listable{
		String objRef;
		String objName;
		
		public String getObjName() {
			return objName;
		}
		
		public void setObjName(String objName) {
			this.objName = objName;
		}
		
		public String getObjRef() {
			return objRef;
		}
		
		public void setObjRef(String objRef) {
			this.objRef = objRef;
		}

		@Override
		public String getName() {
			return objName;
		}

		@Override
		public String getDisplayName() {
			return objRef;
		}
}
