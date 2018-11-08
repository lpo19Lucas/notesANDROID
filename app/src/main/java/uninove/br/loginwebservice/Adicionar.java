package uninove.br.loginwebservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;




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
//
//                HttpURLConnection urlConnection = null;
//                BufferedReader reader = null;
//                String result = null;
//
//                try {
//                    URL url = new URL("https://serene-meadow-32620.herokuapp.com/api/notes");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("POST");
//                    conn.setDoOutput(true);
//                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }

                }
            });


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

