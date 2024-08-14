package ca.brocku.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity{

    Calculator c;


    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("calcObj", c);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //c = new Calculator();

        if (savedInstanceState != null) {
            c = (Calculator)savedInstanceState.getSerializable("calcObj");
            ((TextView)findViewById(R.id.output)).setText(c.getOutput());
        }else{
            c = new Calculator();
        }


        final ToggleButton toggle=(ToggleButton)findViewById(R.id.toggle);
        toggle.setOnClickListener(view -> {

                if (toggle.isChecked()) {
                    c.switchMode(true);
                } else {
                    c.switchMode(false);
                }
            ((TextView)findViewById(R.id.output)).setText("0");
        });



        findViewById(R.id.clearAll).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.reset()));

        findViewById(R.id.clear).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.back()));

        findViewById(R.id.openBracket).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperator('(')));

        findViewById(R.id.closeBracket).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperator(')')));

        findViewById(R.id.div).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperator('/')));

        findViewById(R.id.mult).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperator('*')));

        findViewById(R.id.minus).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperator('-')));

        findViewById(R.id.plus).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperator('+')));

        findViewById(R.id.equal).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.calculate()));


        findViewById(R.id.dec).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('.')));

        findViewById(R.id.num_0).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('0')));

        findViewById(R.id.num_1).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('1')));

        findViewById(R.id.num_2).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('2')));

        findViewById(R.id.num_3).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('3')));

        findViewById(R.id.num_4).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('4')));

        findViewById(R.id.num_5).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('5')));

        findViewById(R.id.num_6).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('6')));

        findViewById(R.id.num_7).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('7')));

        findViewById(R.id.num_8).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('8')));

        findViewById(R.id.num_9).setOnClickListener(view -> ((TextView)findViewById(R.id.output)).setText(c.addOperand('9')));

    }




}