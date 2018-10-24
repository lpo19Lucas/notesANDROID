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

    EditText usuario, senha;
    Button btLogin;
    ArrayList<Contato> contato = new ArrayList<Contato>();
    private Mensagem msg = new Mensagem(MainActivity.this);

    // Vaiável para gerar uma menssagem de carregamento
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        usuario = (EditText) findViewById(R.id.usuario);
//        senha = (EditText) findViewById(R.id.senha);
        btLogin = (Button) findViewById(R.id.btLogin);
//
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
            load = ProgressDialog.show(MainActivity.this, "Aguarde", "Recuperando contatos...");
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://localhost:8080/api/notes");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String linha;
                StringBuffer buffer = new StringBuffer();
                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }

                return buffer.toString();

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

        @Override
        protected void onPostExecute(String dados) {
            super.onPostExecute(dados);
            try {
                JSONObject jsonObject = new JSONObject(dados);
//                String contatos = jsonObject.getString("contatos");

                Log.d("dados", dados);
//
//                if (contatos.equals("-1")) {
//                    AlertDialog.Builder builder = new AlertDialog.
//                            Builder(MainActivity.this);
//                    builder.setTitle("AVISO");
//                    builder.setMessage("Erro no Login");
//                    builder.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                }
//                            });
//                    builder.create().show();
//                } else {
                    JSONArray jsonArray = new JSONArray(dados);

                    // Limpa o ArrayList
                    contato.clear();

                    // Percorre os campos do array e recupera os valores
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Contato a = new Contato();
                        jsonObject = new JSONObject(jsonArray.getString(i));
                        a.setId(Integer.parseInt(jsonObject.getString("id")));
                        a.setTitle(jsonObject.getString("title"));
                        a.setContent(jsonObject.getString("content"));
//                        a.setTelefone(jsonObject.getString("telefone"));
                        contato.add(a);
                    }

                    Intent it = new Intent(MainActivity.this, LoginSucesso.class);
                    Bundle b = new Bundle();
                    b.putSerializable("objContatos", contato);
                    it.putExtras(b);
                    startActivity(it);
                    finish();
//                }
                load.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica a conexão com o Webservice
     */
    public void accessWebService() {
//        if (testaConexao()) {
            new DownloadDados().execute();

//        } else {
//            msg.show("Status da Conexão", "Sem acessso a rede", "Aviso");
//        }
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

