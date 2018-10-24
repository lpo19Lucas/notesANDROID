package uninove.br.loginwebservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class Detalhe extends AppCompatActivity {
    TextView nome, email, telefone;
    Button btVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe);

        // Configura a barra superior para apresentar um botão de voltar (<-)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Detalhes do Contato");

        Intent it = getIntent();
        Bundle params = it.getExtras();
        ArrayList<Contato> contatos = (ArrayList<Contato>) params.getSerializable("objContatos");

        String itemProcurado = params.getString("nome");

        for (Contato contato : contatos) {
            if (contato.getTitle().equals(itemProcurado)) {
                nome = (TextView) findViewById(R.id.nome);
                email = (TextView) findViewById(R.id.email);
                telefone = (TextView) findViewById(R.id.telefone);

                nome.setText(contato.getTitle());
                email.setText(contato.getContent());
//                telefone.setText(contato.getTelefone());
            }
        }

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