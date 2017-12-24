package eus.ehu.ane.tta.ejemplo.presentacion;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebView;

import eus.ehu.ane.tta.ejemplo.R;

import static eus.ehu.ane.tta.ejemplo.R.id.button_send_help;
import static eus.ehu.ane.tta.ejemplo.R.id.button_send_test;

public class TestActivity extends AppCompatActivity {

    String pregunta="¿Cuál de las siguientes opciones NO se indica en el fichero de manifiesto?";
    String[] respuestas={"Versión de la aplicación","Listado de componentes de la aplicación","Opciones del menu de ajustes","Nivel mínimo de la API Android requerida","Nombre del paquete java de la aplicación"};
    String advise="<html><body>The manifest describes the <b>componentes of the applicaction:</b> the activities, services, broadcast receivers and content providers that ...</body></html>";

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
                    findViewById(button_send_test).setVisibility(View.VISIBLE);
                }

            });
        }
    }

    public void comprobarTest(View view)
    {
        int correct=2;
        RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
        //Quitamos el boton de ENVIAR
        ViewGroup layout=(ViewGroup)findViewById(R.id.test_layout);
        layout.removeView(findViewById(button_send_test));

        group.getChildAt(correct).setBackgroundColor(Color.GREEN);
        int selected=group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
        if(selected!=correct)
        {
            group.getChildAt(selected).setBackgroundColor(Color.RED);
            Toast.makeText(this,R.string.test_opcion_erronea,Toast.LENGTH_LONG).show();
            findViewById(button_send_help).setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(this,R.string.test_opcion_correcta,Toast.LENGTH_LONG).show();
        }
    }

    public void textoAyuda(View view)
    {
        WebView web=new WebView(this);
        ViewGroup layout=(ViewGroup)findViewById(R.id.test_layout);
        web.loadData(advise,"text/html",null);
        web.setBackgroundColor(Color.TRANSPARENT);
        web.setLayerType(WebView.LAYER_TYPE_SOFTWARE,null);
        layout.addView(web);
    }
}
