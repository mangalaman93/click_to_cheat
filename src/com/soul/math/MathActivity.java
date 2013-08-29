package com.soul.math;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.soul_flower.math.R;

public class MathActivity extends Activity {
	TextView Result;
	EditText Input;
	int MAX_LENGTH = 6, list_index = 0, new_pos;
	ArrayList<Double> result_Array = new ArrayList<Double>();
	ArrayList<String> input_Array = new ArrayList<String>();
	ArrayList<String> Total_Result = new ArrayList<String>();
	boolean resultFlag = true;
	ListView resultView;
	String txt_temp, text_temp_another;
	boolean radion = false;
	private ViewFlipper vf;
	private float lastX;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_math);

		vf = (ViewFlipper) findViewById(R.id.view_flipper);
		Input = (EditText) findViewById(R.id.calInput);
		resultView = (ListView) findViewById(R.id.listview);

		Input.setKeyListener(null);

		resultView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				txt_temp = Input.getText().toString();
				text_temp_another = txt_temp.substring(0,
						Input.getSelectionStart())
						+ result_Array.get(position);
				new_pos = text_temp_another.length();
				txt_temp = text_temp_another
						+ txt_temp.substring(Input.getSelectionEnd(),
								txt_temp.length());
				Input.setText(txt_temp);
				Input.setSelection(new_pos);
				resultFlag = false;
			}
		});

		resultView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Input.setText(input_Array.get(position));
				Input.setSelection(Input.getText().length());
				resultFlag = false;
				return true;
			}
		});

		View.OnClickListener ButtonHandler = new View.OnClickListener() {
			public void onClick(View v) {
				Button bt_from = (Button) v;
				switch (bt_from.getId()) {
				case R.id.mult:
					if (resultFlag) {
						Input.setText("*");
						resultFlag = false;
					} else {
						txt_temp = Input.getText().toString();
						text_temp_another = txt_temp.substring(0,
								Input.getSelectionStart())
								+ "*";
						new_pos = text_temp_another.length();
						txt_temp = text_temp_another
								+ txt_temp.substring(Input.getSelectionEnd(),
										txt_temp.length());
						Input.setText(txt_temp);
						Input.setSelection(new_pos);
					}
					break;

				case R.id.clear:
					Input.setText("");
					break;

				case R.id.backspace:
				case R.id.backspace2:
					int temp = 0;
					if (Input.getSelectionEnd() == Input.getSelectionStart()) {
						temp = 1;
					}
					txt_temp = Input.getText().toString();
					if (Input.getSelectionEnd() == 0) {
						break;
					}
					text_temp_another = txt_temp.substring(0,
							Input.getSelectionStart() - temp);
					new_pos = text_temp_another.length();
					txt_temp = text_temp_another
							+ txt_temp.substring(Input.getSelectionEnd(),
									txt_temp.length());
					Input.setText(txt_temp);
					Input.setSelection(new_pos);
					break;

				case R.id.equal:
				case R.id.equal2:
					resultFlag = equalHandler();
					Input.setSelection(Input.getText().length());
					break;

				case R.id.abs:
					if (resultFlag) {
						Input.setText("abs()");
						Input.setSelection(Input.getText().length() - 1);
						resultFlag = false;
					} else {
						txt_temp = Input.getText().toString();
						text_temp_another = txt_temp.substring(0,
								Input.getSelectionStart())
								+ "abs()";
						new_pos = text_temp_another.length();
						txt_temp = text_temp_another
								+ txt_temp.substring(Input.getSelectionEnd(),
										txt_temp.length());
						Input.setText(txt_temp);
						Input.setSelection(new_pos - 1);
					}
					break;

				default:
					if (resultFlag) {
						text_temp_another = (String) bt_from.getText();
						new_pos = text_temp_another.length();
						txt_temp = "";
						resultFlag = false;
					} else {
						txt_temp = Input.getText().toString();
						text_temp_another = txt_temp.substring(0,
								Input.getSelectionStart())
								+ bt_from.getText();
						new_pos = text_temp_another.length();
						txt_temp = txt_temp.substring(Input.getSelectionEnd(),
								txt_temp.length());
					}
					if (bt_from.getId() == R.id.sqrt
							|| bt_from.getId() == R.id.log
							|| bt_from.getId() == R.id.ln
							|| bt_from.getId() == R.id.sin
							|| bt_from.getId() == R.id.cos
							|| bt_from.getId() == R.id.tan
							|| bt_from.getId() == R.id.asin
							|| bt_from.getId() == R.id.acos
							|| bt_from.getId() == R.id.atan
							|| bt_from.getId() == R.id.cos) {
						text_temp_another = text_temp_another + "()";
						new_pos = new_pos + 1;
					}
					txt_temp = text_temp_another + txt_temp;
					Input.setText(txt_temp);
					Input.setSelection(new_pos);
					break;
				}
			}
		};

		final Button bt_zero = (Button) findViewById(R.id.zero);
		bt_zero.setOnClickListener(ButtonHandler);
		final Button bt_one = (Button) findViewById(R.id.one);
		bt_one.setOnClickListener(ButtonHandler);
		final Button bt_two = (Button) findViewById(R.id.two);
		bt_two.setOnClickListener(ButtonHandler);
		final Button bt_three = (Button) findViewById(R.id.three);
		bt_three.setOnClickListener(ButtonHandler);
		final Button bt_four = (Button) findViewById(R.id.four);
		bt_four.setOnClickListener(ButtonHandler);
		final Button bt_five = (Button) findViewById(R.id.five);
		bt_five.setOnClickListener(ButtonHandler);
		final Button bt_six = (Button) findViewById(R.id.six);
		bt_six.setOnClickListener(ButtonHandler);
		final Button bt_seven = (Button) findViewById(R.id.seven);
		bt_seven.setOnClickListener(ButtonHandler);
		final Button bt_eight = (Button) findViewById(R.id.eight);
		bt_eight.setOnClickListener(ButtonHandler);
		final Button bt_nine = (Button) findViewById(R.id.nine);
		bt_nine.setOnClickListener(ButtonHandler);
		final Button bt_decimal = (Button) findViewById(R.id.decimal);
		bt_decimal.setOnClickListener(ButtonHandler);
		final Button bt_add = (Button) findViewById(R.id.add);
		bt_add.setOnClickListener(ButtonHandler);
		final Button bt_mult = (Button) findViewById(R.id.mult);
		bt_mult.setOnClickListener(ButtonHandler);
		final Button bt_opbr = (Button) findViewById(R.id.opbr);
		bt_opbr.setOnClickListener(ButtonHandler);
		final Button bt_sub = (Button) findViewById(R.id.sub);
		bt_sub.setOnClickListener(ButtonHandler);
		final Button bt_div = (Button) findViewById(R.id.div);
		bt_div.setOnClickListener(ButtonHandler);
		final Button bt_clbr = (Button) findViewById(R.id.clbr);
		bt_clbr.setOnClickListener(ButtonHandler);
		final Button bt_clear = (Button) findViewById(R.id.clear);
		bt_clear.setOnClickListener(ButtonHandler);
		final Button bt_equal = (Button) findViewById(R.id.equal);
		bt_equal.setOnClickListener(ButtonHandler);
		final Button backSpace = (Button) findViewById(R.id.backspace);
		backSpace.setOnClickListener(ButtonHandler);
		final Button bt_exp = (Button) findViewById(R.id.exp);
		bt_exp.setOnClickListener(ButtonHandler);
		final Button bt_ln = (Button) findViewById(R.id.ln);
		bt_ln.setOnClickListener(ButtonHandler);
		final Button bt_log = (Button) findViewById(R.id.log);
		bt_log.setOnClickListener(ButtonHandler);
		final Button bt_sin = (Button) findViewById(R.id.sin);
		bt_sin.setOnClickListener(ButtonHandler);
		final Button bt_cos = (Button) findViewById(R.id.cos);
		bt_cos.setOnClickListener(ButtonHandler);
		final Button bt_tan = (Button) findViewById(R.id.tan);
		bt_tan.setOnClickListener(ButtonHandler);
		final Button bt_asin = (Button) findViewById(R.id.asin);
		bt_asin.setOnClickListener(ButtonHandler);
		final Button bt_acos = (Button) findViewById(R.id.acos);
		bt_acos.setOnClickListener(ButtonHandler);
		final Button bt_atan = (Button) findViewById(R.id.atan);
		bt_atan.setOnClickListener(ButtonHandler);
		final Button bt_sqrt = (Button) findViewById(R.id.sqrt);
		bt_sqrt.setOnClickListener(ButtonHandler);
		final Button bt_power = (Button) findViewById(R.id.power);
		bt_power.setOnClickListener(ButtonHandler);
		final Button bt_abs = (Button) findViewById(R.id.abs);
		bt_abs.setOnClickListener(ButtonHandler);
		final Button bt_fact = (Button) findViewById(R.id.fact);
		bt_fact.setOnClickListener(ButtonHandler);
		final Button bt_pi = (Button) findViewById(R.id.pi);
		bt_pi.setOnClickListener(ButtonHandler);
		final Button bt_backspace2 = (Button) findViewById(R.id.backspace2);
		bt_backspace2.setOnClickListener(ButtonHandler);
		final Button bt_equal2 = (Button) findViewById(R.id.equal2);
		bt_equal2.setOnClickListener(ButtonHandler);
	}

	/* Menu Content */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_math, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.degree:
			Toast.makeText(this, "You've selected degree", Toast.LENGTH_LONG)
					.show();
			radion = false;
			return true;
		case R.id.radion:
			Toast.makeText(this, "You've selected radion", Toast.LENGTH_LONG)
					.show();
			radion = true;
			return true;
		case R.id.help:
			final Dialog dialog1 = new Dialog(this);
			dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog1.setContentView(R.layout.dialouge);

			Button ok = (Button) dialog1.findViewById(R.id.ok);
			ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog1.dismiss();
				}
			});
			dialog1.show();
			return true;
		}
		return false;
	}

	boolean equalHandler() {
		String expr = Input.getText().toString();
		if (expr.startsWith("-") || expr.startsWith("+")) {
			expr = "0" + expr;
		}
		try {
			double result = EvalExpr.evaluate(expr, radion);
			result_Array.add(result);
			input_Array.add(Input.getText().toString());
			Total_Result.add(Input.getText().toString() + " = " + result);
			list_index = list_index + 1;

			if (list_index > MAX_LENGTH) {
				result_Array.remove(0);
				input_Array.remove(0);
				Total_Result.remove(0);
				list_index = list_index - 1;
			}

			Input.setText("" + result);
			SpecialAdapter adapter = new SpecialAdapter(this, Total_Result);
			resultView.setAdapter(adapter);
			resultView.setSelection(adapter.getCount() - 1);
			resultView.setItemsCanFocus(false);
			return true;
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "erroneous expression!!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	static class ViewHolder {
		TextView text;
	}

	private class SpecialAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<String> data;

		public SpecialAdapter(Context context, ArrayList<String> items) {
			mInflater = LayoutInflater.from(context);
			this.data = items;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// A view to hold each row in the list
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_layout, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.txtTitle);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(data.get(position));
			return convertView;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = touchevent.getX();
			break;
		case MotionEvent.ACTION_UP:
			float currentX = touchevent.getX();
			if (lastX < currentX) {
				if (vf.getDisplayedChild() == 0)
					break;
				vf.setInAnimation(this, R.anim.in_from_left);
				vf.setOutAnimation(this, R.anim.out_to_right);
				vf.showNext();
			}
			if (lastX > currentX) {
				if (vf.getDisplayedChild() == 1)
					break;
				vf.setInAnimation(this, R.anim.in_from_right);
				vf.setOutAnimation(this, R.anim.out_to_left);
				vf.showPrevious();
			}
			break;
		}
		return false;
	}
}
