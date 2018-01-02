package eus.ehu.ane.tta.ejemplo.presentacion;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebView;

import eus.ehu.ane.tta.ejemplo.R;

import static eus.ehu.ane.tta.ejemplo.R.id.alertTitle;
import static eus.ehu.ane.tta.ejemplo.R.id.button_send_help;
import static eus.ehu.ane.tta.ejemplo.R.id.button_send_test;
import android.net.Uri;
import android.content.Intent;
import android.widget.VideoView;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    String pregunta="¿Cuál de las siguientes opciones NO se indica en el fichero de manifiesto?";
    String[] respuestas={"Versión de la aplicación","Listado de componentes de la aplicación","Opciones del menu de ajustes","Nivel mínimo de la API Android requerida","Nombre del paquete java de la aplicación"};
    String adviseHTML="<html><body>The manifest describes the <b>componentes of the applicaction:</b> the activities, services, broadcast receivers and content providers that ...</body></html>";
    String URLaudioVideo="http://u017633.ehu.eus:28080/static/ServidorTta/AndroidManifest.mp4";

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

        //Ponemos la opcion correcta en verde
        group.getChildAt(correct).setBackgroundColor(Color.GREEN);
        //Obtenemos la opcion seleccionada
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

    public void ayuda(View view)
    {
        RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
        int idRadioButton = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
        //String cadena=Integer.toString(idRadioButton);
        //Toast.makeText(this,cadena,Toast.LENGTH_SHORT).show();
        switch(idRadioButton)
        {
            case 0:
                showHTML(URLaudioVideo);
                break;
            case 1:
                showHTML(adviseHTML);
                break;
            case 3:
                showVideo(URLaudioVideo);
                break;
            case 4:
                try {
                    showAudio(URLaudioVideo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showHTML(String advise)
    {
        //Condicion pasa saber si es una pagina web a visualizar
        if(advise.substring(0,10).contains("://"))
        {
            Uri uri=Uri.parse(advise);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        }else
        {
            WebView web=new WebView(this);
            ViewGroup layout=(ViewGroup)findViewById(R.id.test_layout);
            //web.loadData(advise,"text/html",null);
            web.loadData(advise,"text/html",null);
            web.setBackgroundColor(Color.TRANSPARENT);
            web.setLayerType(WebView.LAYER_TYPE_SOFTWARE,null);
            layout.addView(web);
        }
    }

    private void showVideo(String advise)
    {
        //Creando desde el codigo donde se va a visualizar en el layout
        VideoView video=new VideoView(this);
        ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        video.setLayoutParams(params);
        //Asignamos la URL al layout creado
        video.setVideoURI(Uri.parse(advise));

        //Para que no se creen los botones flotantes
        MediaController controller=new MediaController(this)
        {
            @Override
            public void hide()
            {

            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event)
            {
                if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
                {
                    finish();

                }
                return super.dispatchKeyEvent(event);
            }
        };
        controller.setAnchorView(video);
        video.setMediaController(controller);

        ViewGroup layout=(ViewGroup)findViewById(R.id.test_layout);
        layout.addView(video);
        //Visuailiza directamente el video
        video.start();
    }

    private void showAudio(String advise)throws IOException
    {
        View view=new View(this);
        AudioPlayer audio=new AudioPlayer(view);
        ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        audio.setAudioUri(Uri.parse(advise));

        ViewGroup layout=(ViewGroup)findViewById(R.id.test_layout);
        layout.addView(view);
        audio.start();
    }
}
