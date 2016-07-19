package com.quicker.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.gsm.SmsMessage.SubmitPdu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quicker.R;
import com.quicker.tools.NoticeFragment;
import com.quicker.tools.StaticConstant;

public class FormActivity extends Activity {

	private ListView lt1;
	private TextView form_title;
	private Button btn_save;
	private Button btn_submit;
	private String[] formItem;
	private String[] answers;
	private String formTitle;
	private int i, j, num, grpNum, length;// num为表格编号，grpNum为所在组号，表长度

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		Intent intent = getIntent();
		Log.d("FormActivity", "start");

		num = intent.getIntExtra("childPosition", 0);
		grpNum = intent.getIntExtra("groupPosition", 0);
		formTitle = NoticeFragment.formTitle[grpNum][num];
		form_title = (TextView) findViewById(R.id.form_title);
		form_title.setText(formTitle);
		/*
		 * List<Map<String, Object>> formItems = new ArrayList<Map<String,
		 * Object>>(); for( i= 1; NoticeFragment.formItem[num][i]!=null; i++) {
		 * Map<String, Object> formItem = new HashMap<String, Object>();
		 * formItem.put("form_item", NoticeFragment.formItem[num][i]);
		 * formItems.add(formItem); } formItemNumble=i;
		 */

		/*
		 * SimpleAdapter simplead = new SimpleAdapter(this, formItems,
		 * R.layout.form_item, new String[] { "form_item" }, new int[]
		 * {R.id.form_item_name});
		 * lt1=(ListView)findViewById(R.id.listview_form_item);
		 * lt1.setAdapter(simplead);
		 * answer=(EditText)findViewById(R.id.form_item_answer);
		 */
		// formItem=new String[NoticeFragment.formItem[num].length];
		// answers=new String[NoticeFragment.formItem[num].length];
		// for (i = 0; NoticeFragment.formItem[num][i] != null; i++) {
		// }
		//
		// j = NoticeFragment.formItem.length;
		length = NoticeFragment.oneForm.length;
		formItem = NoticeFragment.oneForm;
		answers = new String[length];
		if (grpNum > 0) {// 打开的是已完成或收藏的表格
			answers = NoticeFragment.formAnswer;// 自动填写获取到的数据
		}
		FormListAdapter formListAdapter = new FormListAdapter();
		lt1 = (ListView) findViewById(R.id.listview_form_item);
		lt1.setAdapter(formListAdapter);
		Log.d("onClick", "--------0-------");
		btn_submit = (Button) findViewById(R.id.submit);
		btn_submit.setOnClickListener(formSubmitOnClickListener);
		Log.d("onClick", "--------1-------");
		btn_save = (Button) findViewById(R.id.save);
		btn_save.setOnClickListener(formSaveOnClickListener);
		Log.d("onClick", "--------2-------");

	}

	private OnClickListener formSubmitOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.d("json-onClick", "--------0-------");
			new Thread() {
				private String encoding;

				@Override
				public void run() {
					Looper.prepare();
					String urlPath = "http://"
							+ StaticConstant.localhostConstant
							+ ":8080/quicker/app/submit/"
							+ StaticConstant.stuIdConstant;
					URL url;
					Log.d("json", "--------0-------");
					try {
						url = new URL(urlPath);
						/* 封装子对象 */
						String[] Info = new String[length + 1];

						Log.d("json", "--------1-------");
						for (i = 0; i < length; i++) {
							Log.d("json", "--------i=" + i + "-------");
							Info[i] = answers[i];
						}
						Info[i] = NoticeFragment.formTitle[grpNum][num];
						Log.d("json", Info + "");
						/* 封装数组 */
						JSONObject json = new JSONObject();
						JSONArray array = new JSONArray(Info);

						json.put("formInfo", array);

						/* 把JSON数据转换成String类型使用输出流向服务器写 */
						String content = json.toString();
						Log.d("json00000000000", content);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setConnectTimeout(5000);
						conn.setDoOutput(true);// 设置允许输出
						Log.d("json1111", content);
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Accept-Charset", "utf-8");
						conn.setRequestProperty("User-Agent", "Fiddler");
						conn.setRequestProperty("Content-Type",
								"application/json");
						Log.d("json2222", content);
						// conn.setRequestProperty("Charset", encoding);
						OutputStream os = conn.getOutputStream();

						os.write(content.getBytes());
						os.flush();
						os.close();
						Log.d("json3333", content);
						/* 服务器返回的响应码 */
						int code = conn.getResponseCode();
						Log.d("json4444", content);
						if (code == 200) {
							Toast.makeText(getApplicationContext(), "表格提交成功",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"提交失败 code=" + code, Toast.LENGTH_SHORT)
									.show();
						}
					} catch (Exception e) {
						// TODO: handle exception
						Log.d("json-thread-error", "--------0-------");
						Log.d("json-thread-error", e.toString());
						// throw new RuntimeException(e);
					}
					Looper.loop();
				}

			}.start();

			btn_submit.setText("已提交");
		}

	};

	private OnClickListener formSaveOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("collectForm stuId formTitle", StaticConstant.stuIdConstant + "||" + formTitle);
			String url = "http://" + StaticConstant.localhostConstant
					+ ":8080/quicker/app/collectForm?id=" + StaticConstant.stuIdConstant
					+ "&formTitle=" + formTitle;
			StringRequest loginRequest = new StringRequest(Request.Method.POST,
					url, new Response.Listener<String>() {// 2.new 一个请求
						@Override
						public void onResponse(String s) {
							btn_save.setText("已收藏");
							Toast.makeText(getApplicationContext(), "已收藏",
									Toast.LENGTH_SHORT).show();
							btn_save.setEnabled(false);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "error",
									Toast.LENGTH_SHORT).show();
							Log.d("collectForm", arg0.toString());
						}
					});
			loginRequest.setTag("POST");
			RequestQueue mQueue = Volley.newRequestQueue(FormActivity.this);
			mQueue.add(loginRequest);
		}
	};

	class FormListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return formItem.length;
		}

		@Override
		public Object getItem(int position) {
			return formItem[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater
						.from(FormActivity.this);
				convertView = inflater.inflate(R.layout.form_item, null);
				holder.textView1 = (TextView) convertView
						.findViewById(R.id.form_item_name);
				holder.editText1 = (EditText) convertView
						.findViewById(R.id.form_item_answer);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ref = position;
			holder.textView1.setText(formItem[position]);
			holder.editText1.setText(answers[position]);
			if (grpNum > 0) {// 打开的是已完成或收藏的表格
				holder.editText1.setEnabled(false);// 不可以编辑内容
			}
			holder.editText1.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					answers[holder.ref] = arg0.toString();

				}
			});

			return convertView;
		}

	}

	class ViewHolder {
		TextView textView1;
		EditText editText1;
		int ref;
	}
}
