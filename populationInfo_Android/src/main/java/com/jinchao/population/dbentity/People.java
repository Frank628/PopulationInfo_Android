package com.jinchao.population.dbentity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 *
 */
@Table(name="people")
public class People implements Serializable{
	private static final long serialVersionUID = -1021423303885986152L;

	public People() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	public int id;
	
	@Column(column="name")
	public String name="";

	@Column(column="formername")
	public String formername="";

	@Column(column="collect_datetime")
	public String collect_datetime="";
	
	@Column(column="cardno")
	public String cardno="";
	
	@Column(column="people")//名族
	public String people="";
	
	@Column(column="sex")//性别
	public String sex="";
	
	@Column(column="birthday")
	public String birthday="";
	
	@Column(column="address")
	public String address="";
	
	@Column(column="picture")
	public String picture="";
	
	@Column(column="hjdxz")
	public String hjdxz="";
	
	@Column(column="module")
	public String module="";
	
	@Column(column="uuid")
	public String uuid="";
	
	@Column(column="actiontype")
	public String actiontype="";
	
	@Column(column="card_type")
	public String card_type="";
	
	
	@Column(column="user_id")
	public String user_id="";
	
	@Column(column="is_jzz")
	public String is_jzz="";
	
	@Column(column="height")//身高
	public String height="";
	
	@Column(column="education")
	public String education="";
	
	@Column(column="marriage")
	public String marriage="";
	
	@Column(column="landscape")//政治面貌
	public String landscape="";
	
	@Column(column="homecode")
	public String homecode="";
	
	@Column(column="ResidentAddress")
	public String ResidentAddress="";
	
	@Column(column="Roomcode")
	public String Roomcode="";
	
	@Column(column="RoomType")
	public String RoomType="";
	
	@Column(column="ResidentReason")
	public String ResidentReason="";
	
	@Column(column="SeviceAddress")
	public String SeviceAddress="";
	
	@Column(column="Note")
	public String Note="";
	
	@Column(column="PositionName")
	public String PositionName="";
	
	@Column(column="IsInsured")
	public String IsInsured="";
	
	@Column(column="ChanBaoShiJian")
	public String ChanBaoShiJian="";
	
	@Column(column="Phone")
	public String Phone="";
	
	@Column(column="FuBingYi")
	public String FuBingYi="";
	
	@Column(column="JuZhuLeiBie")
	public String JuZhuLeiBie="";
	
	@Column(column="JuZhuFangShi")
	public String JuZhuFangShi="";
	
	@Column(column="FangDongGuanXi")
	public String FangDongGuanXi="";
	
	@Column(column="FuQiTongXing")
	public String FuQiTongXing="";
	
	@Column(column="ShengYuQingKuang")
	public String ShengYuQingKuang="";
	
	@Column(column="childNum")
	public String childNum="";
	
	@Column(column="gravidity")
	public String gravidity="";
	
	@Column(column="BirthCtl")
	public String BirthCtl="";
	
	@Column(column="ShiFouLingZheng")
	public String ShiFouLingZheng="";
	
	@Column(column="carType")
	public String carType="";
	
	@Column(column="carNo")
	public String carNo="";
	
	@Column(column="Rent")
	public String Rent="";
	
	@Column(column="QQ")
	public String QQ="";
	
	@Column(column="MAC_id")
	public String MAC_id="";
	
	@Column(column="JieYuCuoShi")
	public String JieYuCuoShi="";
	
	@Column(column="sq_name")
	public String sq_name="";
	
	@Column(column="JuZhuShiJian")
	public String JuZhuShiJian="";
	
	@Column(column="LingQuFangShi")
	public String LingQuFangShi="";
	
	@Column(column="ShenQingLeiBie")
	public String ShenQingLeiBie="";
	
	@Column(column="ShouJiXingHao")
	public String ShouJiXingHao="";
	
	@Column(column="ShouJiChuanHao")
	public String ShouJiChuanHao="";
	
	@Column(column="BeiYong")
	public String BeiYong="";
	@Column(column="isReal")
	public String isReal="";
	@Column(column="realId")
	public String realId="";

	//新增字段
	@Column(column="MSN")
	public String MSN="";

	@Column(column="Email")
	public String Email="";

	@Column(column="cbqk")
	public String cbqk="";
	/**
	 * 地址补充
	 */
	@Column(column="czwxz")
	public String czwxz="";

	/**
	 * 来苏日期
	 */
	@Column(column="lsrq")
	public String lsrq="";

	/**
	 * 登记日期
	 */
	@Column(column="djrq")
	public String djrq="";

	/**
	 * 房主姓名
	 */
	@Column(column="fzxm")
	public String fzxm="";

	/**
	 * 房主电话
	 */
	@Column(column="fzdh")
	public String fzdh="";
	/**
	 * 房主身份证
	 */
	@Column(column="fzsfz")
	public String fzsfz="";
	/**
	 * 单位联系电话
	 */
	@Column(column="dwlxdh")
	public String dwlxdh="";
	/**
	 * 职业名称
	 */
	@Column(column="zymc")
	public String zymc="";
	/**
	 * 劳动合同签订情况
	 */
	@Column(column="ldhtqj")
	public String ldhtqj="";
	/**
	 * 社保卡编号
	 */
	@Column(column="sbbh")
	public String sbbh="";
	/**
	 * 就业日期
	 */
	@Column(column="jyrq")
	public String jyrq="";
	/**
	 * 健康证编号
	 */
	@Column(column="jkzbh")
	public String jkzbh="";
	/**
	 * 单位负责人
	 */
	@Column(column="dwfzr")
	public String dwfzr="";
	/**
	 * 是否采取节育措施
	 */
	@Column(column="sfjy")
	public String sfjy="";
	/**
	 * 婚育证明种类
	 */
	@Column(column="hyzmzl")
	public String hyzmzl="";
	/**
	 * 婚育证明编号
	 */
	@Column(column="hyzmbh")
	public String hyzmbh="";
	/**
	 * 婚育证明签发日期
	 */
	@Column(column="hyqfrq")
	public String hyqfrq="";
	/**
	 * 近期是否接种疫苗***
	 */
	@Column(column="jqjzym")
	public String jqjzym="";
	/**
	 * 结婚登记日期
	 */
	@Column(column="jhrq")
	public String jhrq="";
	/**
	 * 预防接种证号
	 */
	@Column(column="yfjzzh")
	public String yfjzzh="";
	/**
	 * 独生子女证
	 */
	@Column(column="dsznz")
	public String dsznz="";
	/**
	 * 服务卡号
	 */
	@Column(column="fwkh")
	public String fwkh="";
	/**
	 * 避孕措施落实日期
	 */
	@Column(column="bycsrq")
	public String bycsrq="";
	/**
	 * 出租开始日期
	 */
	@Column(column="czqx1")
	public String czqx1="";
	/**
	 * 出租结束日期
	 */
	@Column(column="czqx2")
	public String czqx2="";
	/**
	 * 租金到期日期
	 */
	@Column(column="zjdq")
	public String zjdq="";
	/**
	 * 备注2
	 */
	@Column(column="beizhu2")
	public String beizhu2 ="";
	/**
	 * 当前设备串号
	 */
	@Column(column="sbsbh")
	public String sbsbh ="";
	/**
	 * 成员关系1
	 */
	@Column(column="xdr1")
	public String xdr1 ="";
	/**
	 * 成员姓名1
	 */
	@Column(column="xdxm1")
	public String xdxm1 ="";
	/**
	 * 成员性别1
	 */
	@Column(column="xdxb1")
	public String xdxb1 ="";
	/**
	 * 成员生日1
	 */
	@Column(column="xdrq1")
	public String xdrq1 ="";
	/**
	 * 成员身份证1
	 */
	@Column(column="xdsfz1")
	public String xdsfz1 ="";
	/**
	 * 成员关系2
	 */
	@Column(column="xdr2")
	public String xdr2="";
	/**
	 * 成员姓名2
	 */
	@Column(column="xdxm2")
	public String xdxm2="";
	/**
	 * 成员性别2
	 */
	@Column(column="xdxb2")
	public String xdxb2="";
	/**
	 * 成员生日2
	 */
	@Column(column="xdrq2")
	public String xdrq2 ="";
	/**
	 * 成员身份证2
	 */
	@Column(column="xdsfz2")
	public String xdsfz2="";

	/**
	 * 成员关系3
	 */
	@Column(column="xdr3")
	public String xdr3="";
	/**
	 * 成员姓名3
	 */
	@Column(column="xdxm3")
	public String xdxm3="";
	/**
	 * 成员性别3
	 */
	@Column(column="xdxb3")
	public String xdxb3="";
	/**
	 * 成员生日3
	 */
	@Column(column="xdrq3")
	public String xdrq3 ="";
	/**
	 * 成员身份证3
	 */
	@Column(column="xdsfz3")
	public String xdsfz3="";
	/**
	 * 成员关系4
	 */
	@Column(column="xdr4")
	public String xdr4="";
	/**
	 * 成员姓名4
	 */
	@Column(column="xdxm4")
	public String xdxm4="";
	/**
	 * 成员性别4
	 */
	@Column(column="xdxb4")
	public String xdxb4="";
	/**
	 * 成员生日4
	 */
	@Column(column="xdrq4")
	public String xdrq4 ="";
	/**
	 * 成员身份证4
	 */
	@Column(column="xdsfz4")
	public String xdsfz4="";

	/**
	 * 关系人1
	 */
	@Column(column="gxr1")
	public String gxr1="";
	/**
	 * 关系人1姓名
	 */
	@Column(column="gxxm1")
	public String gxxm1="";
	/**
	 * 关系人1性别
	 */
	@Column(column="gxxb1")
	public String gxxb1="";
	/**
	 * 关系人1生日
	 */
	@Column(column="gxrq1")
	public String gxrq1="";
	/**
	 * 关系人1身份证
	 */
	@Column(column="gxsfz1")
	public String gxsfz1="";
	/**
	 * 关系人1有无接种
	 */
	@Column(column="gxrjzk1")
	public String gxrjzk1="";

	/**
	 * 关系人2
	 */
	@Column(column="gxr2")
	public String gxr2="";
	/**
	 * 关系人2姓名
	 */
	@Column(column="gxxm2")
	public String gxxm2="";
	/**
	 * 关系人2性别
	 */
	@Column(column="gxxb2")
	public String gxxb2="";
	/**
	 * 关系人2生日
	 */
	@Column(column="gxrq2")
	public String gxrq2="";
	/**
	 * 关系人2身份证
	 */
	@Column(column="gxsfz2")
	public String gxsfz2="";
	/**
	 * 关系人2有无接种
	 */
	@Column(column="gxrjzk2")
	public String gxrjzk2="";

	/**
	 * 关系人3
	 */
	@Column(column="gxr3")
	public String gxr3="";
	/**
	 * 关系人3姓名
	 */
	@Column(column="gxxm3")
	public String gxxm3="";
	/**
	 * 关系人3性别
	 */
	@Column(column="gxxb3")
	public String gxxb3="";
	/**
	 * 关系人3生日
	 */
	@Column(column="gxrq3")
	public String gxrq3="";
	/**
	 * 关系人3身份证
	 */
	@Column(column="gxsfz3")
	public String gxsfz3="";
	/**
	 * 关系人3有无接种
	 */
	@Column(column="gxrjzk3")
	public String gxrjzk3="";

	/**
	 * 关系人4
	 */
	@Column(column="gxr4")
	public String gxr4="";
	/**
	 * 关系人4姓名
	 */
	@Column(column="gxxm4")
	public String gxxm4="";
	/**
	 * 关系人4性别
	 */
	@Column(column="gxxb4")
	public String gxxb4="";
	/**
	 * 关系人4生日
	 */
	@Column(column="gxrq4")
	public String gxrq4="";
	/**
	 * 关系人4身份证
	 */
	@Column(column="gxsfz4")
	public String gxsfz4="";
	/**
	 * 关系人4有无接种
	 */
	@Column(column="gxrjzk4")
	public String gxrjzk4="";
	public People(String name, String cardno, String people, String sex,
				  String birthday, String address) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
	}

	public People(String cardno, String address, String module, String user_id, String collect_datetime) {
		this.cardno = cardno;
		this.address = address;
		this.module = module;
		this.user_id = user_id;
		this.collect_datetime = collect_datetime;
	}
	public People(String name, String cardno, String people, String sex,
				  String birthday, String address, String picture, String hjdxz,
				  String card_type, String user_id, String sq_name) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.picture = picture;
		this.hjdxz = hjdxz;
		this.card_type = card_type;
		this.user_id = user_id;
		this.sq_name = sq_name;
	}
	public People(String name, String cardno, String people, String sex,
				  String birthday, String address, String picture, String hjdxz,
				  String card_type, String user_id, String sq_name,String formername) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.picture = picture;
		this.hjdxz = hjdxz;
		this.card_type = card_type;
		this.user_id = user_id;
		this.sq_name = sq_name;
		this.formername=formername;
	}

	//登记新
	public People(String name, String cardno, String people, String sex,
				  String birthday, String address, String picture, String hjdxz,
				  String module, String uuid, String actiontype, String card_type,
				  String user_id, String is_jzz, String height, String education,
				  String marriage, String landscape, String homecode,
				  String residentAddress, String roomcode, String roomType,
				  String residentReason, String seviceAddress, String note,
				  String positionName, String isInsured, String chanBaoShiJian,
				  String phone, String fuBingYi, String juZhuLeiBie,
				  String juZhuFangShi, String fangDongGuanXi, String fuQiTongXing,
				  String shengYuQingKuang, String childNum, String gravidity,
				  String birthCtl, String shiFouLingZheng, String carType,
				  String carNo, String rent, String qQ, String mAC_id,
				  String jieYuCuoShi, String sq_name, String juZhuShiJian,
				  String lingQuFangShi, String shenQingLeiBie, String shouJiXingHao,
				  String shouJiChuanHao, String beiYong,String collect_datetime,String MSN,
				  String Email,String czwxz,String lsrq,String djrq) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.picture = picture;
		this.hjdxz = hjdxz;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.card_type = card_type;
		this.user_id = user_id;
		this.is_jzz = is_jzz;
		this.height = height;
		this.education = education;
		this.marriage = marriage;
		this.landscape = landscape;
		this.homecode = homecode;
		this.ResidentAddress = residentAddress;
		this.Roomcode = roomcode;
		this.RoomType = roomType;
		this.ResidentReason = residentReason;
		this.SeviceAddress = seviceAddress;
		this.Note = note;
		this.PositionName = positionName;
		this.IsInsured = isInsured;
		this.ChanBaoShiJian = chanBaoShiJian;
		this.Phone = phone;
		this.FuBingYi = fuBingYi;
		this.JuZhuLeiBie = juZhuLeiBie;
		this.JuZhuFangShi = juZhuFangShi;
		this.FangDongGuanXi = fangDongGuanXi;
		this.FuQiTongXing = fuQiTongXing;
		this.ShengYuQingKuang = shengYuQingKuang;
		this.childNum = childNum;
		this.gravidity = gravidity;
		this.BirthCtl = birthCtl;
		this.ShiFouLingZheng = shiFouLingZheng;
		this.carType = carType;
		this.carNo = carNo;
		this.Rent = rent;
		this.QQ = qQ;
		this.MAC_id = mAC_id;
		this.JieYuCuoShi = jieYuCuoShi;
		this.sq_name = sq_name;
		this.JuZhuShiJian = juZhuShiJian;
		this.LingQuFangShi = lingQuFangShi;
		this.ShenQingLeiBie = shenQingLeiBie;
		this.ShouJiXingHao = shouJiXingHao;
		this.ShouJiChuanHao = shouJiChuanHao;
		this.BeiYong = beiYong;
		this.collect_datetime=collect_datetime;
	}
//handle
	public People(String name, String cardno, String people, String sex,
			String birthday, String address, String picture, String hjdxz,
			String module, String uuid, String actiontype, String card_type,
			String user_id, String is_jzz, String height, String education,
			String marriage, String landscape, String homecode,
			String residentAddress, String roomcode, String roomType,
			String residentReason, String seviceAddress, String note,
			String positionName, String isInsured, String chanBaoShiJian,
			String phone, String fuBingYi, String juZhuLeiBie,
			String juZhuFangShi, String fangDongGuanXi, String fuQiTongXing,
			String shengYuQingKuang, String childNum, String gravidity,
			String birthCtl, String shiFouLingZheng, String carType,
			String carNo, String rent, String qQ, String mAC_id,
			String jieYuCuoShi, String sq_name, String juZhuShiJian,
			String lingQuFangShi, String shenQingLeiBie, String shouJiXingHao,
			String shouJiChuanHao, String beiYong,String collect_datetime,

			String MSN,String Email,String czwxz,String lsrq,String djrq,String fzxm,String fzdh,String fzsfz,String dwlxdh,String zymc,String ldhtqj,String sbbh,
				  String jyrq,String jkzbh,String dwfzr,String sfjy,String hyzmzl,String hyzmbh,String jqjzym,String hyqfrq,String jhrq,String yfjzzh,String fwkh,String bycsrq,String czqx1,String czqx2,String
				  zjdq,String beizhu2 ,String cbqk,String xdr1,String xdxm1,String xdxb1,String xdrq1,String xdsfz1,String
				  xdr2,String xdxm2,String xdxb2,String xdrq2,String xdsfz2,String
				  xdr3,String xdxm3,String xdxb3,String xdrq3,String xdsfz3,String
				  xdr4,String xdxm4,String xdxb4,String xdrq4,String xdsfz4,String
				  gxr1,String gxxm1,String gxxb1,String gxrq1,String gxsfz1,String gxrjzk1,String
				  gxr2,String gxxm2,String gxxb2,String gxrq2,String gxsfz2,String gxrjzk2,String
				  gxr3,String gxxm3,String gxxb3,String gxrq3,String gxsfz3,String gxrjzk3,String
				  gxr4,String gxxm4,String gxxb4,String gxrq4,String gxsfz4,String gxrjzk4
				  ) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.picture = picture;
		this.hjdxz = hjdxz;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.card_type = card_type;
		this.user_id = user_id;
		this.is_jzz = is_jzz;
		this.height = height;
		this.education = education;
		this.marriage = marriage;
		this.landscape = landscape;
		this.homecode = homecode;
		this.ResidentAddress = residentAddress;
		this.Roomcode = roomcode;
		this.RoomType = roomType;
		this.ResidentReason = residentReason;
		this.SeviceAddress = seviceAddress;
		this.Note = note;
		this.PositionName = positionName;
		this.IsInsured = isInsured;
		this.ChanBaoShiJian = chanBaoShiJian;
		this.Phone = phone;
		this.FuBingYi = fuBingYi;
		this.JuZhuLeiBie = juZhuLeiBie;
		this.JuZhuFangShi = juZhuFangShi;
		this.FangDongGuanXi = fangDongGuanXi;
		this.FuQiTongXing = fuQiTongXing;
		this.ShengYuQingKuang = shengYuQingKuang;
		this.childNum = childNum;
		this.gravidity = gravidity;
		this.BirthCtl = birthCtl;
		this.ShiFouLingZheng = shiFouLingZheng;
		this.carType = carType;
		this.carNo = carNo;
		this.Rent = rent;
		this.QQ = qQ;
		this.MAC_id = mAC_id;
		this.JieYuCuoShi = jieYuCuoShi;
		this.sq_name = sq_name;
		this.JuZhuShiJian = juZhuShiJian;
		this.LingQuFangShi = lingQuFangShi;
		this.ShenQingLeiBie = shenQingLeiBie;
		this.ShouJiXingHao = shouJiXingHao;
		this.ShouJiChuanHao = shouJiChuanHao;
		this.BeiYong = beiYong;
		this.collect_datetime=collect_datetime;
		this.cbqk=cbqk;
		this.MSN=MSN   ; this.Email= Email  ; this.czwxz= czwxz  ; this.lsrq= lsrq  ; this.djrq= djrq  ; this.fzxm= fzxm  ; this.fzdh= fzdh  ; this.fzsfz= fzsfz  ; this.dwlxdh= dwlxdh  ; this.zymc= zymc  ; this.ldhtqj= ldhtqj  ; this.sbbh= sbbh  ;
		this.jyrq= jyrq  ; this.jkzbh= jkzbh  ; this.dwfzr=dwfzr   ; this.sfjy= sfjy  ; this.hyzmzl= hyzmzl  ; this.hyzmbh= hyzmbh  ; this.jqjzym= jqjzym  ; this.hyqfrq= hyqfrq  ; this.jhrq= jhrq  ; this.yfjzzh= yfjzzh  ; this.fwkh=  fwkh ; this.bycsrq=bycsrq   ; this.czqx1=  czqx1 ; this.czqx2= czqx2  ;
		this.zjdq=  zjdq ; this.beizhu2 =beizhu2   ;
		this.xdr1=  xdr1 ; this.xdxm1=   xdxm1; this.xdxb1=  xdxb1 ; this.xdrq1=   xdrq1; this.xdsfz1= xdsfz1  ;
		this.xdr2=   xdr2; this.xdxm2= xdxm2  ; this.xdxb2=  xdxb2 ; this.xdrq2= xdrq2  ; this.xdsfz2= xdsfz2  ;
		this.xdr3=  xdr3 ; this.xdxm3=  xdxm3 ; this.xdxb3= xdxb3  ; this.xdrq3= xdrq3  ; this.xdsfz3= xdsfz3  ;
		this.xdr4=  xdr4 ; this.xdxm4=  xdxm4 ; this.xdxb4= xdxb4  ; this.xdrq4= xdrq4  ; this.xdsfz4=xdsfz4   ;
		this.gxr1=  gxr1 ; this.gxxm1= gxxm1  ; this.gxxb1=  gxxb1 ; this.gxrq1= gxrq1  ; this.gxsfz1=  gxsfz1 ; this.gxrjzk1= gxrjzk1  ;
		this.gxr2=  gxr2 ; this.gxxm2=  gxxm2 ; this.gxxb2= gxxb2  ; this.gxrq2= gxrq2  ; this.gxsfz2= gxsfz2  ; this.gxrjzk2= gxrjzk2  ;
		this.gxr3=  gxr3 ; this.gxxm3=   gxxm3; this.gxxb3= gxxb3  ; this.gxrq3= gxrq3  ; this.gxsfz3= gxsfz3  ; this.gxrjzk3= gxrjzk3  ;
		this.gxr4=  gxr4 ; this.gxxm4=  gxxm4 ; this.gxxb4= gxxb4  ; this.gxrq4=  gxrq4 ; this.gxsfz4=  gxsfz4 ; this.gxrjzk4=gxrjzk4;
	}

	//变更
	public People(String name, String cardno, String people, String sex,
				  String birthday, String address, String picture, String hjdxz,
				  String module, String uuid, String actiontype, String card_type,
				  String user_id, String is_jzz, String height, String education,
				  String marriage, String landscape, String homecode,
				  String residentAddress, String roomcode, String roomType,
				  String residentReason, String seviceAddress, String note,
				  String positionName, String isInsured, String chanBaoShiJian,
				  String phone, String fuBingYi, String juZhuLeiBie,
				  String juZhuFangShi, String fangDongGuanXi, String fuQiTongXing,
				  String shengYuQingKuang, String childNum, String gravidity,
				  String birthCtl, String shiFouLingZheng, String carType,
				  String carNo, String rent, String qQ, String mAC_id,
				  String jieYuCuoShi, String sq_name, String juZhuShiJian,
				  String lingQuFangShi, String shenQingLeiBie, String shouJiXingHao,
				  String shouJiChuanHao, String beiYong,String collect_datetime,String isReal,String realId,
				  String MSN,String Email,String czwxz,String lsrq,String djrq,String fzxm,String fzdh,String fzsfz,String dwlxdh,String zymc,String ldhtqj,String sbbh,
				  String jyrq,String jkzbh,String dwfzr,String sfjy,String hyzmzl,String hyzmbh,String jqjzym,String hyqfrq,String jhrq,String yfjzzh,String fwkh,String bycsrq,String czqx1,String czqx2,String
						  zjdq,String beizhu2 ,String cbqk,String xdr1,String xdxm1,String xdxb1,String xdrq1,String xdsfz1,String
						  xdr2,String xdxm2,String xdxb2,String xdrq2,String xdsfz2,String
						  xdr3,String xdxm3,String xdxb3,String xdrq3,String xdsfz3,String
						  xdr4,String xdxm4,String xdxb4,String xdrq4,String xdsfz4,String
						  gxr1,String gxxm1,String gxxb1,String gxrq1,String gxsfz1,String gxrjzk1,String
						  gxr2,String gxxm2,String gxxb2,String gxrq2,String gxsfz2,String gxrjzk2,String
						  gxr3,String gxxm3,String gxxb3,String gxrq3,String gxsfz3,String gxrjzk3,String
						  gxr4,String gxxm4,String gxxb4,String gxrq4,String gxsfz4,String gxrjzk4) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.picture = picture;
		this.hjdxz = hjdxz;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.card_type = card_type;
		this.user_id = user_id;
		this.is_jzz = is_jzz;
		this.height = height;
		this.education = education;
		this.marriage = marriage;
		this.landscape = landscape;
		this.homecode = homecode;
		this.ResidentAddress = residentAddress;
		this.Roomcode = roomcode;
		this.RoomType = roomType;
		this.ResidentReason = residentReason;
		this.SeviceAddress = seviceAddress;
		this.Note = note;
		this.PositionName = positionName;
		this.IsInsured = isInsured;
		this.ChanBaoShiJian = chanBaoShiJian;
		this.Phone = phone;
		this.FuBingYi = fuBingYi;
		this.JuZhuLeiBie = juZhuLeiBie;
		this.JuZhuFangShi = juZhuFangShi;
		this.FangDongGuanXi = fangDongGuanXi;
		this.FuQiTongXing = fuQiTongXing;
		this.ShengYuQingKuang = shengYuQingKuang;
		this.childNum = childNum;
		this.gravidity = gravidity;
		this.BirthCtl = birthCtl;
		this.ShiFouLingZheng = shiFouLingZheng;
		this.carType = carType;
		this.carNo = carNo;
		this.Rent = rent;
		this.QQ = qQ;
		this.MAC_id = mAC_id;
		this.JieYuCuoShi = jieYuCuoShi;
		this.sq_name = sq_name;
		this.JuZhuShiJian = juZhuShiJian;
		this.LingQuFangShi = lingQuFangShi;
		this.ShenQingLeiBie = shenQingLeiBie;
		this.ShouJiXingHao = shouJiXingHao;
		this.ShouJiChuanHao = shouJiChuanHao;
		this.BeiYong = beiYong;
		this.cbqk=cbqk;
		this.collect_datetime=collect_datetime;
		this.isReal=isReal;
		this.realId=realId;
		this.MSN=MSN   ; this.Email= Email  ; this.czwxz= czwxz  ; this.lsrq= lsrq  ; this.djrq= djrq  ; this.fzxm= fzxm  ; this.fzdh= fzdh  ; this.fzsfz= fzsfz  ; this.dwlxdh= dwlxdh  ; this.zymc= zymc  ; this.ldhtqj= ldhtqj  ; this.sbbh= sbbh  ;
		this.jyrq= jyrq  ; this.jkzbh= jkzbh  ; this.dwfzr=dwfzr   ; this.sfjy= sfjy  ; this.hyzmzl= hyzmzl  ; this.hyzmbh= hyzmbh  ; this.jqjzym= jqjzym  ; this.hyqfrq= hyqfrq  ; this.jhrq= jhrq  ; this.yfjzzh= yfjzzh  ; this.fwkh=  fwkh ; this.bycsrq=bycsrq   ; this.czqx1=  czqx1 ; this.czqx2= czqx2  ;
		this.zjdq=  zjdq ; this.beizhu2 =beizhu2   ;
		this.xdr1=  xdr1 ; this.xdxm1=   xdxm1; this.xdxb1=  xdxb1 ; this.xdrq1=   xdrq1; this.xdsfz1= xdsfz1  ;
		this.xdr2=   xdr2; this.xdxm2= xdxm2  ; this.xdxb2=  xdxb2 ; this.xdrq2= xdrq2  ; this.xdsfz2= xdsfz2  ;
		this.xdr3=  xdr3 ; this.xdxm3=  xdxm3 ; this.xdxb3= xdxb3  ; this.xdrq3= xdrq3  ; this.xdsfz3= xdsfz3  ;
		this.xdr4=  xdr4 ; this.xdxm4=  xdxm4 ; this.xdxb4= xdxb4  ; this.xdrq4= xdrq4  ; this.xdsfz4=xdsfz4   ;
		this.gxr1=  gxr1 ; this.gxxm1= gxxm1  ; this.gxxb1=  gxxb1 ; this.gxrq1= gxrq1  ; this.gxsfz1=  gxsfz1 ; this.gxrjzk1= gxrjzk1  ;
		this.gxr2=  gxr2 ; this.gxxm2=  gxxm2 ; this.gxxb2= gxxb2  ; this.gxrq2= gxrq2  ; this.gxsfz2= gxsfz2  ; this.gxrjzk2= gxrjzk2  ;
		this.gxr3=  gxr3 ; this.gxxm3=   gxxm3; this.gxxb3= gxxb3  ; this.gxrq3= gxrq3  ; this.gxsfz3= gxsfz3  ; this.gxrjzk3= gxrjzk3  ;
		this.gxr4=  gxr4 ; this.gxxm4=  gxxm4 ; this.gxxb4= gxxb4  ; this.gxrq4=  gxrq4 ; this.gxsfz4=  gxsfz4 ; this.gxrjzk4=gxrjzk4;
	}

	public People(int id, String name, String cardno, String people,
			String sex, String birthday, String address, String picture,
			String hjdxz, String module, String uuid, String actiontype,
			String card_type, String user_id, String is_jzz, String height,
			String education, String marriage, String landscape,
			String homecode, String residentAddress, String roomcode,
			String roomType, String residentReason, String seviceAddress,
			String note, String positionName, String isInsured,
			String chanBaoShiJian, String phone, String fuBingYi,
			String juZhuLeiBie, String juZhuFangShi, String fangDongGuanXi,
			String fuQiTongXing, String shengYuQingKuang, String childNum,
			String gravidity, String birthCtl, String shiFouLingZheng,
			String carType, String carNo, String rent, String qQ,
			String mAC_id, String jieYuCuoShi, String sq_name,
			String juZhuShiJian, String lingQuFangShi, String shenQingLeiBie,
			String shouJiXingHao, String shouJiChuanHao, String beiYong,String collect_datetime) {
		super();
		this.id = id;
		this.name = name;
		this.cardno = cardno;
		this.people = people;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.picture = picture;
		this.hjdxz = hjdxz;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.card_type = card_type;
		this.user_id = user_id;
		this.is_jzz = is_jzz;
		this.height = height;
		this.education = education;
		this.marriage = marriage;
		this.landscape = landscape;
		this.homecode = homecode;
		ResidentAddress = residentAddress;
		Roomcode = roomcode;
		RoomType = roomType;
		ResidentReason = residentReason;
		SeviceAddress = seviceAddress;
		Note = note;
		PositionName = positionName;
		IsInsured = isInsured;
		ChanBaoShiJian = chanBaoShiJian;
		Phone = phone;
		FuBingYi = fuBingYi;
		JuZhuLeiBie = juZhuLeiBie;
		JuZhuFangShi = juZhuFangShi;
		FangDongGuanXi = fangDongGuanXi;
		FuQiTongXing = fuQiTongXing;
		ShengYuQingKuang = shengYuQingKuang;
		this.childNum = childNum;
		this.gravidity = gravidity;
		BirthCtl = birthCtl;
		ShiFouLingZheng = shiFouLingZheng;
		this.carType = carType;
		this.carNo = carNo;
		Rent = rent;
		QQ = qQ;
		MAC_id = mAC_id;
		JieYuCuoShi = jieYuCuoShi;
		this.sq_name = sq_name;
		JuZhuShiJian = juZhuShiJian;
		LingQuFangShi = lingQuFangShi;
		ShenQingLeiBie = shenQingLeiBie;
		ShouJiXingHao = shouJiXingHao;
		ShouJiChuanHao = shouJiChuanHao;
		BeiYong = beiYong;
		this.collect_datetime=collect_datetime;
	}

	public People(String name, String cardno, String hjdxz, String module,
			String uuid, String actiontype, String card_type, String user_id,
			String is_jzz, String homecode, String residentAddress,
			String roomcode, String sq_name,String collect_datetime,String realId) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.hjdxz = hjdxz;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.card_type = card_type;
		this.user_id = user_id;
		this.is_jzz = is_jzz;
		this.homecode = homecode;
		this.ResidentAddress = residentAddress;
		this.Roomcode = roomcode;
		this.sq_name = sq_name;
		this.collect_datetime=collect_datetime;
		this.realId=realId;
	}
	public People(String name, String cardno, String hjdxz, String module,
				  String uuid, String actiontype, String card_type, String user_id,
				  String is_jzz, String homecode, String residentAddress,
				  String roomcode, String sq_name,String collect_datetime) {
		super();
		this.name = name;
		this.cardno = cardno;
		this.hjdxz = hjdxz;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.card_type = card_type;
		this.user_id = user_id;
		this.is_jzz = is_jzz;
		this.homecode = homecode;
		this.ResidentAddress = residentAddress;
		this.Roomcode = roomcode;
		this.sq_name = sq_name;
		this.collect_datetime=collect_datetime;
	}
	public People(String name, String collect_datetime, String cardno,
			String module, String uuid, String actiontype, String user_id,
			String homecode,String ResidentAddress) {
		super();
		this.name = name;
		this.collect_datetime = collect_datetime;
		this.cardno = cardno;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.user_id = user_id;
		this.homecode = homecode;
		this.ResidentAddress=ResidentAddress;
	}

	public People(String collect_datetime, String cardno, String picture,
			String module, String uuid, String actiontype, String user_id) {
		super();
		this.collect_datetime = collect_datetime;
		this.cardno = cardno;
		this.picture = picture;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.user_id = user_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollect_datetime() {
		return collect_datetime;
	}

	public void setCollect_datetime(String collect_datetime) {
		this.collect_datetime = collect_datetime;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getHjdxz() {
		return hjdxz;
	}

	public void setHjdxz(String hjdxz) {
		this.hjdxz = hjdxz;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getIs_jzz() {
		return is_jzz;
	}

	public void setIs_jzz(String is_jzz) {
		this.is_jzz = is_jzz;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getLandscape() {
		return landscape;
	}

	public void setLandscape(String landscape) {
		this.landscape = landscape;
	}

	public String getHomecode() {
		return homecode;
	}

	public void setHomecode(String homecode) {
		this.homecode = homecode;
	}

	public String getResidentAddress() {
		return ResidentAddress;
	}

	public void setResidentAddress(String residentAddress) {
		ResidentAddress = residentAddress;
	}

	public String getRoomcode() {
		return Roomcode;
	}

	public void setRoomcode(String roomcode) {
		Roomcode = roomcode;
	}

	public String getRoomType() {
		return RoomType;
	}

	public void setRoomType(String roomType) {
		RoomType = roomType;
	}

	public String getResidentReason() {
		return ResidentReason;
	}

	public void setResidentReason(String residentReason) {
		ResidentReason = residentReason;
	}

	public String getSeviceAddress() {
		return SeviceAddress;
	}

	public void setSeviceAddress(String seviceAddress) {
		SeviceAddress = seviceAddress;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getPositionName() {
		return PositionName;
	}

	public void setPositionName(String positionName) {
		PositionName = positionName;
	}

	public String getIsInsured() {
		return IsInsured;
	}

	public void setIsInsured(String isInsured) {
		IsInsured = isInsured;
	}

	public String getChanBaoShiJian() {
		return ChanBaoShiJian;
	}

	public void setChanBaoShiJian(String chanBaoShiJian) {
		ChanBaoShiJian = chanBaoShiJian;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getFuBingYi() {
		return FuBingYi;
	}

	public void setFuBingYi(String fuBingYi) {
		FuBingYi = fuBingYi;
	}

	public String getJuZhuLeiBie() {
		return JuZhuLeiBie;
	}

	public void setJuZhuLeiBie(String juZhuLeiBie) {
		JuZhuLeiBie = juZhuLeiBie;
	}

	public String getJuZhuFangShi() {
		return JuZhuFangShi;
	}

	public void setJuZhuFangShi(String juZhuFangShi) {
		JuZhuFangShi = juZhuFangShi;
	}

	public String getFangDongGuanXi() {
		return FangDongGuanXi;
	}

	public void setFangDongGuanXi(String fangDongGuanXi) {
		FangDongGuanXi = fangDongGuanXi;
	}

	public String getFuQiTongXing() {
		return FuQiTongXing;
	}

	public void setFuQiTongXing(String fuQiTongXing) {
		FuQiTongXing = fuQiTongXing;
	}

	public String getShengYuQingKuang() {
		return ShengYuQingKuang;
	}

	public void setShengYuQingKuang(String shengYuQingKuang) {
		ShengYuQingKuang = shengYuQingKuang;
	}

	public String getChildNum() {
		return childNum;
	}

	public void setChildNum(String childNum) {
		this.childNum = childNum;
	}

	public String getGravidity() {
		return gravidity;
	}

	public void setGravidity(String gravidity) {
		this.gravidity = gravidity;
	}

	public String getBirthCtl() {
		return BirthCtl;
	}

	public void setBirthCtl(String birthCtl) {
		BirthCtl = birthCtl;
	}

	public String getShiFouLingZheng() {
		return ShiFouLingZheng;
	}

	public void setShiFouLingZheng(String shiFouLingZheng) {
		ShiFouLingZheng = shiFouLingZheng;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getRent() {
		return Rent;
	}

	public void setRent(String rent) {
		Rent = rent;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String QQ) {
		this.QQ = QQ;
	}

	public String getMAC_id() {
		return MAC_id;
	}

	public void setMAC_id(String MAC_id) {
		this.MAC_id = MAC_id;
	}

	public String getJieYuCuoShi() {
		return JieYuCuoShi;
	}

	public void setJieYuCuoShi(String jieYuCuoShi) {
		JieYuCuoShi = jieYuCuoShi;
	}

	public String getSq_name() {
		return sq_name;
	}

	public void setSq_name(String sq_name) {
		this.sq_name = sq_name;
	}

	public String getJuZhuShiJian() {
		return JuZhuShiJian;
	}

	public void setJuZhuShiJian(String juZhuShiJian) {
		JuZhuShiJian = juZhuShiJian;
	}

	public String getLingQuFangShi() {
		return LingQuFangShi;
	}

	public void setLingQuFangShi(String lingQuFangShi) {
		LingQuFangShi = lingQuFangShi;
	}

	public String getShenQingLeiBie() {
		return ShenQingLeiBie;
	}

	public void setShenQingLeiBie(String shenQingLeiBie) {
		ShenQingLeiBie = shenQingLeiBie;
	}

	public String getShouJiXingHao() {
		return ShouJiXingHao;
	}

	public void setShouJiXingHao(String shouJiXingHao) {
		ShouJiXingHao = shouJiXingHao;
	}

	public String getShouJiChuanHao() {
		return ShouJiChuanHao;
	}

	public void setShouJiChuanHao(String shouJiChuanHao) {
		ShouJiChuanHao = shouJiChuanHao;
	}

	public String getBeiYong() {
		return BeiYong;
	}

	public void setBeiYong(String beiYong) {
		BeiYong = beiYong;
	}

	public String getIsReal() {
		return isReal;
	}

	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}

	public String getRealId() {
		return realId;
	}

	public void setRealId(String realId) {
		this.realId = realId;
	}

	public String getMSN() {
		return MSN;
	}

	public void setMSN(String MSN) {
		this.MSN = MSN;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCzwxz() {
		return czwxz;
	}

	public void setCzwxz(String czwxz) {
		this.czwxz = czwxz;
	}

	public String getLsrq() {
		return lsrq;
	}

	public void setLsrq(String lsrq) {
		this.lsrq = lsrq;
	}

	public String getDjrq() {
		return djrq;
	}

	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}

	public String getFzxm() {
		return fzxm;
	}

	public void setFzxm(String fzxm) {
		this.fzxm = fzxm;
	}

	public String getFzdh() {
		return fzdh;
	}

	public void setFzdh(String fzdh) {
		this.fzdh = fzdh;
	}

	public String getFzsfz() {
		return fzsfz;
	}

	public void setFzsfz(String fzsfz) {
		this.fzsfz = fzsfz;
	}

	public String getDwlxdh() {
		return dwlxdh;
	}

	public void setDwlxdh(String dwlxdh) {
		this.dwlxdh = dwlxdh;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getLdhtqj() {
		return ldhtqj;
	}

	public void setLdhtqj(String ldhtqj) {
		this.ldhtqj = ldhtqj;
	}

	public String getSbbh() {
		return sbbh;
	}

	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}

	public String getJyrq() {
		return jyrq;
	}

	public void setJyrq(String jyrq) {
		this.jyrq = jyrq;
	}

	public String getJkzbh() {
		return jkzbh;
	}

	public void setJkzbh(String jkzbh) {
		this.jkzbh = jkzbh;
	}

	public String getDwfzr() {
		return dwfzr;
	}

	public void setDwfzr(String dwfzr) {
		this.dwfzr = dwfzr;
	}

	public String getSfjy() {
		return sfjy;
	}

	public void setSfjy(String sfjy) {
		this.sfjy = sfjy;
	}

	public String getHyzmzl() {
		return hyzmzl;
	}

	public void setHyzmzl(String hyzmzl) {
		this.hyzmzl = hyzmzl;
	}

	public String getHyzmbh() {
		return hyzmbh;
	}

	public void setHyzmbh(String hyzmbh) {
		this.hyzmbh = hyzmbh;
	}

	public String getHyqfrq() {
		return hyqfrq;
	}

	public void setHyqfrq(String hyqfrq) {
		this.hyqfrq = hyqfrq;
	}

	public String getJqjzym() {
		return jqjzym;
	}

	public void setJqjzym(String jqjzym) {
		this.jqjzym = jqjzym;
	}

	public String getJhrq() {
		return jhrq;
	}

	public void setJhrq(String jhrq) {
		this.jhrq = jhrq;
	}

	public String getYfjzzh() {
		return yfjzzh;
	}

	public void setYfjzzh(String yfjzzh) {
		this.yfjzzh = yfjzzh;
	}

	public String getDsznz() {
		return dsznz;
	}

	public void setDsznz(String dsznz) {
		this.dsznz = dsznz;
	}

	public String getFwkh() {
		return fwkh;
	}

	public void setFwkh(String fwkh) {
		this.fwkh = fwkh;
	}

	public String getBycsrq() {
		return bycsrq;
	}

	public void setBycsrq(String bycsrq) {
		this.bycsrq = bycsrq;
	}

	public String getCzqx1() {
		return czqx1;
	}

	public void setCzqx1(String czqx1) {
		this.czqx1 = czqx1;
	}

	public String getCzqx2() {
		return czqx2;
	}

	public void setCzqx2(String czqx2) {
		this.czqx2 = czqx2;
	}

	public String getZjdq() {
		return zjdq;
	}

	public void setZjdq(String zjdq) {
		this.zjdq = zjdq;
	}

	public String getBeizhu2() {
		return beizhu2;
	}

	public void setBeizhu2(String beizhu2) {
		this.beizhu2 = beizhu2;
	}

	public String getSbsbh() {
		return sbsbh;
	}

	public void setSbsbh(String sbsbh) {
		this.sbsbh = sbsbh;
	}

	public String getXdr1() {
		return xdr1;
	}

	public void setXdr1(String xdr1) {
		this.xdr1 = xdr1;
	}

	public String getXdxm1() {
		return xdxm1;
	}

	public void setXdxm1(String xdxm1) {
		this.xdxm1 = xdxm1;
	}

	public String getXdxb1() {
		return xdxb1;
	}

	public void setXdxb1(String xdxb1) {
		this.xdxb1 = xdxb1;
	}

	public String getXdrq1() {
		return xdrq1;
	}

	public void setXdrq1(String xdrq1) {
		this.xdrq1 = xdrq1;
	}

	public String getXdsfz1() {
		return xdsfz1;
	}

	public void setXdsfz1(String xdsfz1) {
		this.xdsfz1 = xdsfz1;
	}

	public String getXdr2() {
		return xdr2;
	}

	public void setXdr2(String xdr2) {
		this.xdr2 = xdr2;
	}

	public String getXdxm2() {
		return xdxm2;
	}

	public void setXdxm2(String xdxm2) {
		this.xdxm2 = xdxm2;
	}

	public String getXdxb2() {
		return xdxb2;
	}

	public void setXdxb2(String xdxb2) {
		this.xdxb2 = xdxb2;
	}

	public String getXdrq2() {
		return xdrq2;
	}

	public void setXdrq2(String xdrq2) {
		this.xdrq2 = xdrq2;
	}

	public String getXdsfz2() {
		return xdsfz2;
	}

	public void setXdsfz2(String xdsfz2) {
		this.xdsfz2 = xdsfz2;
	}

	public String getXdr3() {
		return xdr3;
	}

	public void setXdr3(String xdr3) {
		this.xdr3 = xdr3;
	}

	public String getXdxm3() {
		return xdxm3;
	}

	public void setXdxm3(String xdxm3) {
		this.xdxm3 = xdxm3;
	}

	public String getXdxb3() {
		return xdxb3;
	}

	public void setXdxb3(String xdxb3) {
		this.xdxb3 = xdxb3;
	}

	public String getXdrq3() {
		return xdrq3;
	}

	public void setXdrq3(String xdrq3) {
		this.xdrq3 = xdrq3;
	}

	public String getXdsfz3() {
		return xdsfz3;
	}

	public void setXdsfz3(String xdsfz3) {
		this.xdsfz3 = xdsfz3;
	}

	public String getXdr4() {
		return xdr4;
	}

	public void setXdr4(String xdr4) {
		this.xdr4 = xdr4;
	}

	public String getXdxm4() {
		return xdxm4;
	}

	public void setXdxm4(String xdxm4) {
		this.xdxm4 = xdxm4;
	}

	public String getXdxb4() {
		return xdxb4;
	}

	public void setXdxb4(String xdxb4) {
		this.xdxb4 = xdxb4;
	}

	public String getXdrq4() {
		return xdrq4;
	}

	public void setXdrq4(String xdrq4) {
		this.xdrq4 = xdrq4;
	}

	public String getXdsfz4() {
		return xdsfz4;
	}

	public void setXdsfz4(String xdsfz4) {
		this.xdsfz4 = xdsfz4;
	}

	public String getGxr1() {
		return gxr1;
	}

	public void setGxr1(String gxr1) {
		this.gxr1 = gxr1;
	}

	public String getGxxm1() {
		return gxxm1;
	}

	public void setGxxm1(String gxxm1) {
		this.gxxm1 = gxxm1;
	}

	public String getGxxb1() {
		return gxxb1;
	}

	public void setGxxb1(String gxxb1) {
		this.gxxb1 = gxxb1;
	}

	public String getGxrq1() {
		return gxrq1;
	}

	public void setGxrq1(String gxrq1) {
		this.gxrq1 = gxrq1;
	}

	public String getGxsfz1() {
		return gxsfz1;
	}

	public void setGxsfz1(String gxsfz1) {
		this.gxsfz1 = gxsfz1;
	}

	public String getGxrjzk1() {
		return gxrjzk1;
	}

	public void setGxrjzk1(String gxrjzk1) {
		this.gxrjzk1 = gxrjzk1;
	}

	public String getGxr2() {
		return gxr2;
	}

	public void setGxr2(String gxr2) {
		this.gxr2 = gxr2;
	}

	public String getGxxm2() {
		return gxxm2;
	}

	public void setGxxm2(String gxxm2) {
		this.gxxm2 = gxxm2;
	}

	public String getGxxb2() {
		return gxxb2;
	}

	public void setGxxb2(String gxxb2) {
		this.gxxb2 = gxxb2;
	}

	public String getGxrq2() {
		return gxrq2;
	}

	public void setGxrq2(String gxrq2) {
		this.gxrq2 = gxrq2;
	}

	public String getGxsfz2() {
		return gxsfz2;
	}

	public void setGxsfz2(String gxsfz2) {
		this.gxsfz2 = gxsfz2;
	}

	public String getGxrjzk2() {
		return gxrjzk2;
	}

	public void setGxrjzk2(String gxrjzk2) {
		this.gxrjzk2 = gxrjzk2;
	}

	public String getGxr3() {
		return gxr3;
	}

	public void setGxr3(String gxr3) {
		this.gxr3 = gxr3;
	}

	public String getGxxm3() {
		return gxxm3;
	}

	public void setGxxm3(String gxxm3) {
		this.gxxm3 = gxxm3;
	}

	public String getGxxb3() {
		return gxxb3;
	}

	public void setGxxb3(String gxxb3) {
		this.gxxb3 = gxxb3;
	}

	public String getGxrq3() {
		return gxrq3;
	}

	public void setGxrq3(String gxrq3) {
		this.gxrq3 = gxrq3;
	}

	public String getGxsfz3() {
		return gxsfz3;
	}

	public void setGxsfz3(String gxsfz3) {
		this.gxsfz3 = gxsfz3;
	}

	public String getGxrjzk3() {
		return gxrjzk3;
	}

	public void setGxrjzk3(String gxrjzk3) {
		this.gxrjzk3 = gxrjzk3;
	}

	public String getGxr4() {
		return gxr4;
	}

	public void setGxr4(String gxr4) {
		this.gxr4 = gxr4;
	}

	public String getGxxm4() {
		return gxxm4;
	}

	public void setGxxm4(String gxxm4) {
		this.gxxm4 = gxxm4;
	}

	public String getGxxb4() {
		return gxxb4;
	}

	public void setGxxb4(String gxxb4) {
		this.gxxb4 = gxxb4;
	}

	public String getGxrq4() {
		return gxrq4;
	}

	public void setGxrq4(String gxrq4) {
		this.gxrq4 = gxrq4;
	}

	public String getGxsfz4() {
		return gxsfz4;
	}

	public void setGxsfz4(String gxsfz4) {
		this.gxsfz4 = gxsfz4;
	}

	public String getGxrjzk4() {
		return gxrjzk4;
	}

	public void setGxrjzk4(String gxrjzk4) {
		this.gxrjzk4 = gxrjzk4;
	}

	public String getFormername() {
		return formername;
	}

	public void setFormername(String formername) {
		this.formername = formername;
	}

	public String getCbqk() {
		return cbqk;
	}

	public void setCbqk(String cbqk) {
		this.cbqk = cbqk;
	}
}
