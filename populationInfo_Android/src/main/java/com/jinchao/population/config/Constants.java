package com.jinchao.population.config;


import android.os.Environment;

public class Constants {
	public static final String URL="http://222.92.144.66:91/population/";
	public static final String URL_IP="http://222.92.144.66:91/";
	public static final String DB_NAME="config_population.db";
	public static final String DB_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
//	public static final String DB_PATH= "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/com.jinchao.population/databases/";//内存中
	public static final String PCSID="PCSID";
	public static final String USERNAME="USERNAME";
	public static final String PASSWORD="PASSWORD";
	public static final String USERID="USERID";
	public static final String SQID="SQID";
	public static final String SQNAME="SQNAME";
	public static final String Accountlist="Accountlist";
	public static final String LOCAL_DB_VERSION="LOCAL_DB_VERSION";
	public static final String USER_DB="USER_DB";
	public static final String FORMER_ACCOUNT="FORMER_ACCOUNT";
	public static final String[] DEGREE={"高中","本科","大专","中专","研究生","初中","小学","文盲或半文盲"};
	public static final String[] CHUSHUOLEIXING={"租赁房屋","农村私房","企业内部集宿","社会面集宿","建筑工地","居民家中","购买房屋","新村楼房","临时工棚"};
	public static final String[] ZANZHUSHIYOU={"内部单位合同工","建筑民工","其他劳务","个体户经商","学习、培训","离休、退休、退职","事由不详","子女投靠父母"};
	public static final String[] CHANYELEIXING={"二产","一产","三产","其他"};
	public static final String[] JUZHUFANGSHI={"单身居住","集体居住","合伙居住","家庭居住","其他"};
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
	public static final String[] LINGQUFANGSHI={"本人领取","单位领取"};
	public static final String[] SHENGQINGLEIBIE={"居住证","短期居住证"};
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

	public static final String[] LAODONGHETONGQIANDING={"未签","一年以下","一年及以上"};
	public static final String[] HUYUZHENGMING={"全国婚育证明","本地管理服务卡","无"};
	public static final String[] CANBAOZHONGLEI={"养老","医疗","失业","工伤","生育","农保"};
    public static final String[] GUANXIREN={"配偶","子女","父母","兄弟姐妹","其他"};
	public static final String IS_FROM_REALPOPULATION="IS_FROM_REALPOPULATION";//是否来自实有人口
}
                          