package eus.ehu.ane.tta.ejemplo.presentacion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import eus.ehu.ane.tta.ejemplo.R;

public class TestActivity extends AppCompatActivity {

    String pregunta="¿Cuál de las siguientes opciones NO se indica en el fichero de manifiesto?";
    String[] respuestas={"Versión de la aplicación","Listado de componentes de la aplicación","Opciones del menu de ajustes","Nivel mínimo de la API Android requerida","Nombre del paquete java de la aplicación"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
        TextView pregunta_texto=(TextView)findViewById(R.id.pregunta_test);
        //Poner el titulo de la pregunta en el test
        pregunta_texto.setText(pregunta);
        int i;
        for(i=0;i<respuestas.length;i++)
        {
            //Creao los botones radio en esta actividad
            RadioButton radio=new RadioButton(this);
            group.addView(radio);
            radio.setText(respuestas[i]);
            //Visualizar el boton si clickan en algun radio button
            radio.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
                }

            });
        }
    }
}
