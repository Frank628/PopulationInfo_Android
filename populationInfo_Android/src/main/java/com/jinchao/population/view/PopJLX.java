package com.jinchao.population.view;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import com.jinchao.population.MyInfomationManager;
import com.jinchao.population.R;
import com.jinchao.population.base.CommonAdapter;
import com.jinchao.population.base.ViewHolder;
import com.jinchao.population.config.Constants;
import com.jinchao.population.entity.JLXBean;
import com.jinchao.population.entity.YanZhengBean;
import com.jinchao.population.utils.ResultBeanAndList;
import com.jinchao.population.utils.XmlUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PopJLX extends PopupWindow{
	private ListView lv;
	private EditText edt_content;
	private OnJLXListClickListener onJLXListClickListener;
	private ProgressBar pBar;
	public interface OnJLXListClickListener{
		void OnJLXClick(String str);
	}
	private CommonAdapter<JLXBean> adapter;
	private Context context;
	public PopJLX(Context context,OnJLXListClickListener onJLXListClickListener) {
		super();
		this.context=context;
		this.onJLXListClickListener = onJLXListClickListener;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v=inflater.inflate(R.layout.pop_search, null);
		this.setContentView(v);
		this.setWidth(android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		this.setHeight(android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        // 刷新状态  
        this.update();  
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(dw);  
        edt_content=(EditText) v.findViewById(R.id.edt_content);
        pBar=(ProgressBar) v.findViewById(R.id.pb);
        lv=(ListView) v.findViewById(R.id.lv);
        v.findViewById(R.id.ib_close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopJLX.this.dismiss();
			}
		});
        edt_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (edt_content.getText().toString().trim().length()>=2) {
					getRequest(edt_content.getText().toString().trim());
				}else{
					lv.setAdapter(null);
				}
			}
		});
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JLXBean jlxBean =(JLXBean) ((ListView)parent).getItemAtPosition(position);
				PopJLX.this.onJLXListClickListener.OnJLXClick(jlxBean.rn);
				PopJLX.this.dismiss();
			}
		});
	}
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            // 以下拉方式显示popupwindow  
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {  
            this.dismiss();  
        }  
    }
	@Event(value={R.id.btn_search})
	public void onSeachClick(View view){
		if (edt_content.getText().toString().trim().length()>=2) {
			getRequest(edt_content.getText().toString().trim());
		}else{
			Toast.makeText(context, "至少输入两个以上字符", 0).show();
		}
	}
	private void getRequest(String content){
		pBar.setVisibility(View.VISIBLE);
		RequestParams params=new RequestParams(Constants.URL+"HouseAddress.aspx");
		params.addBodyParameter("type", "getroad");
		params.addBodyParameter("road_name",content);
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				pBar.setVisibility(View.GONE);
				ResultBeanAndList<JLXBean> jlxxml=null;
				try {
					jlxxml= XmlUtils.getBeanByParseXml(result,"Table",JLXBean.class, "", JLXBean.class);
					Log.d("aaa", jlxxml.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
//				if (adapter==null) {
					List<JLXBean> list=new ArrayList<JLXBean>();
					if (jlxxml!=null) {
						list.addAll(jlxxml.list);
					}
						adapter = new CommonAdapter<JLXBean>(context,list,R.layout.item_text) {
							@Override
							public void convert(ViewHolder helper, JLXBean item,
									int position) {
								helper.setText(R.id.tv_content, item.rn);
								helper.getView(R.id.tv_bianhao).setVisibility(View.GONE);
							}
						};
						lv.setAdapter(adapter);
					
//				}else{
//					adapter.notifyDataSetChanged();
//				}
			}
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(context, "服务器请求异常", Toast.LENGTH_SHORT).show();
				pBar.setVisibility(View.GONE);
			}
			@Override
			public void onCancelled(CancelledException cex) {}
			@Override
			public void onFinished(){}
		});
	}
}
