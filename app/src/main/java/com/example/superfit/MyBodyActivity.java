package com.example.superfit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.superfit.data.Contract;
import com.example.superfit.data.DbHelper;

/**
 * Экран, отображающий информацию о росте и весе пользователя
 */
public class MyBodyActivity extends AppCompatActivity {

    // объекты для диалоговых окон изменения роста и веса
    Dialog dialogChangeWeight;
    Dialog dialogChangeHeight;

    // хранит текущего пользователя в системе
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;

    TextView tv_weight;
    TextView tv_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_body);

        dialogChangeWeight = new Dialog(this);
        dialogChangeHeight = new Dialog(this);

        mSettings = getSharedPreferences(PREFERENCES.APP_PREFERENCES, Context.MODE_PRIVATE);
        editor = mSettings.edit();

        tv_weight = findViewById(R.id.tv_kg);
        tv_height = findViewById(R.id.tv_cm);

        tv_weight.setText(mSettings.getString(PREFERENCES.APP_PREFERENCES_WEIGHT, "") + " kg");
        tv_height.setText(mSettings.getString(PREFERENCES.APP_PREFERENCES_HEIGHT, "") + " cm");

        // обработка нажатия по кнопке назад
        ImageView btn_back_body = findViewById(R.id.btn_back_body);
        btn_back_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // обработка нажатия по кнопке Edit для изменения веса, вызов соответствующего диалогового окна
        TextView tv_editKg = findViewById(R.id.tv_edit1);
        tv_editKg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogChangeWeight();
            }
        });

        // обработка нажатия по кнопке Edit для изменения роста, вызов соответствующего диалогового окна
        TextView tv_editCm = findViewById(R.id.tv_edit2);
        tv_editCm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogChangeHeight();
            }
        });

    }

    private void openDialogChangeWeight() {
        dialogChangeWeight.setContentView(R.layout.layout_dialog_change_weight);
        dialogChangeWeight.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout btnChange = dialogChangeWeight.findViewById(R.id.dialog_btn_change);
        RelativeLayout btnCancel = dialogChangeWeight.findViewById(R.id.dialog_btn_cancel);

        EditText et = dialogChangeWeight.findViewById(R.id.et_weight);
        et.requestFocus();

        // блокировка кнопки Change , если поле пустое
        btnChange.setEnabled(false);
        et.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {
              }
              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                  if (et.getText().toString().isEmpty() && et.getText().toString().contains(" ")) {
                      btnChange.setEnabled(false);
                  } else {
                      btnChange.setEnabled(true);
                  }
              }

              @Override
              public void afterTextChanged(Editable s) {
              }
          });


        // обработка нажатия по кнопкам Change и Cancel
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_btn_change:
                        Double weight = Double.parseDouble(et.getText().toString());

                        DbHelper dbHelper = new DbHelper(getApplicationContext());

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Contract.UserEntry.COLUMN_WEIGHT, weight);
                        db.update(Contract.UserEntry.TABLE_NAME, values, Contract.UserEntry.COLUMN_NAME + "= ?", new String[] { mSettings.getString(PREFERENCES.APP_PREFERENCES_NAME, "") });

                        editor.putString(PREFERENCES.APP_PREFERENCES_WEIGHT, weight.toString()).commit();
                        tv_weight.setText(mSettings.getString(PREFERENCES.APP_PREFERENCES_WEIGHT, "")  + " kg");
                        dialogChangeWeight.cancel();
                        break;
                    case R.id.dialog_btn_cancel:
                        dialogChangeWeight.dismiss();
                        break;
                }
            }
        };

        btnChange.setOnClickListener(onClick);
        btnCancel.setOnClickListener(onClick);

        dialogChangeWeight.show();
    }

    private void openDialogChangeHeight() {
        dialogChangeHeight.setContentView(R.layout.layout_dialog_change_height);
        dialogChangeHeight.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout btnChange = dialogChangeHeight.findViewById(R.id.dialog_btn_change_height);
        RelativeLayout btnCancel = dialogChangeHeight.findViewById(R.id.dialog_btn_cancel_height);

        EditText et = dialogChangeHeight.findViewById(R.id.et_height);
        et.requestFocus();

        // блокировка кнопки Change , если поле пустое
        btnChange.setEnabled(false);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et.getText().toString().isEmpty() && et.getText().toString().contains(" ")) {
                    btnChange.setEnabled(false);
                } else {
                    btnChange.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // обработка нажатия по кнопкам Change и Cancel
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_btn_change_height:
                        Double height = Double.parseDouble(et.getText().toString());

                        DbHelper dbHelper = new DbHelper(getApplicationContext());

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(Contract.UserEntry.COLUMN_HEIGHT, height);
                        db.update(Contract.UserEntry.TABLE_NAME, values, Contract.UserEntry.COLUMN_NAME + "= ?", new String[] { mSettings.getString(PREFERENCES.APP_PREFERENCES_NAME, "") });

                        editor.putString(PREFERENCES.APP_PREFERENCES_HEIGHT, height.toString()).commit();
                        tv_height.setText(mSettings.getString(PREFERENCES.APP_PREFERENCES_HEIGHT, "")  + " cm");
                        dialogChangeHeight.cancel();
                        break;
                    case R.id.dialog_btn_cancel_height:
                        dialogChangeHeight.dismiss();
                        break;
                }
            }
        };

        btnChange.setOnClickListener(onClick);
        btnCancel.setOnClickListener(onClick);

        dialogChangeHeight.show();
    }
}