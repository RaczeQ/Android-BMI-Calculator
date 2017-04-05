package pl.edu.pwr.lab1raczycki.lab1;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mass;
    private EditText height;
    private RadioGroup buttons;
    private static final String UNITS_MODE = "metrical";
    private int mode;
    private boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        height = (EditText) findViewById(R.id.InputHeight);
        mass = (EditText) findViewById(R.id.InputMass);
        buttons = (RadioGroup) findViewById(R.id.radioGroup);
        buttons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                changeUnits(findViewById(checkedId));
            }
        });
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(UNITS_MODE, 0);
        } else {
            load();
            if (mode == 1) {
                RadioButton metrical = (RadioButton) findViewById(R.id.Metrical);
                metrical.setChecked(true);
            }
            if (mode == 2) {
                RadioButton imperial = (RadioButton) findViewById(R.id.Imperial);
                imperial.setChecked(true);
            }
        }
        if (mode == 0) {
            RadioButton metrical = (RadioButton) findViewById(R.id.Metrical);
            metrical.setChecked(true);
        }
        Button count = (Button) findViewById(R.id.Count);
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBMI();
            }
        });
        mass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    countBMI();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                save();
                return false;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                share();
                return false;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                goToAbout();
                return false;
            }
        });
        return true;
    }

    private void goToAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        this.startActivity(intent);
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String shareBody = getResources().getString(R.string.ShareBody) + " " + ((TextView) findViewById(R.id.Output)).getText();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.ShareSubject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.ShareVia)));
    }

    private void save() {
        SharedPreferences sp = getSharedPreferences("SuperBMICounterApp",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sp.edit();
        prefsEditor.putInt("mode", mode);
        prefsEditor.putString("mass", mass.getText().toString());
        prefsEditor.putString("height",height.getText().toString());
        prefsEditor.apply();
        Toast.makeText(this, getResources().getString(R.string.Saved),
                Toast.LENGTH_SHORT).show();
    }

    private void load() {
        SharedPreferences sp = getSharedPreferences("SuperBMICounterApp",Context.MODE_PRIVATE);
        mode = sp.getInt("mode",0);
        mass.setText(sp.getString("mass", null));
        height.setText(sp.getString("height", null));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setEnabled(valid);
        menu.getItem(1).setEnabled(valid);
        return super.onPrepareOptionsMenu(menu);
    }

    private void disableMenu() {
        valid = false;
        super.invalidateOptionsMenu();
    }

    private void enableMenu() {
        valid = true;
        super.invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(UNITS_MODE, mode);
    }

    private void changeUnits(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        TextView unitMass = (TextView) findViewById(R.id.unitMass);
        TextView unitHeight = (TextView) findViewById(R.id.unitHeight);
        String heightString = height.getText().toString();
        String massString = mass.getText().toString();
        switch(view.getId()) {
            case R.id.Metrical:
                unitMass.setText(R.string.UnitsMassMetrical);
                unitHeight.setText(R.string.UnitsHeightMetrical);
                if (checked && mode != 1) {
                    mode = 1;
                    if (!heightString.equals("")) {
                        float oldHeight = Float.parseFloat(heightString);
                        float newHeight = oldHeight / 3.28f;
                        height.setText(String.format("%.2f",newHeight));
                    }
                    if (!massString.equals("")) {
                        float oldMass = Float.parseFloat(massString);
                        float newMass = oldMass * 6.35f;
                        mass.setText(String.format("%.2f",newMass));
                    }
                }
                break;
            case R.id.Imperial:
                unitMass.setText(R.string.UnitsMassImperial);
                unitHeight.setText(R.string.UnitsHeightImperial);
                if (checked && mode != 2) {
                    mode = 2;
                    if (!heightString.equals("")) {
                        float oldHeight = Float.parseFloat(heightString);
                        float newHeight = oldHeight * 3.28f;
                        height.setText(String.format("%.2f",newHeight));
                    }
                    if (!massString .equals("")) {
                        float oldMass = Float.parseFloat(massString);
                        float newMass = oldMass / 6.35f;
                        mass.setText(String.format("%.2f",newMass));
                    }
                }
                break;
        }
        if (!heightString.equals("") && !massString .equals("")) {
            countBMI();
        }
    }

    private void countBMI() {
        String heightString = height.getText().toString();
        String massString = mass.getText().toString();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (!heightString.equals("") && !massString.equals("")) {
            try {
                float BMI;
                if (buttons.getCheckedRadioButtonId() == R.id.Metrical) {
                    CountBMIForMetric Counter = new CountBMIForMetric();
                    BMI = Counter.countBMI(Float.parseFloat(massString), Float.parseFloat(heightString));
                } else {
                    CountBMIForImperial Counter = new CountBMIForImperial();
                    BMI = Counter.countBMI(Float.parseFloat(massString), Float.parseFloat(heightString));
                }
                TextView output = (TextView) findViewById(R.id.Output);
                TextView output2 = (TextView) findViewById(R.id.Output2);
                output.setText(String.format("%.2f",BMI));
                output2.setText(getTextValue(BMI, output));
                mass.setError(null);
                height.setError(null);
                enableMenu();
            } catch (IllegalArgumentException e) {
                Log.e("stack",e.toString());
                disableMenu();
                TextView output = (TextView) findViewById(R.id.Output);
                TextView output2 = (TextView) findViewById(R.id.Output2);
                output.setText(null);
                output2.setText(null);
                if (buttons.getCheckedRadioButtonId() == R.id.Metrical) {
                    if (e.toString().contains("Mass")) {
                        mass.setError(getResources().getString(R.string.MetricalMassError));
                    }
                    if (e.toString().contains("Height")) {
                        height.setError(getResources().getString(R.string.MetricalHeightError));
                    }
                }
                else {
                    if (e.toString().contains("Mass")) {
                        mass.setError(getResources().getString(R.string.ImperialMassError));
                    }
                    if (e.toString().contains("Height")) {
                        height.setError(getResources().getString(R.string.ImperialHeightError));
                    }
                }
            }
        }
        else {
            if (buttons.getCheckedRadioButtonId() == R.id.Metrical) {
                if (heightString.equals("")) {
                    height.setError(getResources().getString(R.string.MetricalHeightError));
                }
                if (massString.equals("")) {
                    mass.setError(getResources().getString(R.string.MetricalMassError));
                }
            } else {
                if (heightString.equals("")) {
                    height.setError(getResources().getString(R.string.ImperialHeightError));
                }
                if (massString.equals("")) {
                    mass.setError(getResources().getString(R.string.ImperialMassError));
                }
            }
        }
    }

    private String getTextValue(float BMI, TextView text) {
        if (BMI < 16.0) {
            text.setTextColor(getResources().getColor(R.color.c3));
            return getResources().getString(R.string.BMIText1);
        }
        if (BMI < 16.99) {
            text.setTextColor(getResources().getColor(R.color.c2));
            return getResources().getString(R.string.BMIText2);
        }
        if (BMI < 18.49) {
            text.setTextColor(getResources().getColor(R.color.c1));
            return getResources().getString(R.string.BMIText3);
        }
        if (BMI < 24.99) {
            text.setTextColor(getResources().getColor(R.color.c0));
            return getResources().getString(R.string.BMIText4);
        }
        if (BMI < 29.99) {
            text.setTextColor(getResources().getColor(R.color.c1));
            return getResources().getString(R.string.BMIText5);
        }
        if (BMI < 34.99) {
            text.setTextColor(getResources().getColor(R.color.c2));
            return getResources().getString(R.string.BMIText6);
        }
        if (BMI < 39.99) {
            text.setTextColor(getResources().getColor(R.color.c3));
            return getResources().getString(R.string.BMIText7);
        }
        text.setTextColor(getResources().getColor(R.color.c4));
        return getResources().getString(R.string.BMIText8);
    }
}
