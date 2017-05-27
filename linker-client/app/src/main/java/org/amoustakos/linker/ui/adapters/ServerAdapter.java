package org.amoustakos.linker.ui.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.db.RealmManager;
import org.amoustakos.linker.io.models.Server;
import org.amoustakos.linker.ui.adapters.abs.RealmRecyclerViewAdapter;

import io.realm.Realm;

public class ServerAdapter extends RealmRecyclerViewAdapter<Server> {

    final Context context;
    final RealmManager realmManager;

    private Realm realm;
    private LayoutInflater inflater;

    public ServerAdapter(Context context, RealmManager realmManager) {
        this.context = context;
        this.realmManager = realmManager;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_server, parent, false);
        return new ServerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ServerViewHolder) holder).loadItem(getItem(position));
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null)
            return getRealmAdapter().getCount();
        return 0;
    }





    public static class ServerViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        TextView name;
        TextView descr;
        Server item;

        public ServerViewHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.card_view_container);
            name = (TextView) itemView.findViewById(R.id.tv_server_name);
            descr = (TextView) itemView.findViewById(R.id.tv_server_descr);
            initViews();
        }

        private void initViews(){
            //TODO: Listeners here
        }

        private void loadItem(Server srv){
            item = srv;
            name.setText(srv.name);
            descr.setText(srv.description);
        }


    }
}
