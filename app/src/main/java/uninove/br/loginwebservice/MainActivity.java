package uninove.br.loginwebservice;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button btLogin;
    ArrayList<Contato> anotacoes = new ArrayList<Contato>();
    private Mensagem msg = new Mensagem(MainActivity.this);

    // Vaiável para gerar uma menssagem de carregamento
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btLogin = (Button) findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessWebService();
            }
        });

    }

    class DownloadDados extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            load = ProgressDialog.show(MainActivity.this, "Aguarde", "Recuperando anotações...");
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;

            try {
                URL url = new URL("https://serene-meadow-32620.herokuapp.com/api/notes");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                result = inputStreamToString(inputStream);

                return result;

            } catch (Exception e) {
                e.printStackTrace();

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }

        private String inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();

            InputStreamReader isr = new InputStreamReader(is);

            BufferedReader rd = new BufferedReader(isr);

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer.toString();
        }


        @Override
        protected void onPostExecute(String dados) {
            super.onPostExecute(dados);
            try {

                JSONArray jsonArray = new JSONArray(dados);

                Log.d("dados", String.valueOf(jsonArray));

                  // Limpa o ArrayList
                anotacoes.clear();

//                    // Percorre os campos do array e recupera os valores
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Contato anotacao = new Contato();
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        anotacao.setId(Integer.parseInt(jsonObject.getString("id")));
                        anotacao.setTitle(jsonObject.getString("title"));
                        anotacao.setContent(jsonObject.getString("content"));
                        anotacao.setCreatedAt(jsonObject.getString("createdAt"));
                        anotacao.setUpdatedAt(jsonObject.getString("updatedAt"));
                        anotacoes.add(anotacao);
                    }

                    Intent it = new Intent(MainActivity.this, LoginSucesso.class);
                    Bundle b = new Bundle();
                    b.putSerializable("objContatos", anotacoes);
                    it.putExtras(b);
                    startActivity(it);
                    finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica a conexão com o Webservice
     */
    public void accessWebService() {
        if (testaConexao()) {
            new DownloadDados().execute();
            msg.show("Status da Conexão", "Possue acessso a Rede", "Aviso");

        } else {
            msg.show("Status da Conexão", "Sem acessso a rede", "Aviso");
        }
    }

    /**
     * Verifica se a rede está disponível
     *
     * @return
     */
    public boolean testaConexao() {
        // Flag para o status da conexao
        Boolean isInternetPresent = false;

        // Classe para detecção da conexão
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        // Retorna o status da conexão
        isInternetPresent = cd.isConnectingToInternet();

        // Verifica a conexãos
        if (!isInternetPresent) {
            return false;
        } else {
            return true;
        }
    }
}

