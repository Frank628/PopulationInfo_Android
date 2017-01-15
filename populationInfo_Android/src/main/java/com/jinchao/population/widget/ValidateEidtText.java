package com.jinchao.population.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jinchao.population.R;
import com.jinchao.population.base.BaseHandleIDActivity;
import com.jinchao.population.utils.CommonIdcard;
import com.jinchao.population.utils.CommonUtils;

/**
 * Created by OfferJiShu01 on 2016/11/9.
 */

public class ValidateEidtText extends EditText {

    int textColor, textErrorColor;
    Drawable drawableLeft_Right=null,drawableLeft_Error=null,drawableRight_Right=null,drawableRight_Error=null,drawable_empty=null;
    int current_validate_type=-1;
    public ValidateEidtText(Context context) {
        this(context,null);
    }

    public ValidateEidtText(Context context, AttributeSet attrs) {
        this(context, attrs,android.R.attr.editTextStyle);
    }

    public ValidateEidtText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValidateEditText,defStyleAttr,0);
        for (int i=0;i<a.getIndexCount();i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.ValidateEditText_vd_textRightColor:
                    textColor=a.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.ValidateEditText_vd_validate_type:
                    current_validate_type=a.getInteger(attr,-1);
                    break;
                case R.styleable.ValidateEditText_vd_textErrorColor:
                    textErrorColor=a.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.ValidateEditText_vd_drawable_left_right:
                    drawableLeft_Right=a.getDrawable(attr);
                    break;
                case R.styleable.ValidateEditText_vd_drawable_left_error:
                    drawableLeft_Error=a.getDrawable(attr);
                    break;
                case R.styleable.ValidateEditText_vd_drawable_right_right:
                    drawableRight_Right=a.getDrawable(attr);
                    break;
                case R.styleable.ValidateEditText_vd_drawable_right_error:
                    drawableRight_Error=a.getDrawable(attr);
                    break;
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(
                null, null, getResources().getDrawable(R.drawable.icon_empty), null);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }else{
                    validate(hasFocus);
                }
            }
        });
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
               validate(true);
            }
        });
    }

    private void validate(boolean hasFocus){
            if (TextUtils.isEmpty(getText().toString().trim())){
                setCompoundDrawablesWithIntrinsicBounds(
                        null, null, getResources().getDrawable(R.drawable.icon_empty), null);
                return;
            }
            switch (current_validate_type){
                case 0://手机
                    if (!CommonUtils.isGuangdaTel(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("电话必须为8、11、12位全数字！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 1://别名
                    if (!CommonUtils.isChinese(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("别名为15位的汉字！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 2://邮箱
                    if (!CommonUtils.isEmail(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("a-Z 0-9中间必须加@符号",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 3://身份证
                    if (!hasFocus){
                        String idcard=getText().toString().trim();
                        if (CommonIdcard.validateCard(idcard)) {
                            if (idcard.length() == 15) {
                                idcard = CommonIdcard.conver15CardTo18(idcard);
                                setText(idcard);
                                Toast.makeText(getContext().getApplicationContext(), "15位转18位证件号成功",Toast.LENGTH_SHORT).show();
                            } else if (idcard.length() == 17) {
                                idcard = CommonIdcard.conver17CardTo18(idcard);
                                setText(idcard);
                                Toast.makeText(getContext().getApplicationContext(), "17位转18位证件号成功", Toast.LENGTH_SHORT).show();
                            }
                            setTextColor(textColor);
                            setCompoundDrawablesWithIntrinsicBounds(
                                    null, null, drawableRight_Right, null);
                        } else {
                            setCompoundDrawablesWithIntrinsicBounds(
                                    null, null, drawableRight_Error, null);
                            setTextColor(textErrorColor);
                            setError("身份证号格式不合法！",null);
                        }
                    }else{
                        if (getText().toString().trim().length()==18&&CommonIdcard.validateCard(getText().toString().trim())){
                            setTextColor(textColor);
                            setCompoundDrawablesWithIntrinsicBounds(
                                    null, null, drawableRight_Right, null);
                        }
                    }
                    break;
                case 4://车牌号
                    if (!CommonUtils.isCarNo(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("车牌号格式错误",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 5://身高
                    if (!CommonUtils.isTrueHigh(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("身高格式错误！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 6://室号
                    if (getText().toString().trim().length()!=4){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("室号由4位数字或字母组成！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 7://5位汉字的职位名称
                    if (!(getText().toString().trim().length()<=5&&CommonUtils.isChinese(getText().toString().trim()))){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("职位名称5个汉字以内！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 8://18字符的社保编号
                    if (!CommonUtils.isSBBH(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("社保卡18个字符或9个汉字！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 9://10字符的健康证编号
                    if (!CommonUtils.isJKZBH(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("健康证10个字符或5个汉字！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 10://房屋编号
                    if (!CommonUtils.isFangwuBianHao(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("房屋编号由6位字母或数字组成！",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 11://地址
                    if (!CommonUtils.isAddress(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("提示:户籍详细地址只能输入汉字、数字、大写字母和-(中横线)以及英文半角小括号和、(中文半角顿号)!",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                case 12://非单引号字符
                    if (!CommonUtils.isNormal(getText().toString().trim())){
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Error, null);
                        setTextColor(textErrorColor);
                        setError("提示:不可包含单引号'",null);
                    }else{
                        setTextColor(textColor);
                        setCompoundDrawablesWithIntrinsicBounds(
                                null, null, drawableRight_Right, null);
                    }
                    break;
                default:
                    break;
            }

    }

}
