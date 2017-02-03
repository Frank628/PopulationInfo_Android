package com.jinchao.population.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jinchao.population.MyApplication;
import com.jinchao.population.R;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.dbentity.HouseAddress;
import com.jinchao.population.dbentity.HouseAddress10;
import com.jinchao.population.dbentity.HouseAddress2;
import com.jinchao.population.dbentity.HouseAddress3;
import com.jinchao.population.dbentity.HouseAddress4;
import com.jinchao.population.dbentity.HouseAddress5;
import com.jinchao.population.dbentity.HouseAddress6;
import com.jinchao.population.dbentity.HouseAddress7;
import com.jinchao.population.dbentity.HouseAddress8;
import com.jinchao.population.dbentity.HouseAddress9;
import com.jinchao.population.dbentity.HouseAddressOldBean10;
import com.jinchao.population.dbentity.HouseAddressOldBean2;
import com.jinchao.population.dbentity.HouseAddressOldBean3;
import com.jinchao.population.dbentity.HouseAddressOldBean4;
import com.jinchao.population.dbentity.HouseAddressOldBean5;
import com.jinchao.population.dbentity.HouseAddressOldBean6;
import com.jinchao.population.dbentity.HouseAddressOldBean7;
import com.jinchao.population.dbentity.HouseAddressOldBean8;
import com.jinchao.population.dbentity.HouseAddressOldBean9;
import com.jinchao.population.dbentity.HouseJLX;
import com.jinchao.population.dbentity.JLX;
import com.jinchao.population.dbentity.HouseAddressOldBean;
import com.jinchao.population.entity.UserBean.AccountOne;
import com.jinchao.population.mainmenu.AddRentalHouseActivity;
import com.jinchao.population.utils.DatabaseUtil;
import com.jinchao.population.utils.DeviceUtils;
import com.jinchao.population.view.Dialog.DialogClickListener;
import com.jinchao.population.widget.wheel.OnWheelChangedListener;
import com.jinchao.population.widget.wheel.WheelView;
import com.jinchao.population.widget.wheel.adapter.ArrayWheelAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class PopHouse extends PopupWindow implements OnWheelChangedListener,OnClickListener{
	public OnHouseEnsureClickListener onEnsureClickListener;
	private View mPopView;
	private ViewFlipper viewfipper;
	private WheelView PCSView,USERView;
	private TextView btn_ensure,btn_cancle;
	private AccountOne accountOne;
	private Activity context1;
	private String[] PSCName,USERName,UserId;
	private Map<String, String[]> UserNameMap=new HashMap<String, String[]>();
	private Map<String, String[]> UserIdMap=new HashMap<String, String[]>();
	private String mCurrentPcs,mCurrentUserName,mCurrentUserId;
	private ListView lv;
	private EditText edt_content;
	private DbUtils dbUtils;
    private int database_tableNo=10;
	private LinearLayout ll_bottom;
	private TextView tv_up;
	public interface OnHouseEnsureClickListener{
		void OnHouseEnSureClick(String bianhao,String dizhi);
	}

	public PopHouse(final Activity context, String[] PSCName, String[] USERName, String[] UserId, Map<String, String[]> UserNameMap, Map<String, String[]> UserIdMap, OnHouseEnsureClickListener onEnsureClickListener1) {
		this.onEnsureClickListener = onEnsureClickListener1;
		this.context1=context;
		this.PSCName=PSCName;
		this.USERName=USERName;
		this.UserId=UserId;
		this.UserNameMap=UserNameMap;
		this.UserIdMap=UserIdMap;
        if (((MyApplication)context.getApplication()).database_tableNo==0){
            database_tableNo= DatabaseUtil.getNullDB(context);
        }else{
            database_tableNo=((MyApplication)context.getApplication()).database_tableNo;
        }
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView=inflater.inflate(R.layout.pop_house, null);
		viewfipper = new ViewFlipper(context);
		viewfipper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		tv_up=(TextView) mPopView.findViewById(R.id.tv_up);
		ll_bottom=(LinearLayout) mPopView.findViewById(R.id.bottom);
		PCSView=(WheelView) mPopView.findViewById(R.id.wv_1);
		USERView=(WheelView) mPopView.findViewById(R.id.wv_2);
		btn_ensure=(TextView) mPopView.findViewById(R.id.btn_ensure);
		btn_cancle=(TextView) mPopView.findViewById(R.id.btn_cancle);
		lv=(ListView) mPopView.findViewById(R.id.lv);
		edt_content=(EditText) mPopView.findViewById(R.id.edt_content);
		btn_cancle.setOnClickListener(this);
		btn_ensure.setOnClickListener(this);
		viewfipper.addView(mPopView);
		viewfipper.setFlipInterval(6000000);
		this.setContentView(viewfipper);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		dbUtils=DeviceUtils.getDbUtils(context);
        if (PSCName!=null){
            PCSView.setViewAdapter(new ArrayWheelAdapter<String>(context1, PSCName));
        }
		PCSView.addChangingListener(PopHouse.this);
		USERView.addChangingListener(PopHouse.this);
		PCSView.setVisibleItems(7);
		USERView.setVisibleItems(7);
        if (PSCName!=null) {
            updateUser();
        }

		edt_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				lv.setAdapter(null);
				String content =edt_content.getText().toString().trim();
				if (content.length()>=1) {
					try {
                        switch (database_tableNo){
                            case 1:
                                List<HouseAddressOldBean> list1=new ArrayList<HouseAddressOldBean>();
                                list1=dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("scode", "like", "%"+content+"%"));
                                if (list1==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list1.size()==0) {
                                    list1=dbUtils.findAll(Selector.from(HouseAddressOldBean.class).where("address", "like", "%"+content+"%"));
                                    if (list1.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean>  adapter = new CommonAdapter<HouseAddressOldBean>(context1,list1,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean houseAddress =(HouseAddressOldBean) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 2:
                                List<HouseAddressOldBean2> list2=new ArrayList<HouseAddressOldBean2>();
                                list2=dbUtils.findAll(Selector.from(HouseAddressOldBean2.class).where("scode", "like", "%"+content+"%"));
                                if (list2==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list2.size()==0) {
                                    list2=dbUtils.findAll(Selector.from(HouseAddressOldBean2.class).where("address", "like", "%"+content+"%"));
                                    if (list2.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean2>  adapter2 = new CommonAdapter<HouseAddressOldBean2>(context1,list2,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean2 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter2);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean2 houseAddress =(HouseAddressOldBean2) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 3:
                                List<HouseAddressOldBean3> list3=new ArrayList<HouseAddressOldBean3>();
                                list3=dbUtils.findAll(Selector.from(HouseAddressOldBean3.class).where("scode", "like", "%"+content+"%"));
                                if (list3==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list3.size()==0) {
                                    list3=dbUtils.findAll(Selector.from(HouseAddressOldBean3.class).where("address", "like", "%"+content+"%"));
                                    if (list3.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean3>  adapter3 = new CommonAdapter<HouseAddressOldBean3>(context1,list3,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean3 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter3);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean3 houseAddress =(HouseAddressOldBean3) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 4:
                                List<HouseAddressOldBean4> list4=new ArrayList<HouseAddressOldBean4>();
                                list4=dbUtils.findAll(Selector.from(HouseAddressOldBean4.class).where("scode", "like", "%"+content+"%"));
                                if (list4==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list4.size()==0) {
                                    list4=dbUtils.findAll(Selector.from(HouseAddressOldBean4.class).where("address", "like", "%"+content+"%"));
                                    if (list4.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean4>  adapter4 = new CommonAdapter<HouseAddressOldBean4>(context1,list4,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean4 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter4);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean4 houseAddress =(HouseAddressOldBean4) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 5:
                                List<HouseAddressOldBean5> list5=new ArrayList<HouseAddressOldBean5>();
                                list5=dbUtils.findAll(Selector.from(HouseAddressOldBean5.class).where("scode", "like", "%"+content+"%"));
                                if (list5==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list5.size()==0) {
                                    list5=dbUtils.findAll(Selector.from(HouseAddressOldBean5.class).where("address", "like", "%"+content+"%"));
                                    if (list5.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean5>  adapter5 = new CommonAdapter<HouseAddressOldBean5>(context1,list5,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean5 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter5);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean5 houseAddress =(HouseAddressOldBean5) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 6:
                                List<HouseAddressOldBean6> list6=new ArrayList<HouseAddressOldBean6>();
                                list6=dbUtils.findAll(Selector.from(HouseAddressOldBean6.class).where("scode", "like", "%"+content+"%"));
                                if (list6==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list6.size()==0) {
                                    list6=dbUtils.findAll(Selector.from(HouseAddressOldBean6.class).where("address", "like", "%"+content+"%"));
                                    if (list6.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean6>  adapter6 = new CommonAdapter<HouseAddressOldBean6>(context1,list6,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean6 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter6);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean6 houseAddress =(HouseAddressOldBean6) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 7:
                                List<HouseAddressOldBean7> list7=new ArrayList<HouseAddressOldBean7>();
                                list7=dbUtils.findAll(Selector.from(HouseAddressOldBean7.class).where("scode", "like", "%"+content+"%"));
                                if (list7==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list7.size()==0) {
                                    list7=dbUtils.findAll(Selector.from(HouseAddressOldBean7.class).where("address", "like", "%"+content+"%"));
                                    if (list7.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean7>  adapter7 = new CommonAdapter<HouseAddressOldBean7>(context1,list7,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean7 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter7);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean7 houseAddress =(HouseAddressOldBean7) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 8:
                                List<HouseAddressOldBean8> list8=new ArrayList<HouseAddressOldBean8>();
                                list8=dbUtils.findAll(Selector.from(HouseAddressOldBean8.class).where("scode", "like", "%"+content+"%"));
                                if (list8==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list8.size()==0) {
                                    list8=dbUtils.findAll(Selector.from(HouseAddressOldBean8.class).where("address", "like", "%"+content+"%"));
                                    if (list8.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean8>  adapter8 = new CommonAdapter<HouseAddressOldBean8>(context1,list8,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean8 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter8);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean8 houseAddress =(HouseAddressOldBean8) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 9:
                                List<HouseAddressOldBean9> list9=new ArrayList<HouseAddressOldBean9>();
                                list9=dbUtils.findAll(Selector.from(HouseAddressOldBean9.class).where("scode", "like", "%"+content+"%"));
                                if (list9==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list9.size()==0) {
                                    list9=dbUtils.findAll(Selector.from(HouseAddressOldBean9.class).where("address", "like", "%"+content+"%"));
                                    if (list9.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean9>  adapter9 = new CommonAdapter<HouseAddressOldBean9>(context1,list9,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean9 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter9);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean9 houseAddress =(HouseAddressOldBean9) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;
                            case 10:
                                List<HouseAddressOldBean10> list10=new ArrayList<HouseAddressOldBean10>();
                                list10=dbUtils.findAll(Selector.from(HouseAddressOldBean10.class).where("scode", "like", "%"+content+"%"));
                                if (list10==null) {
                                    Toast.makeText(context1, "未下载地址库,请先下载全库地址~", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (list10.size()==0) {
                                    list10=dbUtils.findAll(Selector.from(HouseAddressOldBean10.class).where("address", "like", "%"+content+"%"));
                                    if (list10.size()==0) {
                                        Dialog.showRadioDialog(context, "查无此房屋！！！", new DialogClickListener() {
                                            @Override
                                            public void confirm() {}
                                            @Override
                                            public void cancel() {}
                                        });
                                    }
                                }
                                CommonAdapter<HouseAddressOldBean10>  adapter10 = new CommonAdapter<HouseAddressOldBean10>(context1,list10,R.layout.item_text) {
                                    @Override
                                    public void convert(ViewHolder helper,HouseAddressOldBean10 item, int position) {
                                        helper.setText(R.id.tv_content, item.address);
                                        helper.setText(R.id.tv_bianhao,"房屋编号："+ item.scode);
                                    }
                                };
                                lv.setAdapter(adapter10);
                                lv.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                                        HouseAddressOldBean10 houseAddress =(HouseAddressOldBean10) ((ListView)parent).getItemAtPosition(position);
                                        onEnsureClickListener.OnHouseEnSureClick(houseAddress.scode, houseAddress.address);
                                        PopHouse.this.dismiss();
                                    }
                                });
                                break;

                        }

						
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
		});
		edt_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					tv_up.setVisibility(View.VISIBLE);
					ll_bottom.setVisibility(View.GONE);
				}else{

				}
			}
		});
		tv_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edt_content.setText("");
				edt_content.clearFocus();
				tv_up.setVisibility(View.GONE);
				ll_bottom.setVisibility(View.VISIBLE);
			}
		});


	}


	private void updateUser(){
		int pcsCurrentPostion =PCSView.getCurrentItem();
		mCurrentPcs=PSCName[pcsCurrentPostion];
		String users[] =UserNameMap.get(mCurrentPcs);
		String userids[] =UserIdMap.get(mCurrentPcs);
		if (users==null) {
			users=new String[]{""};
		}
		if (userids==null) {
			userids=new String[]{""};
		}
		USERView.setViewAdapter(new ArrayWheelAdapter<String>(context1, users));
		USERView.setCurrentItem(0);
		mCurrentUserId=userids[0];
		mCurrentUserName=users[0];
		
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel==PCSView) {
			updateUser();
		}else if (wheel==USERView) {
			mCurrentUserName=UserNameMap.get(mCurrentPcs)[newValue];
			mCurrentUserId=UserIdMap.get(mCurrentPcs)[newValue];
		}
	}
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		viewfipper.startFlipping();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ensure:
            try {
                switch (database_tableNo){
                    case 1:
                        HouseAddress list_er=dbUtils.findFirst(HouseAddress.class);
                        if (list_er==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 2:
                        HouseAddress2 list_er2=dbUtils.findFirst(HouseAddress2.class);
                        if (list_er2==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 3:
                        HouseAddress3 list_er3=dbUtils.findFirst(HouseAddress3.class);
                        if (list_er3==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 4:
                        HouseAddress4 list_er4=dbUtils.findFirst(HouseAddress4.class);
                        if (list_er4==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 5:
                        HouseAddress5 list_er5=dbUtils.findFirst(HouseAddress5.class);
                        if (list_er5==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 6:
                        HouseAddress6 list_er6=dbUtils.findFirst(HouseAddress6.class);
                        if (list_er6==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 7:
                        HouseAddress7 list_er7=dbUtils.findFirst(HouseAddress7.class);
                        if (list_er7==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 8:
                        HouseAddress8 list_er8=dbUtils.findFirst(HouseAddress8.class);
                        if (list_er8==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 9:
                        HouseAddress9 list_er9=dbUtils.findFirst(HouseAddress9.class);
                        if (list_er9==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                    case 10:
                        HouseAddress10 list_er10=dbUtils.findFirst(HouseAddress10.class);
                        if (list_er10==null){
                            Toast.makeText(context1,"无二级关联地址，请直接搜索房屋编号或地址",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        onEnsureClickListener.OnHouseEnSureClick(mCurrentUserId, mCurrentPcs+mCurrentUserName.trim());
                        this.dismiss();
                        break;
                }


            } catch (DbException e) {
                e.printStackTrace();
            }

			break;
		case R.id.btn_cancle:
			this.dismiss();
			break;
		default:
			break;
		}
	}
}
