/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.wira.pmgt.shared.model.dashboard;

import java.io.Serializable;

public class Data implements Serializable{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
  private String name;
  private Number data1;
  private Number data2;
  private double data3;
  private double data4;
  private double data5;
  private double data6;
  private double data7;
  private double data8;
  private double data9;
  String title;
  
  public Data() {

  }

  public Data(String name, double data1, double data2, double data3, double data4, double data5, double data6,
      double data7, double data8, double data9) {
    super();
    this.name = name;
    this.title = name;
    this.data1 = data1;
    this.data2 = data2;
    this.data3 = data3;
    this.data4 = data4;
    this.data5 = data5;
    this.data6 = data6;
    this.data7 = data7;
    this.data8 = data8;
    this.data9 = data9;
  }
  
  public Data(String name, Number value, String title){
	  this.name = name;
	  data1 = value;
	  this.title = title;
  }
  
  public Data(String name, Number value){
	  this(name,value,name);
  }


  public Number getData1() {
    return data1;
  }

  public Number getData2() {
    return data2;
  }

  public double getData3() {
    return data3;
  }

  public double getData4() {
    return data4;
  }

  public double getData5() {
    return data5;
  }

  public double getData6() {
    return data6;
  }

  public double getData7() {
    return data7;
  }

  public double getData8() {
    return data8;
  }

  public double getData9() {
    return data9;
  }

  public String getName() {
    return name;
  }

  public void setData1(Number data1) {
    this.data1 = data1;
  }

  public void setData2(Number data2) {
    this.data2 = data2;
  }

  public void setData3(double data3) {
    this.data3 = data3;
  }

  public void setData4(double data4) {
    this.data4 = data4;
  }

  public void setData5(double data5) {
    this.data5 = data5;
  }

  public void setData6(double data6) {
    this.data6 = data6;
  }

  public void setData7(double data7) {
    this.data7 = data7;
  }

  public void setData8(double data8) {
    this.data8 = data8;
  }

  public void setData9(double data9) {
    this.data9 = data9;
  }

  public void setName(String name) {
    this.name = name;
  }

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

}
