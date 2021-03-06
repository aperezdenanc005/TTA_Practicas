package eus.ehu.ane.tta.ejemplo.presentacion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eus.ehu.ane.tta.ejemplo.R;
import eus.ehu.ane.tta.ejemplo.modelo.RestClient;
import eus.ehu.ane.tta.ejemplo.modelo.User;

import static android.Manifest.permission.READ_CONTACTS;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public final static String EXTRA_LOGIN = "login";
    public final static String EXTRA_LESSON_TITLE="title";
    public final static String EXTRA_DNI = "dni";
    public final static String EXTRA_PASSWD="passwd";
    public final static String EXTRA_USER_ID="id";
    public final static String EXTRA_LESSON_NUMBER="number";
    RestClient restClient=new RestClient("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
    }

    /*
    public void login(View view)
    {

        //Intent al que le pasamos el contexto (La acticidad actual) y la actividad a la que queremos ir
        Intent intent= new Intent(this,MenuActivity.class);
        //Obtenemos el login y el password introducidos por el usuario
        String login =((EditText)findViewById(R.id.login)).getText().toString();
        String passwd =((EditText)findViewById(R.id.passwd)).getText().toString();
        //Comprobamos que no esten vacion los campos
        if(login.matches("") || passwd.matches(""))
        {
            Toast.makeText(this,R.string.login_incorrecto,Toast.LENGTH_LONG).show();
        }else
        {
            //Se le envia a la actividad que vamos a lanzar el login introducido por el usuario
            intent.putExtra(LoginActivity.EXTRA_LOGIN,login);
            Toast.makeText(this,R.string.login_correcto,Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }*/

    public void autenticacion(View view) throws IOException
    {
        final String dni =((EditText)findViewById(R.id.login)).getText().toString();
        final String passwd =((EditText)findViewById(R.id.passwd)).getText().toString();
        new AsyncTask<Void,Void,Void>()
        {
            private User user;
            @Override
            protected Void doInBackground(Void... voids)
            {
                try{
                    restClient.setHttpBasicAuth(dni,passwd);
                    JSONObject json=restClient.getJson(String.format("getStatus?dni=%s",dni));
                    user=new User(json.getInt("id"),json.getString("user"),json.getInt("lessonNumber"),json.getString("lessonTitle"),json.getInt("nextTest"),json.getInt("nextExercise"));
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
                Intent intent= new Intent(getApplicationContext(),MenuActivity.class);
                intent.putExtra(LoginActivity.EXTRA_LOGIN,user.getUser());
                intent.putExtra(LoginActivity.EXTRA_LESSON_TITLE,user.getLessonTitle());
                int i=user.getLessonNumber();
                intent.putExtra(LoginActivity.EXTRA_LESSON_NUMBER,Integer.toString(i));
                intent.putExtra(LoginActivity.EXTRA_DNI, dni);
                intent.putExtra(LoginActivity.EXTRA_PASSWD, passwd);
                int id=user.getId();
                intent.putExtra(LoginActivity.EXTRA_USER_ID,Integer.toString(id));
                startActivity(intent);
            }
        }.execute();
    }
}

