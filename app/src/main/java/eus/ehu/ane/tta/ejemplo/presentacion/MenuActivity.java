package eus.ehu.ane.tta.ejemplo.presentacion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eus.ehu.ane.tta.ejemplo.R;

import android.content.Intent;
import android.widget.TextView;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Obtengo el login del intent que ha lanzado mi actividad
        Intent intent=getIntent();
        //Obtengo donde quiero visualizarlo en mi actividad
        TextView textLogin=(TextView)findViewById(R.id.menu_login);
        //Visualizo el login obtenido mediante el intent que se guarda en la variable declarada en la LoginActivity
        textLogin.setText("Bienvenid@ "+intent.getStringExtra(LoginActivity.EXTRA_LOGIN));
    }

    public void test(View view)
    {
        Intent intent = new Intent(this,TestActivity.class);
        startActivity(intent);
    }

    public void ejercicio(View view)
    {
        Intent intent=new Intent(this,ExerciseActivity.class);
        startActivity(intent);
    }
}
