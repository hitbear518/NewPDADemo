package me.senwang.newpdademo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wang Sen on 1/27/2015.
 * Last modified:
 * By:
 */
public class WdtStock {
	@SerializedName("warehouse_no")
	public String warehouseNo; //仓库编码
	@SerializedName("warehouse_name")
	public String warehouseName; //仓库名称
	@SerializedName("warehouse_type")
	public int warehouseType; //仓库类别
	@SerializedName("spec_no")
	public String specNo; //商家编码
	@SerializedName("goods_name")
	public String goodsName; //货品名称
	@SerializedName("spec_name")
	public String specName; //规格名称
	@SerializedName("spec_code")
	public String specCode; //规格码
	public String barcode; //主条码
	public String weight; //重量
	public String img_url; //图片连接

	@SerializedName("stock_num")
	public int stockNum;        //库存量
	@SerializedName("lock_num")
	public int lockNum;    //活动冻结库存
	@SerializedName("unpay_num")
	public int unpayNum; //未付款库存
	@SerializedName("subscribe_num")
	public int subscribeNum; //预订单库存
	@SerializedName("order_num")
	public int orderNum;        //待审核量
	@SerializedName("sending_num")
	public int sendingNum;        //待发货量
	@SerializedName("purchase_num")
	public int purchaseNum;        //采购在途量
	@SerializedName("transfer_num")
	public int transferNum;        //调拨在途量
	@SerializedName("to_purchase_num")
	public int toPurchaseNum; //
	@SerializedName("purchase_arrive_num")
	public int purchaseArriveNum; //采购到货量
	@SerializedName("cost_price")
	public int costPrice;    //成本价

//	@SerializedName("spec_wh_no")
//	public String specWhNo; //WMS货品编码
//	@SerializedName("wms_sync_stock")
//	public int wmsSyncStock;    //外部WMS同步时库存
//	@SerializedName("wms_preempty_stock")
//	public int wmsPreemptyStock; //外部WMS同步时占用库存
//	@SerializedName("wms_stock_diff")
//	public int wmsStockDiff;    //外部WMS同步时,与系统库存的差
//	@SerializedName("wms_sync_time")
//	public int wmsSyncTime;        //与外部WMS同步时间
}
