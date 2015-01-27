package me.senwang.newpdademo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wang Sen on 1/27/2015.
 * Last modified:
 * By:
 */
public class WdtWarehouse {
	@SerializedName("warehouse_type") public int warehouseType;
	@SerializedName("warehouse_no") public String warehouseNo;
	public String name;
	public String province;
	public String city;
	public String district;
	public String address;
	public String contact;
	public String zip;
	public String mobile;
	@SerializedName("telno") public String telNo;
	@SerializedName("is_defect") public int isDefect; //是否残次品库
}
