package eus.ehu.ane.tta.ejemplo.presentacion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import eus.ehu.ane.tta.ejemplo.R;

import android.content.Intent;
import android.widget.TextView;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    public final static String EXTRA_DNI = "dni";
    public final static String EXTRA_PASSWD="passwd";
    public final static String EXTRA_USER_ID="id";
    String dni;
    String passwd;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Obtengo el login del intent que ha lanzado mi actividad
        Intent intent=getIntent();
        dni=intent.getStringExtra(LoginActivity.EXTRA_DNI);
        passwd=intent.getStringExtra(LoginActivity.EXTRA_PASSWD);
        id=intent.getStringExtra(LoginActivity.EXTRA_USER_ID);
        //Obtengo donde quiero visualizarlo en mi actividad
        TextView textLogin=(TextView)findViewById(R.id.menu_login);
        TextView textLeccion=(TextView)findViewById(R.id.menu_leccion);
        //Visualizo el login obtenido mediante el intent que se guarda en la variable declarada en la LoginActivity
        textLogin.setText("Bienvenid@ "+intent.getStringExtra(LoginActivity.EXTRA_LOGIN));
        textLeccion.setText("Lección "+intent.getStringExtra(LoginActivity.EXTRA_LESSON_NUMBER)+": "+intent.getStringExtra(LoginActivity.EXTRA_LESSON_TITLE));
    }

    public void test(View view)
    {
        Intent intent = new Intent(this,TestActivity.class);
        intent.putExtra(MenuActivity.EXTRA_DNI, dni);
        intent.putExtra(MenuActivity.EXTRA_PASSWD, passwd);
        intent.putExtra(MenuActivity.EXTRA_USER_ID, id);
        startActivity(intent);
    }

    public void ejercicio(View view)
    {
        Intent intent=new Intent(this,ExerciseActivity.class);
        intent.putExtra(MenuActivity.EXTRA_DNI, dni);
        intent.putExtra(MenuActivity.EXTRA_PASSWD, passwd);
        startActivity(intent);
    }
}
