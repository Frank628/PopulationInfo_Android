package com.jinchao.population.config;
import android.os.Environment;

public class Constants {
	public static final String URL="http://222.92.144.66:91/population/";
//	public static final String URL_IP="http://222.92.144.66:91/";
	public static final String DB_NAME="config_population.db";
	public static final String DB_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
//	public static final String DB_PATH= "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/com.jinchao.population/databases/";//内存中
	public static final int KAISA_OFFSET=3;
	public static final String PCSID="PCSID";
	public static final String USERNAME="USERNAME";
	public static final String PASSWORD="PASSWORD";
	public static final String USERID="USERID";
	public static final String SQID="SQID";
	public static final String SQNAME="SQNAME";
	public static final String SQCODE="SQCODE";
	public static final String Accountlist="Accountlist";
	public static final String LOCAL_DB_VERSION="LOCAL_DB_VERSION";
	public static final String LATEST_APP_VERSION="LATEST_APP_VERSION";
	public static final String LATEST_APP_VERSION_FORCE="LATEST_APP_VERSION_FORCE";
	public static final String LATEST_APP_VERSION_URL="LATEST_APP_VERSION_URL";
	public static final String LATEST_APP_VERSION_JSON="LATEST_APP_VERSION_JSON";
	public static final String USER_DB="USER_DB";
	public static final String FORMER_ACCOUNT="FORMER_ACCOUNT";
	public static final String YUJING_TIME_LIMIT="YUJING_TIME_LIMIT";//到期预警接口请求时间
	public static final String YUJING_LIST="YUJING_LIST";//到期预警数据
	public static final String[] DEGREE={"高中","本科","大专","中专","研究生","初中","小学","文盲或半文盲"};
	public static final String[] CHUSHUOLEIXING={"租赁房屋","农村私房","企业内部集宿","社会面集宿","工地现场","居民家中","一般民宅","自购房屋","新村楼房","临时工棚","学校","部队","团体","旅馆","旅店","宾馆","医院","公寓","流动性施工单位","机关","招待所","宗教场所","水上船舶","收容站","疗养院","康复中心","遣送站","个体摊点","商业用房","暂住处不详"};
	public static final String[] ZANZHUSHIYOU={"内部单位合同工","企事业雇佣临时工","建筑民工","装卸运输工","办厂","务农","养殖","其他劳务","单位经商","个体户经商","其他经商","采购、供销","驻地办事机构","其他公务","饮食业","公用事业","咨询服务","仓储业","手工业劳动","修理工匠","保姆","拣、收废品","其他服务","其他经济型","开会、考察、出差","学习、培训","寄读、借读","治病、疗养","探亲、访友","旅游、观光","婚入","离休、退休、退职","刑满释放、解除教养","寄养","盲流","灾荒流入","夫妻投靠","子女投靠父母","父母投靠子女","其他投靠亲友","其他社会型","录(聘)用","随迁学龄前子女","事由不详"};
	public static final String[] CHANYELEIXING={"二产","一产","三产","投资经商","金融业","其他"};
	public static final String[] JUZHUFANGSHI={"单人居住","集体居住","多人居住","家庭居住","混合居住","多家合住","旅馆式居住","其他"};
	public static final String[] FANGDONGGUANXI={"租赁","员工","自有","其他"};
	public static final String[] JIEYUCUOSHI={"结扎","上环","服药","其他","不适用"};
	public static final String[] JIAOTONGGONGJU={"无","电动车","自行车","摩托车","汽车","其他"};
	public static final String[] MENPAIHAODANWEI={"请选择","号","-"};
	public static final String[] FUHAODANWEI={"请选择","号","座"};
	public static final String[] SHIHAODANWEI={"请选择","号","室"};
	public static final String[] LOUDANWEI={"请选择","东楼","西楼","南楼","北楼","前楼","后楼"};
	public static final String[] ZHUANGHAODANWEI={"请选择","幢","栋","排","舍","楼","号楼"};
	public static final String[] DANYUAN={"请选择","一单元","二单元","三单元","四单元","五单元","六单元","七单元","八单元","九单元","十单元","十一单元","十二单元","十三单元","十四单元","十五单元","十六单元","十七单元","十八单元","十九单元","二十单元",
										"一组","二组","三组","四组","五组","六组","七组","八组","九组","十组","十一组","十二组","十三组","十四组","十五组","十六组","十七组","十八组","十九组","二十组",
										"东门","西门","南门","北门","中门","边门","后门","付门","前门","甲单元","乙单元","丙单元","丁单元","戊单元","己单元"+"庚单元","辛单元","壬单元"};
	public static final String[] JUZHUSHIJIAN={"半年到一年","一个月以下","一个月到半年","一年以上"};
	public static final String[] JUZHUSHIJIAN_CODE={"3","1","2","4"};
	public static final String[] LINGQUFANGSHI={"本人领取","单位领取"};
	public static final String[] SHENGQINGLEIBIE={"居住证","短期居住证"};
	public static final String[] SHENGQINGLEIBIE_C0DE={"1","2"};
	public static final String[] ZHUZHAILEIXING={"住宅","商住房","商店","生产企业","商贸企业","其他"};

	public static final String IS_REMEMBER_BLUTOOTH="IS_REMEMBER_BLUTOOTH";
	public static final String DEVICE_WAY="DEVICE_WAY";
	public static final String NET_WAY="NET_WAY";
	public static final String DEFAULT_INDEX="DEFAULT_INDEX";
	public static final String BluetoothSetting_long_time="BluetoothSetting_long_time";
	public static final String REMEMBER_BLUETOOTH="REMEMBER_BLUETOOTH";
	public static final String[] DEVICE_AREA={ "自动", "蓝牙", "OTG", "NFC" };
	public static final String[] NET_AREA={ "自动", "移动", "电信", "联通", "手动" };
	public static final String[] DEFAULT_INDEX_AREA={ "外来人口信息采集", "实有人口调查","巡逻盘查","电动车检查","房屋位置信息录入" };
	public static final String DATABASE_TYPE="DATABASE_TYPE";//0获取失败，1外来人口库，2实有人口库

	public static final String BIANHAO_STR="BIANHAO_STR";
	public static final String ZANZHUDIZHI_STR="ZANZHUDIZHI_STR";
	public static final String SHIHAO_STR="SHIHAO_STR";
	public static final String CHUSUOLEIXING_STR="CHUSUOLEIXING_STR";
	public static final String FUWUCHUSUO_STR="FUWUCHUSUO_STR";
	public static final String DANWEIDIZHI_STR="DANWEIDIZHI_STR";

	public static final String IS_BIANHAO_STR="IS_BIANHAO_STR";
	public static final String IS_ZANZHUDIZHI_STR="IS_ZANZHUDIZHI_STR";
	public static final String IS_SHIHAO_STR="IS_SHIHAO_STR";
	public static final String IS_CHUSUOLEIXING_STR="IS_CHUSUOLEIXING_STR";
	public static final String IS_FUWUCHUSUO_STR="IS_FUWUCHUSUO_STR";
	public static final String IS_DANWEIDIZHI_STR="IS_DANWEIDIZHI_STR";
	public static final  String[] MINZU=new String[]{"汉","蒙古","回","藏","维吾尔","苗","彝","壮","布依","朝鲜","满","侗","瑶","白","土家",
			"哈尼","哈萨克","傣","黎","傈僳","佤","畲","高山","拉祜","水","东乡","纳西","景颇","柯尔克孜",
			"土","达斡尔","仫佬","羌","布朗","撒拉","毛南","仡佬","锡伯","阿昌","普米","塔吉克","怒", "乌孜别克",
			"俄罗斯","鄂温克","崩龙","保安","裕固","京","塔塔尔","独龙","鄂伦春","赫哲","门巴","珞巴","基诺","革家人","穿青人","其他"};
	public static final  String[] MINZU_CODE=new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
			"16","17","18","19","20","21","22","23","24","25","26","27","28","29",
			"30","31","32","33","34","35","36","37","38","39","40","41","42", "43",
			"44","45","46","47","48","49","50","51","52","53","54","55","56","62","61","57"};
	public static final String[] LAODONGHETONGQIANDING={"未签","一年以下","一年及以上"};
	public static final String[] HUYUZHENGMING={"全国婚育证明","本地管理服务卡","无"};
	public static final String[] CANBAOZHONGLEI={"养老","医疗","失业","工伤","生育","农保"};
    public static final String[] GUANXIREN={"配偶","子女","父母","兄弟姐妹","其他"};
	public static final String[] FANGWULEIXING={"搭建房","车库","楼房","平房","企业集宿","社会集宿","私房","公房","新村楼房","写字楼","建筑工地","其他"};
	public static final String[] FANGWULEIXING_CODE={"a","b","c","5","6","7","1","2","3","4","8","9"};
	public static final String[] ZUZHULEIXING={"单人居住","家庭居住","多人居住","混合居住","多家合住","旅馆式居住","其他"};
	public static final String[] ZUZHULEIXING_CODE={"1","2","3","4","5","6","99"};
	public static final String[] FANGWUJIEGOU={"楼房","平房","其他"};
	public static final String[] FANGWUJIEGOU_CODE={"1","2","3"};
	public static final String[] FANGWUYONGTU={"居住","生产","经营","仓储"};
	public static final String[] FANGWUYONGTU_CODE={"1","2","3","4"};
	public static final String[] JUZHULEIXING={"个人出租","单位出租","自购房屋","借住"};
	public static final String[] JUZHULEIXING_CODE={"1","2","3","4"};
	public static final String[] CHENGZUTUJING={"承租途径未知","房主直接出租","承租人转租","中介出租","其他","不适用"};
	public static final String[] CHENGZUTUJING_CODE={"1","2","3","4","5","9"};

	//2018-06-22
	public static final String[] FANGWUDENGJI={"放心户","一般户","重点户"};
	public static final String[] FANGWUDENGJI_CODE={"10","20","30"};

	public static final String[] ZHONGDIANFANGWU={"人均居住面积低于当地标准","居住10人以上且存在安全隐患","违法建筑","违规改变房屋使用性质","不符合有关工程建设强制性标准","住宿与生产、储存、经营混合设置"};
	public static final String[] ZHONGDIANFANGWU_CODE={"21","22","23","24","25","26"};

	public static final String[] ZHONGDIANJUZHURENYUAN={"单身居住","频繁变更租住地点","重点人员租住","无业人员租住","旅馆式租住","情况不明"};
	public static final String[] ZHONGDIANJUZHURENYUAN_CODE={"27","28","31","32","33","35"};

	public static final String[] FANGWUYINHUAN={"治安隐患","消防隐患","住建隐患","其他隐患"};
	public static final String[] FANGWUYINHUAN_CODE={"1","2","3","99"};


	public static final String IS_FROM_REALPOPULATION="IS_FROM_REALPOPULATION";//是否来自实有人口
	public static final String IS_NFC_READER="IS_NFC_READER";//是否是从NFC读取页面跳转来
	public static final String Where_from="Where_from";
	public static final String NFCJSONBEAN="NFCJSONBEAN";
	public static final String HOUSE_INFOR="HOUSE_INFOR";
	public static final String[] JZZLQFS={"快递免费配送+赠送手机卡","自行前往社区领取"};
	public static final String PWD_NTAG216="PASSWORD";//保证pwd8位
}
                          