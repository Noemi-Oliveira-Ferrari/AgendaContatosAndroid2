package br.senai.sp.catlogodecontatos;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import br.senai.sp.conversores.Imagem;
import br.senai.sp.modelo.Contato;

public class CadastroContatoHelper {

    private EditText txtNome;
    private EditText txtEndereco;
    private EditText txtTelefone;
    private EditText txtEmail;
    private EditText txtEnderecoLinkedin;
    private TextInputLayout layoutTxtNome;
    private TextInputLayout layoutTxtEndereco;
    private TextInputLayout layoutTxtTelefone;
    private TextInputLayout layoutTxtEmail;
    private TextInputLayout layoutTxtEnderecoLinkedin;
    private ImageView fotoContato;
    private MenuItem menu_del;

    private Contato contato;

    public CadastroContatoHelper(CadastroContatoActivity activity){

         txtNome = activity.findViewById(R.id.txt_nome);
         txtEndereco = activity.findViewById(R.id.txt_endereco);
         txtTelefone = activity.findViewById(R.id.txt_telefone);
         txtEmail = activity.findViewById(R.id.txt_email);
         txtEnderecoLinkedin = activity.findViewById(R.id.txt_endereco_linkedin);
         layoutTxtNome = activity.findViewById(R.id.layout_txt_nome);
         layoutTxtEndereco = activity.findViewById(R.id.layout_txt_endereco);
         layoutTxtTelefone = activity.findViewById(R.id.layout_txt_telefone);
         layoutTxtEmail = activity.findViewById(R.id.layout_txt_email);
         layoutTxtEnderecoLinkedin = activity.findViewById(R.id.layout_txt_endereco_linkedin);
         fotoContato = activity.findViewById(R.id.image_view_contato);

         contato = new Contato();
         //menu_del = activity.findViewById(R.id.menu_del);

    }


    public boolean validar(){

        boolean validado = true;
            if(txtNome.getText().toString().isEmpty()){
                layoutTxtNome.setErrorEnabled(true);
                layoutTxtNome.setError("Por favor digite o nome do contato");
                validado = false;
            }
            else{
                layoutTxtNome.setErrorEnabled(false);
            }
            if(txtEndereco.getText().toString().isEmpty()){
                layoutTxtEndereco.setErrorEnabled(true);
                layoutTxtEndereco.setError("Por favor digite o endereço");
                validado = false;
            }
            else{
                layoutTxtEndereco.setErrorEnabled(false);
            }
        if(txtTelefone.getText().toString().isEmpty()){
            layoutTxtTelefone.setErrorEnabled(true);
            layoutTxtTelefone.setError("Por favor digite o telefone");
            validado = false;
        }
        else{
            layoutTxtTelefone.setErrorEnabled(false);
        }
        if(txtEmail.getText().toString().isEmpty()){
            layoutTxtEmail.setErrorEnabled(true);
            layoutTxtEmail.setError("Por favor digite o e-mail");
            validado = false;
        }
        else{
            layoutTxtEmail.setErrorEnabled(false);
        }


        return validado;
    }

    public Contato getContato(){

        contato.setNome(txtNome.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setEndereco_linkedin(txtEnderecoLinkedin.getText().toString());

        Bitmap bitmap = ((BitmapDrawable)fotoContato.getDrawable()).getBitmap();
        /*reduzir o bitmap*/
        Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

        /*converter o bitmap em array de bytes*/
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapReduzido.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] fotoArray = byteArrayOutputStream.toByteArray();

        contato.setFoto(fotoArray);
        return contato;
    }


    public void preencherFormulario(Contato contato) {

        txtNome.setText(contato.getNome());
        txtEndereco.setText(contato.getEndereco());
        txtTelefone.setText(contato.getTelefone());
        txtEmail.setText(contato.getEmail());
        txtEnderecoLinkedin.setText(contato.getEndereco_linkedin());
        if(contato.getFoto() != null) {
           fotoContato.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));
        }
        this.contato = contato;

    }
}
