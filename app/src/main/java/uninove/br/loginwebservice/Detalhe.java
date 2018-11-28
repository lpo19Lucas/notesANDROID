package uninove.br.loginwebservice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Detalhe extends AppCompatActivity {
    EditText titulo, conteudo, id;
    Button excluir, salvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe);

        // Configura a barra superior para apresentar um botão de voltar (<-)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Detalhes da Anotação");

        Intent it = getIntent();
        Bundle params = it.getExtras();
        ArrayList<Contato> contatos = (ArrayList<Contato>) params.getSerializable("objContatos");

        String itemProcurado = params.getString("nome");


        for (Contato contato : contatos) {
            if (contato.getTitle().equals(itemProcurado)) {
                titulo = (EditText) findViewById(R.id.titulo2);
                conteudo = (EditText) findViewById(R.id.conteudo2);
                id = (EditText) findViewById(R.id.id);
//                id = (TextView) findViewById(R.id.id);
//                criado = (TextView) findViewById(R.id.criado);
//                atualizado = (TextView) findViewById(R.id.atualizado);

                titulo.setText(contato.getTitle());
                conteudo.setText(contato.getContent());
                id.setText(contato.getId());
//                email.setText(contato.getContent());
//                criado.setText(contato.getCreatedAt());
//                atualizado.setText(contato.getUpdatedAt());
            }
        }

        salvar = (Button) findViewById(R.id.salvar);
        excluir = (Button) findViewById(R.id.salvar);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkNetworkConnection();
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        if(salvar.performClick()){
            conn.setRequestMethod("PUT");
        }

        if(excluir.performClick()){
            conn.setRequestMethod("DELETE");
        }

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

        if(salvar.performClick()){
            jsonObject.accumulate("id", id.getText());
            jsonObject.accumulate("title", titulo.getText().toString());
            jsonObject.accumulate("content", conteudo.getText().toString());
        }

        if(excluir.performClick()){
            jsonObject.accumulate("id", id.getText());
        }

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

    // Botão na ActionBar (Barra Superior)
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
