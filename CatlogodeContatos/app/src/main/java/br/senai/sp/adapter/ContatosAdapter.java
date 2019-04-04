package br.senai.sp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import br.senai.sp.catlogodecontatos.R;
import br.senai.sp.conversores.Imagem;
import br.senai.sp.modelo.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends BaseAdapter{

    private List<Contato> contatos;
    private Context context;

    public ContatosAdapter(Context context, List<Contato> contatos){
        this.contatos = contatos;
        this.context = context;

    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Contato contato = contatos.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_lista_contatos, null);

        TextView txtTelefone = view.findViewById(R.id.txt_telefone);
        txtTelefone.setText(contato.getTelefone());

        TextView txtNome = view.findViewById(R.id.txt_nome);
        txtNome.setText(contato.getNome());

        CircleImageView fotoContato = view.findViewById(R.id.image_contato);
        ImageView fotoDetalhes = view.findViewById(R.id.image_detalhes);
        ImageView fotoTelefone = view.findViewById(R.id.image_ligar);


        fotoContato.setImageBitmap(Imagem.arrayToBitmap(contato.getFoto()));
        return view;
    }

}
