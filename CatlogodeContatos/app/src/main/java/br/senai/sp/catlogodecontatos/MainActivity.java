package br.senai.sp.catlogodecontatos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.senai.sp.adapter.ContatosAdapter;
import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class MainActivity extends AppCompatActivity {

   private ListView listaContatos;
   private ImageButton btnNovoContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //*** Associa o objeto ListView à View ListView do layout xml
        listaContatos = findViewById(R.id.list_contatos);

        btnNovoContato = findViewById(R.id.bt_novo_contato);


        //*** Ação do botão novo
        btnNovoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastroContato = new Intent(MainActivity.this,CadastroContatoActivity.class);
                startActivity(cadastroContato);


            }
        });


        // Definição de um menu de contexto para a listview(lista de filmes)

        registerForContextMenu(listaContatos);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);
                /*Intenção de abrir a cadastro de filmes*/
                Intent cadastro = new Intent(MainActivity.this, CadastroContatoActivity.class);
                /*leva o contato*/
                cadastro.putExtra("contato", contato);
                startActivity(cadastro);

            }
        });



    }





    //registerForContextMenu chama esse método
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_context_lista_contatos, menu);


       /* MenuItem deletar = menu.add("Excluir");
        MenuItem editar = menu.add("Editar");
        //detecta qual item do menu foi clicado
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, "DELETAR", Toast.LENGTH_LONG).show();
                return false;
            }
        });*/


        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

            /*detecta o item no menu do contexto*/
    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        /* Caixa de diálogo que confirma exclusão */

        new AlertDialog.Builder(this).setTitle("Excluir contato").setMessage("Tem certeza que deseja excluir o contato?").setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                        Contato contato = (Contato) listaContatos.getItemAtPosition(info.position);
                        ContatoDAO dao = new ContatoDAO(MainActivity.this);
                        dao.excluir(contato);
                        Toast.makeText(MainActivity.this, contato.getNome() + " excluído!", Toast.LENGTH_LONG).show();
                        dao.close();
                        carregarLista();
                    }
                }).setNegativeButton("Não", null).show();

        return super.onContextItemSelected(item);

    }

    @Override
    protected void onResume() {
        carregarLista();
        super.onResume();
    }

    private void carregarLista(){

        // abrir banco de dados, rodar uma query de consulta, retornar um arraylist

        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.getContatos();
        dao.close();

        //*** Definimos um adapter para carregar os dados da ArrayList na ListView utilizando um layout pronto
        //ArrayAdapter<Contato> listaContatosAdapter = new ArrayAdapter<Contato>(this,android.R.layout.simple_list_item_1, contatos);
        ContatosAdapter adapter = new ContatosAdapter(this, contatos);
        listaContatos.setAdapter(adapter);

        //*** Injetamos o adapter no objeto ListView
        //listaContatos.setAdapter(listaContatosAdapter);
    }
}
