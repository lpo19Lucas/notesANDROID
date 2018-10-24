package uninove.br.loginwebservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class LoginSucesso extends AppCompatActivity {
    TextView nome;
    Button btVoltar;
    ListView listaContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_sucesso);

        // Configura a barra superior para apresentar um botão de voltar (<-)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Lista de Contatos");

        Intent it = getIntent();
        Bundle params = it.getExtras();
        final ArrayList<Contato> contatos = (ArrayList<Contato>) params.getSerializable("objContatos");
        ArrayList<String> lstContatos = new ArrayList<String>();

        for (Contato contato : contatos) {
            lstContatos.add(contato.getTitle());
        }
        Collections.sort(lstContatos);

        final ArrayAdapter<String> meuAdapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_expandable_list_item_1,
                        lstContatos
                );

        listaContatos = (ListView) findViewById(R.id.listaContatos);
        listaContatos.setAdapter(meuAdapter);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelecionado = (String) listaContatos.getItemAtPosition(i);

                Intent it = new Intent(LoginSucesso.this, Detalhe.class);
                Bundle b = new Bundle();

                b.putString("nome", itemSelecionado);
                b.putSerializable("objContatos", contatos);

                it.putExtras(b);
                startActivity(it);
            }
        });

    }

    // Método para finalizar a aplicação
    @Override
    public void finish() {
        System.runFinalizersOnExit(true);
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // Botão na ActionBar (Barra Superior)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
