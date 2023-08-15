package com.svalero.cybershopapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.svalero.cybershopapp.R;
import com.svalero.cybershopapp.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientHolder> {
    private List<Client> clientList;

    public ClientAdapter(List<Client> clientList) {
        this.clientList = clientList;
    }

    @Override
    public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item, parent, false);
        return new ClientHolder(view);
    }

    @Override
    public void onBindViewHolder(ClientHolder holder, int position) {
        holder.clientName.setText(clientList.get(position).getName());
        holder.clientSurname.setText(clientList.get(position).getSurname());
        holder.clientNumber.setText(String.valueOf(clientList.get(position).getNumber()));
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class ClientHolder extends RecyclerView.ViewHolder{
        public TextView clientName;
        public TextView clientSurname;
        public TextView clientNumber;
        public View parentView;

        public ClientHolder(View view){
            super(view);
            parentView = view;

            clientName = view.findViewById(R.id.clientName);
            clientSurname = view.findViewById(R.id.clientSurname);
            clientNumber = view.findViewById(R.id.clientNumber);
        }
    }
}
