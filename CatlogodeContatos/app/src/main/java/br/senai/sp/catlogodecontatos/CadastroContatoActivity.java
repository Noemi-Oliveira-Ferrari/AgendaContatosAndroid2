package br.senai.sp.catlogodecontatos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class CadastroContatoActivity extends AppCompatActivity {

    private CadastroContatoHelper helper;
    public static final int GALERIA_REQUEST = 1;
    public static final int CAMERA_REQUEST = 2;
    private ImageButton btnCamera;
    private ImageButton btnGaleria;
    private ImageView imgFoto;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro_contato);


        /*BOTAO DE VOLTAR A TELA PRINCIPAL*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helper = new CadastroContatoHelper(CadastroContatoActivity.this);


        helper = new CadastroContatoHelper(CadastroContatoActivity.this);
        btnCamera = findViewById(R.id.btn_camera);
        btnGaleria = findViewById(R.id.btn_galeria);
        imgFoto = findViewById(R.id.image_view_contato);

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastroContatoActivity.this, "Abrindo a galeria...", Toast.LENGTH_LONG).show();
                Intent galeria = new Intent(Intent.ACTION_GET_CONTENT);
                galeria.setType("image/*");
                startActivityForResult(galeria, GALERIA_REQUEST);

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent para abrir a camera*/
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String nomeArquivo = "/IMG_" + System.currentTimeMillis() + ".jpg";
                caminhoFoto = getExternalFilesDir(null) + nomeArquivo;
                Log.d("NOME_ARQUIVO", nomeArquivo);

                File arquivoFoto = new File(caminhoFoto);
                Uri fotoUri = FileProvider.getUriForFile(
                        CadastroContatoActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        arquivoFoto

                );

                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(intentCamera, CAMERA_REQUEST);
                Toast.makeText(CadastroContatoActivity.this, "Foto capturada", Toast.LENGTH_LONG).show();
            }
        });



         /*obter intenção*/
         Intent intent = getIntent();

         Contato contato = (Contato) intent.getSerializableExtra("contato");
            if(contato != null) {
                helper.preencherFormulario(contato);
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            try {
                /*tratamento para não dar erro se o usuário não escolher nenhuma imagem*/
                if (requestCode == GALERIA_REQUEST) {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgFoto.setImageBitmap(bitmap);
                }
                if (requestCode == CAMERA_REQUEST){
                    Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    imgFoto.setImageBitmap(bitmapReduzido);

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

    }




    /*Colocar menu na activity(menu_cadastro_contatos.xmlml)*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    /*Inflador de menu, enche o menu*/
        MenuInflater menuInflater = getMenuInflater();
        /*O método inflate precisa de 2 parâmetros (o layout e o objeto menu)*/
        menuInflater.inflate(R.menu.menu_cadastro_contatos, menu);
        /*Retorna o menu pra quem chamou*/
        return super.onCreateOptionsMenu(menu);
    }


/*Notificação*/
    public void criarNotificacaoSimples(){
        int id = 1;
        String titulo = "Agenda de Contatos";
        String texto = "Um contato foi adicionado à sua lista!";
        int icone = android.R.drawable.ic_dialog_info;

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent p = getPendingIntent(id, intent, this);

        NotificationCompat.Builder notificacao = new NotificationCompat.Builder(this);
        notificacao.setSmallIcon(icone);
        notificacao.setContentTitle(titulo);
        notificacao.setContentText(texto);
        notificacao.setContentIntent(p);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(id, notificacao.build());
    }

    private PendingIntent getPendingIntent(int id, Intent intent, Context context){
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.getComponent());
        stackBuilder.addNextIntent(intent);

        PendingIntent p = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
        return p;
    }


    /*Retorna o item selecionado no menu*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      /*getItemId() traz o id do item selecionado*/
       switch (item.getItemId()){
           case R.id.menu_salvar:

                Contato contato = helper.getContato();
            //*** abrir o banco, query de insert, fechar o banco
               ContatoDAO dao = new ContatoDAO(this);

               if(contato.getId() == 0){
                    CadastroContatoHelper helper = new CadastroContatoHelper(this);
                    if(helper.validar() == true){
                        dao.salvar(contato);
                        criarNotificacaoSimples();
                        Toast.makeText(this, contato.getNome() + " gravado com sucesso!", Toast.LENGTH_LONG).show();
                        finish();

                    }else{
                        Toast.makeText(this, "Verifique os dados", Toast.LENGTH_LONG).show();
                    }

               }else{

                   if(helper.validar() == true){
                       dao.atualizar(contato);
                       Toast.makeText(this, contato.getNome() + " atualizado com sucesso!", Toast.LENGTH_LONG).show();
                       finish();

                   }else{
                       Toast.makeText(this, "Verifique os dados", Toast.LENGTH_LONG).show();
                   }

               }

               dao.close();
               break;
           case R.id.menu_del:
               Contato contatoCancelar = helper.getContato();

               if(contatoCancelar.getId() == 0){
                   Toast.makeText(this, "Nenhum contato adicionado!", Toast.LENGTH_LONG).show();
               }else {

                   final ContatoDAO daoExcluir = new ContatoDAO(this);
                   final Contato contatoExcluir = helper.getContato();
                   new AlertDialog.Builder(this).setTitle("Excluir contato").setMessage("Tem certeza que deseja excluir o contato?").setPositiveButton("Sim",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   daoExcluir.excluir(contatoExcluir);

                                   Toast.makeText(CadastroContatoActivity.this, contatoExcluir.getNome() + " excluído!", Toast.LENGTH_LONG).show();
                                   daoExcluir.close();
                                   finish();
                               }
                           }).setNegativeButton("Não", null).show();
               }
               break;
           case R.id.menu_ligar:

               Contato contatoLigar = helper.getContato();

               if(contatoLigar.getId() == 0){
                   Toast.makeText(this, "Não é possível ligar!", Toast.LENGTH_LONG).show();
               }else {

                   Contato numeroTelefone = helper.getContato();
                   Uri uri = Uri.parse("tel:" + numeroTelefone.getTelefone());
                   Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                   startActivity(intent);
               }
               break;
           case android.R.id.home:
               /*CHAMADA DA MAIN QUANDO CLICA NO BOTÃO DE VOLTAR*/
               finish();
               break;
           default:
                   Toast.makeText(CadastroContatoActivity.this, "Nada", Toast.LENGTH_LONG).show();
                   break;

       }


        return super.onOptionsItemSelected(item);
    }


}
