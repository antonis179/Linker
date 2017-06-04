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
import org.amoustakos.linker.util.RxUtil;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;

public class ServerAdapter extends RealmRecyclerViewAdapter<Server> {

    final Context context;
    final RealmManager realmManager;

    private Realm realm;
    private LayoutInflater inflater;

    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private final PublishSubject<Integer> onLongClickSubject = PublishSubject.create();


    public ServerAdapter(Context context, RealmManager realmManager) {
        this.context = context;
        this.realmManager = realmManager;

        onClickSubject.compose(RxUtil.applyDefaultSchedulers());
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


    public Observable<Integer> getPositionClicks(){
        return onClickSubject;
    }

    public Observable<Integer> getLongClicks(){
        return onLongClickSubject;
    }


    public class ServerViewHolder extends RecyclerView.ViewHolder {

        CardView container;
        TextView name;
        TextView descr;
        Server item;

        public ServerViewHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.card_view_container);
            name = (TextView) itemView.findViewById(R.id.tv_server_name);
            descr = (TextView) itemView.findViewById(R.id.tv_server_descr);

            container.setOnClickListener(v->onClickSubject.onNext(getAdapterPosition()));
            container.setOnLongClickListener(v -> {
                onLongClickSubject.onNext(getAdapterPosition());
                return true;
            });
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
