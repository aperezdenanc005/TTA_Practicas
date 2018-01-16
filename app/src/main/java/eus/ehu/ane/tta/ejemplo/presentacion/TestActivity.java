package eus.ehu.ane.tta.ejemplo.presentacion;

import android.graphics.Color;
import android.os.AsyncTask;
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
import eus.ehu.ane.tta.ejemplo.modelo.Test;
import eus.ehu.ane.tta.ejemplo.modelo.RestClient;

import static eus.ehu.ane.tta.ejemplo.R.id.alertTitle;
import static eus.ehu.ane.tta.ejemplo.R.id.button_send_help;
import static eus.ehu.ane.tta.ejemplo.R.id.button_send_test;
import static eus.ehu.ane.tta.ejemplo.R.id.test_choices;

import android.net.Uri;
import android.content.Intent;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    //String pregunta="¿Cuál de las siguientes opciones NO se indica en el fichero de manifiesto?";
    //String[] respuestas={"Versión de la aplicación","Listado de componentes de la aplicación","Opciones del menu de ajustes","Nivel mínimo de la API Android requerida","Nombre del paquete java de la aplicación"};
    String adviseHTML="<html><body>The manifest describes the <b>componentes of the applicaction:</b> the activities, services, broadcast receivers and content providers that ...</body></html>";
    String URLaudioVideo="http://u017633.ehu.eus:28080/static/ServidorTta/AndroidManifest.mp4";
    String dni;
    String passwd;
    String userID;
    int id=1;
    Test test;
    int selected;

    RestClient restClient=new RestClient("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
        //TextView pregunta_texto=(TextView)findViewById(R.id.pregunta_test);

        Intent intent=getIntent();
        dni=intent.getStringExtra(MenuActivity.EXTRA_DNI);
        passwd=intent.getStringExtra(MenuActivity.EXTRA_PASSWD);
        userID=intent.getStringExtra(MenuActivity.EXTRA_USER_ID);
        //Poner el titulo de la pregunta en el test
        //pregunta_texto.setText(pregunta);

        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                try{
                    restClient.setHttpBasicAuth(dni,passwd);
                    JSONObject json=restClient.getJson(String.format("getTest?id=%s",id));
                    test=new Test();
                    test.setWording(json.getString("wording"));
                    JSONArray array=json.getJSONArray("choices");
                    //JSONObject objetoResourceType=array.getJSONObject("resourceType");
                    for(int i=0; i<array.length();i++)
                    {
                        JSONObject item=array.getJSONObject(i);
                        Test.Choice choice=new Test.Choice();
                        choice.setId(item.getInt("id"));
                        choice.setAnswer(item.getString("answer"));
                        choice.setCorrect(item.getBoolean("correct"));
                        choice.setAdvise(item.getString("advise"));
                        //choice.setMime(item.optString("mime",null));
                        /*JSONObject objetoResourceType=item.getJSONObject("resourceType");
                        if(objetoResourceType.getString("mime").matches("null"))
                        {
                            choice.setMime("null");
                        }
                        else
                        {
                            choice.setMime(objetoResourceType.getString("mime"));
                        }*/
                        if(item.getString("resourceType").matches("null"))
                        {
                            choice.setMime("null");
                        }
                        else
                        {
                            JSONObject objetoResourceType=item.getJSONObject("resourceType");
                            choice.setMime(objetoResourceType.getString("mime"));
                        }
                        test.getChoices().add(choice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                View view;
                TextView pregunta_texto=(TextView)findViewById(R.id.pregunta_test);
                pregunta_texto.setText(test.getWording());
                for(int i=0; i<test.getChoices().size();i++)
                {
                    RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
                    RadioButton radio=new RadioButton(getApplicationContext());
                    group.addView(radio);
                    radio.setText(test.getChoices().get(i).getAnswer());
                    radio.setTextColor(Color.BLACK);
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
        }.execute();

        /*int i;
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
        }*/
    }

    public void comprobarTest(View view)
    {
        int correct=0;
        RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
        int choices=group.getChildCount();
        for(int i=0; i<choices;i++)
        {
            group.getChildAt(i).setEnabled(false);
        }

        //Ponemos cual es la opcion correcta
        for(int i=0;i<test.getChoices().size();i++)
        {
            if(test.getChoices().get(i).isCorrect()==true)
            {
                correct=i;
            }
        }

        //Quitamos el boton de ENVIAR
        ViewGroup layout=(ViewGroup)findViewById(R.id.test_layout);
        layout.removeView(findViewById(button_send_test));

        //Ponemos la opcion correcta en verde
        group.getChildAt(correct).setBackgroundColor(Color.GREEN);
        //Obtenemos la opcion seleccionada
        selected=group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
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

        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                try{
                    restClient.setHttpBasicAuth(dni,passwd);
                    JSONObject json=new JSONObject();
                    json.put("userId",userID);
                    json.put("choiceId",test.getChoices().get(selected).getId());
                    restClient.postJSON(json,"postChoice");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                Toast.makeText(getApplicationContext(), R.string.resultado_enviado,Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    public void ayuda(View view)
    {
        /*RadioGroup group=(RadioGroup)findViewById(R.id.test_choices);
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
        }*/

        String mime=test.getChoices().get(selected).getMime();
        switch(test.getChoices().get(selected).getMime())
        {
            case "text/html":
                showHTML(test.getChoices().get(selected).getAdvise());
                break;
            case "video/mp4":
                showVideo(test.getChoices().get(selected).getAdvise());
                break;
            case "audio/mpeg":
                try {
                    showAudio(test.getChoices().get(selected).getAdvise());
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
