package eus.ehu.ane.tta.ejemplo.presentacion;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.database.Cursor;

import eus.ehu.ane.tta.ejemplo.R;
import eus.ehu.ane.tta.ejemplo.modelo.Exercise;
import eus.ehu.ane.tta.ejemplo.modelo.RestClient;

import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ExerciseActivity extends AppCompatActivity {

    //String preguntaEjercicio="Explica cómo aplicarías el patrón de diseño MVP en el desarrollo de una app para Android";
    private final static int READ_REQUEST_CODE=0;
    private final static int PICTURE_REQUEST_CODE=1;
    private final static int AUDIO_REQUEST_CODE=2;
    private final static int VIDEO_REQUEST_CODE=3;
    private final static String TAG="tag";
    private Uri pictureUri;
    String dni;
    String passwd;
    String idUsuario;
    String fileName;
    String path;
    int resultadoGuardarFichero;
    String visualizar;

    RestClient restClient=new RestClient("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
    int id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent=getIntent();
        dni=intent.getStringExtra(MenuActivity.EXTRA_DNI);
        passwd=intent.getStringExtra(MenuActivity.EXTRA_PASSWD);
        idUsuario=intent.getStringExtra(MenuActivity.EXTRA_USER_ID);
        //TextView pregunta=(TextView)findViewById(R.id.exercise_wording);
        //pregunta.setText(preguntaEjercicio);

        new AsyncTask<Void,Void,Void>()
        {
            private Exercise exercise;
            @Override
            protected Void doInBackground(Void... voids)
            {
                try{
                    restClient.setHttpBasicAuth(dni,passwd);
                    JSONObject json=restClient.getJson(String.format("getExercise?id=%s",id));
                    exercise=new Exercise();
                    exercise.setWording(json.getString("wording"));
                    //JSONObject lessonObject=json.getJSONObject("lessonBean");
                    //exercise.getLessonBean().setNumber(lessonObject.getInt("number"));
                    //exercise.getLessonBean().setTitle(lessonObject.getString("title"));
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
                TextView ejercicioEnunciado=findViewById(R.id.exercise_wording);
                ejercicioEnunciado.setText(exercise.getWording());
            }
        }.execute();
    }

    public void subirFichero(View view)
    {
        Intent intent =new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //Puede seleccionar cualquier tipo de documento
        intent.setType("*/*");
        //Recuperar el resultado (de la seleccion del fichero)
        //READ_REQUEST_CODE --> Identificador numerico del resultado para diferenciarlo cuando se recupere
        startActivityForResult(intent,READ_REQUEST_CODE);
        //Como resultado de como se ha realizado la ejecucion se devuelve (Ambos existen por defecto): RESULT_OK o RESULT_CANCEL
    }
    public void sacarFoto(View view)
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Toast.makeText(this,R.string.no_camera,Toast.LENGTH_SHORT).show();
        }else
        {
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null)
            {
                //Obtengo el path
                File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                try
                {
                    File file=File.createTempFile("tta",".jpg",dir);
                    pictureUri= Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
                    startActivityForResult(intent,PICTURE_REQUEST_CODE);
                }catch (IOException es)
                {
                    es.printStackTrace();
                }
            }else
            {
                Toast.makeText(this,R.string.no_app,Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void grabarAudio(View view)
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
        {
            Toast.makeText(this,R.string.no_micro,Toast.LENGTH_SHORT).show();
        }else
        {
            Intent intent=new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            if(intent.resolveActivity(getPackageManager())!=null)
            {
                startActivityForResult(intent,AUDIO_REQUEST_CODE);
            }else
            {
                Toast.makeText(this,R.string.no_app,Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void grabarVideo(View view)
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Toast.makeText(this,R.string.no_camera,Toast.LENGTH_SHORT).show();
        }else
        {
            Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null)
            {
                startActivityForResult(intent,VIDEO_REQUEST_CODE);
            }else
            {
                Toast.makeText(this,R.string.no_app,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Si el resultado de la ejecucion (Subir fichero, sacar foto, grabar video u audio) no es exitoso
        if(resultCode!= Activity.RESULT_OK)
        {
            return;
        }
        else
        {
            switch(requestCode)
            {
                case READ_REQUEST_CODE:
                    Uri uri = data.getData();
                    //Log.i(TAG, "Uri: " + uri.toString());
                    fileName=consultaMetadatos(uri);
                    path=data.getData().getPath();
                    //Toast.makeText(this, path,Toast.LENGTH_LONG).show();
                    uploadFile(fileName,uri,path);
                    break;
                case PICTURE_REQUEST_CODE:
                    //subirFichero(pictureUri);
                    break;
                case AUDIO_REQUEST_CODE:
                    break;
                case VIDEO_REQUEST_CODE:
                    break;
            }
        }
    }

    public String consultaMetadatos(Uri uri)
    {
        Cursor cursor=getContentResolver().query(uri,null,null,null,null,null);
        String displayName ="";
        try
        {
            if(cursor!=null && cursor.moveToFirst())
            {
                displayName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG,"Display Name: "+displayName);

                int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
                String size=null;
                if(!cursor.isNull(sizeIndex))
                    size=cursor.getString(sizeIndex);
                else
                    size="Unknown";
                Log.i(TAG,"Size: "+size);
            }
        }finally {
            cursor.close();
        }
        return displayName;
    }
    public void uploadFile(final String fileName, final Uri uri, final String path)
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                try{
                    restClient.setHttpBasicAuth(dni,passwd);
                    InputStream inputStream=getContentResolver().openInputStream(uri);
                    //visualizar=String.format("postExercise?user=%s",idUsuario+"&id=1");
                    resultadoGuardarFichero=restClient.postFile(String.format("postExercise?user=%s",idUsuario+"&id=1"),inputStream,fileName);
                    //resultadoGuardarFichero=restClient.postFile(path,inputStream,fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //int visualizar=resultadoGuardarFichero;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                Toast.makeText(getApplicationContext(), R.string.fichero_subido,Toast.LENGTH_LONG).show();
            }
        }.execute();
    }
}
