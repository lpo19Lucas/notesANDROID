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

            inserir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

