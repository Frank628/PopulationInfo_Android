package com.jinchao.population.dbentity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

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

	public String getRealId() {
		return realId;
	}

	public void setRealId(String realId) {
		this.realId = realId;
	}

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

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getMAC_id() {
		return MAC_id;
	}

	public void setMAC_id(String mAC_id) {
		MAC_id = mAC_id;
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

	public String getCollect_datetime() {
		return collect_datetime;
	}
	public String getIsReal() {
		return isReal;
	}

	public void setIsReal(String isReal) {
		this.isReal = isReal;
	}
	public void setCollect_datetime(String collect_datetime) {
		this.collect_datetime = collect_datetime;
	}

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
			String shouJiChuanHao, String beiYong,String collect_datetime) {
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
				  String shouJiChuanHao, String beiYong,String collect_datetime,String isReal,String realId) {
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
		this.isReal=isReal;
		this.realId=realId;
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
			String homecode) {
		super();
		this.name = name;
		this.collect_datetime = collect_datetime;
		this.cardno = cardno;
		this.module = module;
		this.uuid = uuid;
		this.actiontype = actiontype;
		this.user_id = user_id;
		this.homecode = homecode;
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
	
	
	
}
