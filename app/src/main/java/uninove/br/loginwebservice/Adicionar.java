package uninove.br.loginwebservice;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Adicionar extends AppCompatActivity{

        EditText titulo, conteudo;
        Button inserir;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.adicionarr);

            Intent it = getIntent();
            // Configura a barra superior para apresentar um botão de voltar (<-)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setTitle("Detalhes da Anotação");



            titulo = (EditText) findViewById(R.id.titulo);
            conteudo = (EditText) findViewById(R.id.conteudo);
            inserir = (Button) findViewById(R.id.inserir);
            
            inserir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetworkConnection();
                    
                }
            });


    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            Toast.makeText(this, "Conectado" + networkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sem Acesso a Rede", Toast.LENGTH_SHORT).show();
        }

        return isConnected;
    }


    private String httpPost(String myUrl) throws IOException, JSONException {

        URL url = new URL(myUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        JSONObject jsonObject = buidJsonObject();

        setPostRequestContent(conn, jsonObject);

        conn.connect();

        Log.i("Retorno: ", conn.getResponseMessage().toUpperCase());
        return conn.getResponseMessage().toUpperCase();

    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                try {
                    return httpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Erro!";
                }
            } catch (IOException e) {
                return "Sem acesso a página";
            }
        }

        @Override
        protected void onPostExecute(String result) {
//            tvResult.setText(result);
//            tvResult.setText("Retorno: " + result);
        }
    }


    public void send(View view) {
        // faz o HTTP POST request
        if (checkNetworkConnection())
            new HTTPAsyncTask().execute("https://serene-meadow-32620.herokuapp.com/api/notes");

        else
            Toast.makeText(this, "Sem Conexão!", Toast.LENGTH_SHORT).show();

    }

    private JSONObject buidJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("title", titulo.getText().toString());
        jsonObject.accumulate("content", conteudo.getText().toString());

        return jsonObject;
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }


//        Botão na ActionBar (Barra Superior)
    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
}

