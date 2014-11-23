package com.yifa.health_manage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.yifa.health_manage.util.AndroidUtils;
import com.yifa.health_manage.util.MyLoger;

public class UtilsAllActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private MyLoger loger = MyLoger.getInstence("UtilsAllActivity");

	private RadioGroup saxGroup;

	private TextView title, message, messageName, item_ps;

	private ImageButton left;
	private EditText nameEdit, briEdit, heightEdit, weightEdit, yaoEdit;

	private boolean sax = true;// true-man false -woman

	private int height, weight, yao, type;

	private final int type0 = 0, type1 = 1, type2 = 2, type3 = 3, type4 = 4,
			type5 = 5, type6 = 6;
	private TextView text1, text2, text3, text4, text5, text6;

	private int brisday;

	private Button okButton;

	private LinearLayout allLayout, nameLayout, itemlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_utils_all);
		initView();
		type = getIntent().getIntExtra("type", 0);
		switch (type) {
		case type0:
			title.setText("体重检测");
			messageName.setText("体重检测");
			item_ps.setText(R.string.utils_wight);
			break;
		case type1:
			title.setText("一分钟肥胖检测");
			messageName.setText("一分钟肥胖检测");
			item_ps.setText(R.string.utils_1fenzhong);
			break;
		case type2:
			messageName.setText("标准体重计算器");
			title.setText("标准体重");
			item_ps.setText(R.string.utils_biaozhun);
			break;
		case type3:
			title.setText("运动心率");
			messageName.setText("运动心率");
			item_ps.setText(R.string.utils_yundong);
			break;
		case type4:
			title.setText("每日所需总热量");
			messageName.setText("每日所需总热量");
			item_ps.setText(R.string.utils_reliang);
			break;
		case type5:
			title.setText("基础代谢计算");
			messageName.setText("基础代谢");
			item_ps.setText(R.string.utils_jichudaixie);
			break;
		case type6:
			title.setText("全部检测");
			messageName.setText("全部检测");
			item_ps.setText(R.string.utils_all_mes);
			break;

		default:
			break;
		}
	}

	private void initView() {
		title = (TextView) findViewById(R.id.activity_top_title);
		item_ps = (TextView) findViewById(R.id.jiashao);
		messageName = (TextView) findViewById(R.id.mess_name);
		message = (TextView) findViewById(R.id.text);
		saxGroup = (RadioGroup) findViewById(R.id.user_sax);
		briEdit = (EditText) findViewById(R.id.user_bri);
		heightEdit = (EditText) findViewById(R.id.user_hight);
		weightEdit = (EditText) findViewById(R.id.user_wight);
		yaoEdit = (EditText) findViewById(R.id.user_yaowei);
		okButton = (Button) findViewById(R.id.register_btn_next);
		allLayout = (LinearLayout) findViewById(R.id.allLayout);
		nameLayout = (LinearLayout) findViewById(R.id.namelayout);
		itemlayout = (LinearLayout) findViewById(R.id.itemLayout);

		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		text4 = (TextView) findViewById(R.id.text4);
		text5 = (TextView) findViewById(R.id.text5);
		text6 = (TextView) findViewById(R.id.text6);
		title.setText("全部检测");

		saxGroup.setOnCheckedChangeListener(this);
		title.setOnClickListener(this);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_top_title:
			finish();
			break;
		case R.id.register_btn_next:

			if (isCheck()) {
				initDate();
				String meass = "";
				itemlayout.setVisibility(View.VISIBLE);
				switch (type) {
				case type0:
					meass = getObesityInt() + "BMI";
					nameLayout.setVisibility(View.VISIBLE);
					break;
				case type1:
					nameLayout.setVisibility(View.VISIBLE);
					meass = getObesity();
					break;
				case type2:
					nameLayout.setVisibility(View.VISIBLE);
					meass = getWeight();
					break;
				case type3:
					nameLayout.setVisibility(View.VISIBLE);
					meass = getSportHeart();
					break;
				case type4:
					nameLayout.setVisibility(View.VISIBLE);
					meass = getHotValue();
					break;
				case type5:
					nameLayout.setVisibility(View.VISIBLE);
					meass = getJiChuDaiXie() + "BMR";
					break;
				case type6:
					allLayout.setVisibility(View.VISIBLE);
					setAll(getUserData());
					break;
				default:
					break;
				}
				message.setText(meass);
				break;
			}
		default:
			break;
		}
	}

	private void setAll(String[] ss) {
		text1.setText(ss[0]);
		text2.setText(ss[1]);
		text3.setText(ss[2]);
		text4.setText(ss[3]);
		text5.setText(ss[4]);
		text6.setText(ss[5]);

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.sax_man:
			sax = true;
			break;
		case R.id.sax_women:
			sax = false;
			break;

		default:
			break;
		}

	}

	private String[] getUserData() {

		String string1 = getObesityInt() + "";
		String string2 = getObesity();
		String string3 = getWeight();
		String string4 = getSportHeart();
		String string5 = getHotValue();
		String string6 = getJiChuDaiXie();

		return new String[] { string1, string2, string3, string4, string5,
				string6 };
	}

	private void initDate() {
		height = Integer.valueOf(heightEdit.getText().toString().trim());
		weight = Integer.valueOf(weightEdit.getText().toString().trim());
		yao = Integer.valueOf(yaoEdit.getText().toString().trim());
		brisday = Integer.valueOf(briEdit.getText().toString().trim());
	}

	// 男性标准体重计算公式 = (身高cm-100)x0.9(kg)
	// 女性标准体重计算公式 = (身高cm-100)x0.9(kg)-2.5(kg)
	private String getWeight() {

		String value = "";
		if (sax) {
			value = (height - 100) * 0.9 + "";
		} else {
			value = (height - 100) * 0.9 - 2.5 + "";
		}
		return value;
	}

	/**
	 * 
	 * 肥胖检测 “体重指数”（bmi）=体重（kg）/身高的平方（┫）；如体重70公斤，身高1.7米，bmi＝70÷（1.7×1.7)＝24。
	 * “体重指数”（bmi）进行实际体重评价 中国成人的bmi的判定标准 bmi
	 * 
	 * 偏瘦： BMI指数 < 18 正常体重： BMI指数 = 18 - 25 超重： BMI指数 = 25 - 30 轻度肥胖： BMI指数 > 30
	 * 中度肥胖： BMI指数 > 35 重度肥胖： BMI指数 > 40
	 * */
	private String getObesity() {

		String value = "失败";
		float a = weight * 10000 / (height * height);

		if (a < 18) {
			value = "偏瘦";
		} else if (a >= 18 && a < 25) {
			value = "正常";
		} else if (a >= 25 && a < 30) {
			value = "超重";
		} else if (a >= 30 && a < 35) {
			value = "轻度肥胖";
		} else if (a >= 35 && a < 40) {
			value = "中度肥胖";
		} else if (a >= 40) {
			value = "重度肥胖";
		}

		return value;
	}

	// 体重指数
	private double getObesityInt() {
		double a = weight * 10000 / (height * height);
		return a;
	}

	/**
	 * 最佳运动心率控制区域计算法：(适合一般人) （220─现在年龄）×0.8=最大运动心率 （220─现在年龄）×0.6=最小运动心率
	 * */
	private String getSportHeart() {

		int mix = (int) ((220 - brisday) * 0.8);
		int max = (int) ((220 - brisday) * 0.6);

		return max + "-" + mix;
	}

	private boolean isDateTime() {
		String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		Pattern pattern = Pattern.compile(eL);
		Matcher matcher = pattern.matcher("");

		return matcher.matches();
	}

	private int isUserAge() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date oldDate = dateFormat.parse("");
			int age = (int) (System.currentTimeMillis() - oldDate.getTime())
					/ (3600 * 24 * 365 * 1000);
			loger.d(age);
			return age;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * */
	private String getHotValue() {
		String value = "";
		if (sax) {
			value = (66 + (13.7 * weight) + (5 * brisday) - (6.8 * isUserAge()))
					* 1.5 + "";
		} else {
			value = (655 + (9.6 * weight) + (1.7 * brisday) - (4.7 * isUserAge()))
					* 1.5 + "";
		}
		return value;
	}

	/**
	 * 基础代谢率计算公式: 女性:655 + (9.6 x 体重) + (1.7 x 身高) - (4.7X年龄) 男性:66 + (13.7 x
	 * 体重) + (5.0 x 身高) - (6.8x年龄)
	 * */
	private String getJiChuDaiXie() {
		String value = "";
		if (sax) {
			value = 66 + (13.7 * weight) + (5 * brisday) - (6.8 * isUserAge())
					+ "";
		} else {
			value = 655 + (9.6 * weight) + (1.7 * brisday)
					- (4.7 * isUserAge()) + "";

		}
		return value;
	}

	private boolean isCheck() {
		if (briEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "请填写年龄");
			return false;
		} else if (Integer.valueOf(briEdit.getText().toString().trim()) <= 0
				|| Integer.valueOf(briEdit.getText().toString().trim()) > 120) {
			AndroidUtils.showToast(this, "请正确填写年龄");
			return false;
		} else if (heightEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "请填写身高");
			return false;
		} else if (weightEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "请填写体重");
			return false;
		} else if (yaoEdit.getText().toString().trim().equalsIgnoreCase("")) {
			AndroidUtils.showToast(this, "请填写腰围");
			return false;
		}
		return true;
	}
}
